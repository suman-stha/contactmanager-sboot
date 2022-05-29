package com.smart.smartcontactmanager.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import com.smart.smartcontactmanager.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotController {
    @Autowired
    private EmailService emailService;
    Random random = new Random(1000);

    // email id form open handler
    @RequestMapping("/forgot")
    public String openEmailForm() {

        return "forgot_email_form";
    }

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam("email") String email, HttpSession session) {
        System.out.println("Email " + email);

        // generating otp of 4 digit

        int otp = random.nextInt(999999);
        System.out.println("OTP " + otp);

        // write code to send otp

        String subject = "OTP From SCM";
        String message = "<h1> OTP =" + otp + " </h1>";
        String to = email;

        boolean flag = this.emailService.sendEmail(subject, message, to);

        if (flag) {

            session.setAttribute("myotp", otp);
            session.setAttribute("email", email);
            return "verify_otp";

        } else {
            session.setAttribute("message", "Check your email id!");
            return "forgot_email_form";
        }

    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") Integer otp, HttpSession session) {
        Integer myOtp = (int) session.getAttribute("myotp");
        String email = (String) session.getAttribute("email");

        if (myOtp == otp) {
            // show password change form
            return "password_change_form";

        } else {
            session.setAttribute("message", "You have entered wrong otp!!");
            return "verify_otp";
        }

    }

}

