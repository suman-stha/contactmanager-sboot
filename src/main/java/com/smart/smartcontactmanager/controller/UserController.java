package com.smart.smartcontactmanager.controller;

import java.security.Principal;

import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal) {// principal coming from spring security

        String userName = principal.getName();
        System.out.println("USERNAME " + userName);
        // get the user name(Email)
        User user = userRepository.getUserByUserName(userName);
        System.out.println("USER " + user);

        model.addAttribute("user", user);

        return "normal/user_dashboard";
    }

}
