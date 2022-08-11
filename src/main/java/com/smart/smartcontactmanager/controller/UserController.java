package com.smart.smartcontactmanager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.smart.smartcontactmanager.dao.ContactRepository;
import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.entities.Contact;
import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ContactRepository contactRepository;

    // method for adding common data to response
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {

        String userName = principal.getName();
        System.out.println("USERNAME " + userName);
        // get the user name(Email)
        User user = userRepository.getUserByUserName(userName);
        System.out.println("USER " + user);

        model.addAttribute("user", user);

    }

    // dashboard home
    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal) {// principal coming from spring security
        model.addAttribute("title", "Home Page");
        return "normal/user_dashboard";
    }

    // open add form handler
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model) {
        model.addAttribute("title", "Add contact");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";

    }

    // processing add contact form
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact,
            @RequestParam("profileImage") MultipartFile file,
            Principal principal, HttpSession session) {
        try {
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);

            // proessing and uploading file..
            if (file.isEmpty()) {
                // if the file is empty then try our message
                System.out.println("File is empty");
                contact.setImage("contact.png");
            } else {
                // upload file to folder and update the name to contact
                contact.setImage(file.getOriginalFilename());
                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Image is uploaded");
            }
            user.getContacts().add(contact);
            contact.setUser(user);

            this.userRepository.save(user);

            System.out.println("ADDED TO DATABASE:  " + user);
            System.out.println("DATA " + contact);

            // message success...
            session.setAttribute("message", new Message("Your contact is added!! ADD more", "success"));

        } catch (Exception e) {
            System.out.println(e.getMessage());

            // error message...
            session.setAttribute("message", new Message("Something went wrong!! Please try again", "danger"));
        }
        // return "normal/add_contact_form";
        return "redirect:/user/show-contacts";

    }

    // show contacts handler
    // per page=5[n] contacts
    // current page=0[page]
    @GetMapping("/show-contacts")
    public String showContacts(Model m, Principal principal) {
        m.addAttribute("title", "Show user contacts");

        // send the list of contacts
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);

        List<Contact> contacts = this.contactRepository.findContactsByUser(user.getId());
        m.addAttribute("contacts", contacts);

        // String userName = principal.getName();
        // User user = this.userRepository.getUserByUserName(userName);
        // List<Contact> contacts = user.getContacts();

        return "normal/show_contacts";
    }

    // showing particular contact details
    @RequestMapping("/{cId}/contact")
    public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal) {
        System.out.println("CID" + cId);
        Optional<Contact> contactOptional = this.contactRepository.findById(cId);
        Contact contact = contactOptional.get();

        //
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);

        if (user.getId() == contact.getUser().getId()) {

            model.addAttribute("contact", contact);
            model.addAttribute("title", contact.getName());

        }

        return "normal/contact_detail";
    }

    // delete contact handler

    @RequestMapping("/delete/{cid}")
    @Transactional
    public String deleteContact(@PathVariable("cid") Integer cId, Model model, Principal principal,
            HttpSession session) {
        System.out.println("CID" + cId);
        Optional<Contact> contactOptional = this.contactRepository.findById(cId);
        Contact contact = contactOptional.get();

        User user = this.userRepository.getUserByUserName(principal.getName());
        user.getContacts().remove(contact);
        this.userRepository.save(user);

        this.contactRepository.delete(contact);
        System.out.println("Deleted");
        session.setAttribute("message", new Message("Contact deleted Successfully...", "success"));

        return "redirect:/user/show-contacts";
    }

    // open update contact handler
    @PostMapping("/update-contact/{cid}")

    public String updateForm(@PathVariable("cid") Integer cid, Model model) {
        model.addAttribute("title", "Update Contact");

        Contact contact = this.contactRepository.findById(cid).get();

        model.addAttribute("contact", contact);
        System.out.println(contact);
        return "normal/update_form";
    }

    // update contact handler
    @RequestMapping(value = "/process-update", method = RequestMethod.POST) // @Postmapping("/process-update")//same

    public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
            Model model, HttpSession session, Principal principal) {

        try {

            // old contact detail
            Contact oldContactDetail = this.contactRepository.findById(contact.getcId()).get();
            // image
            if (!file.isEmpty()) {
                // delete old photo
                File deleteFile = new ClassPathResource("static/img").getFile();
                File file1 = new File(deleteFile, oldContactDetail.getImage());
                file1.delete();
                // update new photo

                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                contact.setImage(file.getOriginalFilename());

            }

            else {
                contact.setImage(oldContactDetail.getImage());

            }
            User user = this.userRepository.getUserByUserName(principal.getName());

            contact.setUser(user);
            this.contactRepository.save(contact);
            session.setAttribute("message", new Message("Your contact is updated", "success"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("CONTACT NAME " + contact.getName());
        System.out.println("CONTACT ID " + contact.getcId());

        return "redirect:/user/" + contact.getcId() + "/contact";
        // return "redirect:/user/show-contacts";

    }

    // Your profile handler
    @GetMapping("/profile")
    public String yourProfile(Model model) {
        model.addAttribute("title", "Profile Page");
        return "normal/profile";
    }

    // open settings handler
    @GetMapping("/settings")
    public String openSetting() {
        return "normal/settings";
    }

    // change password handler
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword, Principal principal,
            HttpSession session) {

        System.out.println("Old Password " + oldPassword);
        System.out.println("NEw Password " + newPassword);
        String userName = principal.getName();
        User currentUser = this.userRepository.getUserByUserName(userName);
        System.out.println(currentUser.getPassword());

        if (this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
            // change the password

            currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
            this.userRepository.save(currentUser);
            session.setAttribute("message", new Message("Your message is succssfully updated.", "success"));
        } else {
            // errror
            session.setAttribute("message", new Message("Wrong password please try again!! ", "danger"));
            return "redirect:/user/settings";
         }
        return "redirect:/user/index";
    }
}
