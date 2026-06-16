package com.example.group35_base.service;

import com.example.group35_base.model.DailyStreak;
import com.example.group35_base.model.User;
import com.example.group35_base.repo.DailyStreakRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DailyStreakService {

    private final DailyStreakRepository dailyStreakRepository;
    private final UserService userService;

    @Autowired
    public DailyStreakService(DailyStreakRepository dailyStreakRepository, UserService userService) {
        this.dailyStreakRepository = dailyStreakRepository;
        this.userService = userService;
    }

    // Save or update the streak
    public DailyStreak saveStreak(DailyStreak streak) {
        return dailyStreakRepository.save(streak);
    }

    // Admin/testing: get all users’ streaks
    public List<DailyStreak> getAllStreaks() {
        return dailyStreakRepository.findAll();
    }

    // Get streak for specific user (Optional)
    public Optional<DailyStreak> getStreakByUser(User user) {
        return dailyStreakRepository.findAll()
                .stream()
                .filter(s -> s.getUser().getId().equals(user.getId()))
                .findFirst();
    }

    // Return just the streak count for a user (defaults to 0 if none found)
    public int getStreakCount(User user) {
        return getStreakByUser(user)
                .map(DailyStreak::getStreakCount)
                .orElse(0);
    }

    // Has the user already claimed XP today?
    public boolean hasClaimedToday(User user) {
        return getStreakByUser(user)
                .map(s -> LocalDate.now().isEqual(s.getLastLoginDate()))
                .orElse(false);
    }

    // Called when user hits "Claim XP" → handles streak logic & XP awarding
    public int claimXpForToday(User user) {
        DailyStreak streak = getStreakByUser(user).orElseGet(() -> {
            DailyStreak newStreak = new DailyStreak();
            newStreak.setUser(user);
            newStreak.setStreakCount(1);
            newStreak.setLastLoginDate(LocalDate.now());
            dailyStreakRepository.save(newStreak);
            userService.awardXp(user, 10); // First-time claim: 10 XP
            return newStreak;
        });

        LocalDate today = LocalDate.now();
        LocalDate lastLogin = streak.getLastLoginDate();

        // Already claimed today? Don’t re-award
        if (today.isEqual(lastLogin)) {
            return streak.getStreakCount();
        }

        // Continued the streak (next day login)
        if (lastLogin != null && lastLogin.plusDays(1).isEqual(today)) {
            streak.setStreakCount(streak.getStreakCount() + 1);
        } else {
            // Missed a day – reset
            streak.setStreakCount(1);
        }

        // Update login date
        streak.setLastLoginDate(today);
        dailyStreakRepository.save(streak);

        // Award XP = 10 × current streak
        int awardedXp = streak.getStreakCount() * 10;
        userService.awardXp(user, awardedXp);

        return streak.getStreakCount();
    }
}

