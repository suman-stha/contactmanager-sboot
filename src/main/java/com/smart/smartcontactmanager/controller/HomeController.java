package com.smart.smartcontactmanager.controller;

import com.smart.smartcontactmanager.entities.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

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
    public String registerUSer(@ModelAttribute("user") User user,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model) {
        System.out.println("Agreemnt: " + agreement);
        System.out.println("User: " + user);
        return "signup";
    }
}
