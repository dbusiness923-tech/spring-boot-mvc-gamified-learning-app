package com.example.group35_base.controller;

import com.example.group35_base.model.GameProgress;
import com.example.group35_base.model.User;
import com.example.group35_base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProgressController {

    private final UserService userService;

    @Autowired
    public ProgressController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/fastest-time")
    public String showFastestTime(Model model) {
        User user = userService.loggedInUser();
        if (user == null) {
            return "redirect:/login";
        }

        // Fetch user's fastest completion times
        List<GameProgress> fastestTimes = userService.getFastestCompletionTimes(user);
        // Debug logging
        System.out.println("==== FASTEST TIMES DATA ====");
        fastestTimes.forEach(progress -> {
            System.out.println("Puzzle: " + progress.getPuzzle().getQuestion());
            System.out.println("Time: " + progress.getCompletionTimeInSeconds() + "s");
        });

        model.addAttribute("fastestTimes", fastestTimes);
        return "TimeCompletion/fastestTime"; // Name of the Thymeleaf template (fastestTime.html)
    }

    @GetMapping("/completed-games")
    public String showCompletedGames(Model model) {
        User user = userService.loggedInUser();
        if (user == null) {
            return "redirect:/login";
        }

        // Fetch user's completed games
        List<GameProgress> completedGames = userService.getCompletedGames(user);
        model.addAttribute("completedGames", completedGames);

        return "completedGames/gamesCompleted"; // Name of the Thymeleaf template (completedGames.html)
    }
}