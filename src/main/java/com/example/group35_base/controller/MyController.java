package com.example.group35_base.controller;


import com.example.group35_base.model.User;
import com.example.group35_base.repo.UserRepository;
import com.example.group35_base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;

@Controller
public class MyController {

    @Autowired
    private UserService userService;



    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, String password) {
        userService.registerUser(username, password);
        return "redirect:/login";

    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/welcome")
    public String welcome(Model model) {
        User user = userService.loggedInUser();

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("xp", user.getXp());
        model.addAttribute("level", user.getLevel());
        return "welcome";
    }
}

