package com.example.group35_base.service;

import com.example.group35_base.model.DailyStreak; // stays for now (used in other methods)
import com.example.group35_base.model.GameProgress;
import com.example.group35_base.model.Puzzle;
import com.example.group35_base.model.User;
import com.example.group35_base.repo.DailyStreakRepository;
import com.example.group35_base.repo.GameProgressRepository;
import com.example.group35_base.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import java.time.LocalDateTime;


@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GameProgressRepository gameProgressRepository;

    @Autowired
    private DailyStreakRepository dailyStreakRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        user.setXp(0);
        user.setLevel(1);
        userRepository.save(user);
    }

    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return null; // not logged in
        }

        // Just return user, don't touch streaks or XP here
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    public void updateProfile(User updatedUser) {
        User existingUser = loggedInUser();
        if (existingUser == null) return;

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setCountry(updatedUser.getCountry());

        userRepository.save(existingUser);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);

    }



    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void awardXp(User user, int amount) {
        user.addXp(amount);
        userRepository.save(user);
    }

    public Map<String, Integer> getXpAndLevel(Long userId) {
        Optional<Object[]> userOptional = userRepository.findXpAndLevelByUserId(userId);

        if (userOptional.isEmpty()) return null;

        Object[] data = userOptional.get();
        Map<String, Integer> xpLevelData = new HashMap<>();
        xpLevelData.put("xp", loggedInUser().getXp());
        xpLevelData.put("level", loggedInUser().getLevel());
        return xpLevelData;
    }

    public void saveProgress(User user, Puzzle puzzle, boolean completed, int completionTime) {
        GameProgress progress = gameProgressRepository.findByUserAndPuzzle(user, puzzle)
                .orElse(new GameProgress());

        progress.setUser(user);
        progress.setPuzzle(puzzle);
        progress.setCompleted(completed);
        progress.setLastPlayed(LocalDateTime.now());

        if (progress.getStartTime() == null) {
            progress.setStartTime(LocalDateTime.now());
        }

        if (completed) {
            progress.setEndTime(LocalDateTime.now());
            long duration = Duration.between(progress.getStartTime(), progress.getEndTime()).getSeconds();
            progress.setCompletionTimeInSeconds((int) duration);
        }

        // Debugging: Print values before saving
        System.out.println("Start Time: " + progress.getStartTime());
        System.out.println("End Time: " + progress.getEndTime());
        System.out.println("Completion Time: " + progress.getCompletionTimeInSeconds());


        gameProgressRepository.save(progress);
    }

    public List<GameProgress> loadProgress(User user) {
        return gameProgressRepository.findByUser(user);
    }

    public List<GameProgress> getFastestCompletionTimes(User user) {
        return gameProgressRepository.findByUserAndCompletedOrderByCompletionTimeInSecondsAsc(user, true);
    }

    public List<GameProgress> getCompletedGames(User user) {
        return gameProgressRepository.findByUserAndCompleted(user, true);
    }

    public GameProgress getGameProgress(User user, Puzzle puzzle) {
        return gameProgressRepository.findByUserAndPuzzle(user, puzzle)
                .orElse(new GameProgress());
    }

    public List<Puzzle> getIncompletePuzzles(User user) {
        return gameProgressRepository.findIncompletePuzzlesByUser(user);
    }
    public List<User> getAllUsersOrderedByLevelAndXp() {
        return userRepository.findAllByOrderByLevelDescXpDesc();
    }

    public int getCompletedPuzzleCount(User user) {
        return gameProgressRepository.countByUserAndCompleted(user, true);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }



}
