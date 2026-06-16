package com.example.group35_base.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import com.example.group35_base.model.User;
import com.example.group35_base.repo.UserRepository;
import com.example.group35_base.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
public class ProfileController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public String viewProfile(Model model) {

        User user = userService.loggedInUser(); // Gets the currently logged-in user

        if (user == null) {
            System.out.println(" User is NULL! Redirecting to login...");
            return "redirect:/login"; //Redirect if no user is found
        }

        System.out.println(" User found: " + user.getUsername()); // Debugging Log
        model.addAttribute("user", user); //Adds user details to the model
        model.addAttribute("xp", user.getXp());
        model.addAttribute("level", user.getLevel());

        return "profile/view";
    }


    @GetMapping("/edit")
    public String editProfile(Model model) {
        User user = userService.loggedInUser();

        if (user == null) {
            System.out.println(" User is NULL! Redirecting to login...");
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "profile/edit";

    }




    @PostMapping("/profile/update") public String updateProfile(@ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "profile/edit"; //Return to edit page if validation errors exist
        }
        userService.updateProfile(user); // Update the users profile
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/profile";
    }

    @PostMapping("/profile/delete")
    public String deleteProfile(@ModelAttribute("user") RedirectAttributes redirectAttributes) {
        User user = userService.loggedInUser();
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found or not logged in.");
            return "redirect:/Login";
        }

        try {
            userService.deleteUser(user);
            SecurityContextHolder.getContext().setAuthentication(null);
            SecurityContextHolder.clearContext();
            redirectAttributes.addFlashAttribute("successMessage", "Profile deleted successfully!");
            System.out.println("Profile deleted, redirecting to login.");
        } catch (Exception e) {
            System.err.println("Error during profile deletion: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "An error occurred while deleting the profile.");
            return "redirect:/error";
        }
        return "redirect:/Login";
    }






}
