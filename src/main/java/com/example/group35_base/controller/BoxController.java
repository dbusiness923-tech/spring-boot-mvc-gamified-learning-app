package com.example.group35_base.controller;



import com.example.group35_base.model.User;
import com.example.group35_base.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.group35_base.model.User;
import org.springframework.ui.Model;
import com.example.group35_base.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BoxController {


    //@Autowired
    //private UserService userService;


    private final UserService userService;

    @Autowired  // Constructor injection (recommended)
    public BoxController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/box1")
    public String showBox1(Model model) {
        User user = userService.loggedInUser();


        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("xp", user.getXp());
        model.addAttribute("level", user.getLevel());


        if (user == null) return "redirect:/login";

        model.addAttribute("fastestTimes",
                userService.getFastestCompletionTimes(user));

        return "TimeCompletion/fastestTime";
    }

    @GetMapping("/box2")
    public String showBox2(Model model) {

        User user = userService.loggedInUser();

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("xp", user.getXp());
        model.addAttribute("level", user.getLevel());


        return "Puzzles/PuzzlesPage";
    }

    @GetMapping("/box3")
    public String showBox3(Model model) {


        User user = userService.loggedInUser();

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("xp", user.getXp());
        model.addAttribute("level", user.getLevel());


        user = userService.loggedInUser();
        if (user == null) return "redirect:/login";

        model.addAttribute("completedGames",
                userService.getCompletedGames(user));

        return "completedGames/gamesCompleted";
    }

    @GetMapping("/box4")
    public String showBox4(Model model) {
        User currentUser = userService.loggedInUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("currentUser", currentUser); // Add this
        model.addAttribute("xp", currentUser.getXp());
        model.addAttribute("level", currentUser.getLevel());

        List<User> leaderboard = userService.getAllUsersOrderedByLevelAndXp();
        model.addAttribute("leaderboard", leaderboard);

        Map<Long, Integer> completedCounts = new HashMap<>();
        for (User user : leaderboard) {
            completedCounts.put(user.getId(),
                    userService.getCompletedPuzzleCount(user));
        }
        model.addAttribute("completedCounts", completedCounts);
        return "Rankings/Leaderboard";
    }

    @GetMapping("/box5")
    public String getBox5(Model model) {
        User user = userService.loggedInUser();
        model.addAttribute("user", user);
        return "box5";
    }

}
