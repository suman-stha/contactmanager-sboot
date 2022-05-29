package com.smart.smartcontactmanager.service;

import java.util.Properties;

import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    public boolean sendEmail(String subject, String message, String to) {

        // rest of the code
        boolean f = false;

        String from = "shresthasuman919@gmail.com";
        // this method is responsible for sending email

        // //Variable for gmail host
        String host = "smtp.gmail.com";
        // //get the system properties
        Properties properties = System.getProperties();
        System.out.println("PROPERTIES " + properties);

        // //setting important information properties object
        // //host set
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.auth", "true");

        // //Step 1:to get the session object..

        Session session = Session.getInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("shresthasuman919@gmail.com", "");
            }

        });
        session.setDebug(true);
        // //step2: compose the message[text, multimedia

        MimeMessage m = new MimeMessage(session);

        // //from email
        try {
            m.setFrom(from);

            // //adding recipient to message
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // //adding subject to message
            m.setSubject(subject);

            // //adding text to message
            m.setText(message);

            // //send

            // //step 3: send the message using Transport class

            Transport.send(m);
            System.out.println("Sent sucess........");
            f = true;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return f;

    }

}
