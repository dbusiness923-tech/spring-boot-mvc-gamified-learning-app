package com.example.group35_base.controller;

import com.example.group35_base.model.GameProgress;
import com.example.group35_base.model.Puzzle;
import com.example.group35_base.model.User;
import com.example.group35_base.service.PuzzleService;
import com.example.group35_base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PuzzleController {

    private final PuzzleService puzzleService;
    private final UserService userService;

    @Autowired
    public PuzzleController(PuzzleService puzzleService, UserService userService) {
        this.puzzleService = puzzleService;
        this.userService = userService;
    }



    // Show puzzles based on difficulty
    @GetMapping("/puzzles/{difficulty}")
    public String showPuzzle(@PathVariable ("difficulty") String difficulty, Model model) {
        User user = userService.loggedInUser();
        if (user == null) {
            return "redirect:/login";
        }

        Puzzle puzzle = puzzleService.getRandomPuzzle(difficulty);
        if (puzzle == null) {
            model.addAttribute("error", "No puzzles available for this difficulty.");
            return "Puzzles/error";
        }
        model.addAttribute("puzzle", puzzle);
        model.addAttribute("xp", user.getXp());
        model.addAttribute("level", user.getLevel());
        return "Puzzles/puzzle";
    }

    @PostMapping("/api/puzzles/check")
    public String checkAnswer(@RequestParam Long puzzleId, @RequestParam String answer, Model model) {
        User user = userService.loggedInUser();
        if (user == null) {
            return "redirect:/login";
        }
        // Get the puzzle from the service
        Puzzle puzzle = puzzleService.getPuzzleById(puzzleId);

        // Handle null case
        if (puzzle == null) {
            model.addAttribute("error", "Puzzle not found.");
            return "Puzzles/error";
        }

        GameProgress progress = userService.getGameProgress(user, puzzle);
        System.out.println("[DEBUG] Current progress: " + progress);  // Log existing progress

        // If no progress exists, create a new one with start time
        if (progress.getStartTime() == null) {
            progress.setStartTime(LocalDateTime.now());
            System.out.println("[DEBUG] Start time set: " + progress.getStartTime());
        }

        boolean isCorrect = puzzleService.checkAnswer(puzzleId, answer);

        // Calculate completion time only if the puzzle is solved
        int completionTimeInSeconds = 0;
        if (isCorrect) {
            progress.setEndTime(LocalDateTime.now());
            completionTimeInSeconds = (int) Duration.between(progress.getStartTime(), progress.getEndTime()).getSeconds();
            progress.setCompletionTimeInSeconds(completionTimeInSeconds);
            progress.setCompleted(true);
            System.out.println("[DEBUG] Puzzle solved in: " + progress.getCompletionTimeInSeconds() + "s");

            String difficulty = puzzle.getDifficulty().toLowerCase();
            switch (difficulty) {
                case "easy":
                    user.setEasyCompleted(true);
                    break;
                case "medium":
                    user.setMediumCompleted(true);
                    break;
                case "hard":
                    user.setHardCompleted(true);
                    break;
            }
            userService.saveUser(user);
        }


        userService.saveProgress(user, puzzle, isCorrect, completionTimeInSeconds);

        // Add the puzzle and result to the model
        model.addAttribute("isCorrect", isCorrect);
        model.addAttribute("puzzle", puzzle);
        model.addAttribute("completionTime", progress.getCompletionTimeInSeconds()); // Pass to view






        //Award appropriate xp if answer is correct

        if (isCorrect) {

            String difficulty = puzzle.getDifficulty();

            int xpReward;
            switch (difficulty.toUpperCase()) {
                case "EASY":
                    xpReward = 30;
                    break;
                    case "MEDIUM":
                        xpReward = 60;
                        break;
                    case "HARD":
                        xpReward = 100;
                        break;
                    default:
                        xpReward = 0;
                }

                userService.awardXp(user, xpReward);


            }


        model.addAttribute("xp", user.getXp());
        model.addAttribute("level", user.getLevel());


        // Redirect to a result page based on difficulty
        switch (puzzle.getDifficulty()) {
            case Puzzle.EASY:
                return "Puzzles/easyResult";
            case Puzzle.MEDIUM:
                return "Puzzles/mediumResult";
            case Puzzle.HARD:
                return "Puzzles/hardResult";
            default:
                return "Puzzles/error"; // Catch unexpected cases
        }
    }
    @GetMapping("/puzzles/progress")
    public String loadProgress(Model model) {
        User user = userService.loggedInUser();
        if (user == null) {
            return "redirect:/login";
        }
        List<GameProgress> progress = userService.loadProgress(user);
        model.addAttribute("progress", progress);
        return "Puzzles/progress";
    }





}
