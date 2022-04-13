package com.smart.smartcontactmanager.controller;

import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.smart.smartcontactmanager.dao.UserRepository;

@Controller
public class HomeController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    // handler for home page
    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Home-Smart Contact Manger");

        return "home";

    }

    // handler for about page
    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About-smart contact manager");
        return "about";

    }

    // handler for signup page
    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register smart contact manager");
        model.addAttribute("user", new User());
        return "signup";

    }

    // handler for registering user
    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUSer(@Valid @ModelAttribute("user") User user, BindingResult result,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
            HttpSession session) {

        try {

            if (!agreement) {
                System.out.println("You have not agreed the terms and conditons");
                throw new Exception("You have not agreed the terms and conditons");
            }
            if (result.hasErrors()) {
                System.out.println("ERROR" + result.toString());
                model.addAttribute("user", user);
                return "signup";
            }

            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageUrl("default.png");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println("Agreemnt: " + agreement);
            System.out.println("User: " + user);

            User save = this.userRepository.save(user);

            System.out.println(save);

            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully registered!!", "alert-success"));
        } catch (Exception e) {

            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong !!" + e.getMessage(), "alert-danger"));
        }
        return "signup";
    }

    // handler for custom login
    @GetMapping("/signin")
    public String customLogin(Model model) {
        model.addAttribute("title", "Login Page");
        return "login";

    }
}