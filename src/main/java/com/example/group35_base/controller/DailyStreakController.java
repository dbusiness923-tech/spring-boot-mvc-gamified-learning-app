package com.example.group35_base.controller;

import com.example.group35_base.model.User;
import com.example.group35_base.service.DailyStreakService;
import com.example.group35_base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DailyStreakController {

    private final DailyStreakService dailyStreakService;
    private final UserService userService;

    @Autowired
    public DailyStreakController(DailyStreakService dailyStreakService, UserService userService) {
        this.dailyStreakService = dailyStreakService;
        this.userService = userService;
    }

    // GET page showing the user's current streak and claim button
    @GetMapping("/daily-streak")
    public String showStreakPage(Model model) {
        User user = userService.loggedInUser();
        if (user == null) {
            return "redirect:/login";
        }

        int streakCount = dailyStreakService.getStreakCount(user);
        boolean hasClaimed = dailyStreakService.hasClaimedToday(user);

        model.addAttribute("streakCount", streakCount);
        model.addAttribute("hasClaimed", hasClaimed);

        return "dailyStreak/streakPage";
    }

    // POST request when the user claims XP
    @PostMapping("/claim-xp")
    public String claimXp(Model model) {
        User user = userService.loggedInUser();
        if (user == null) {
            return "redirect:/login";
        }

        boolean hasClaimed = dailyStreakService.hasClaimedToday(user);
        if (hasClaimed) {
            model.addAttribute("message", "⚠️ You’ve already claimed XP today!");
        } else {
            int newStreak = dailyStreakService.claimXpForToday(user);
            model.addAttribute("message", "✅ XP claimed! Streak is now " + newStreak + " 🔥");
        }

        // Reload updated streak info
        int updatedStreak = dailyStreakService.getStreakCount(user);
        boolean claimedNow = dailyStreakService.hasClaimedToday(user);
        model.addAttribute("streakCount", updatedStreak);
        model.addAttribute("hasClaimed", claimedNow);

        return "dailyStreak/streakPage";
    }
}


