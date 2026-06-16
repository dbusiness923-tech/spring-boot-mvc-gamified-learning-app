package com.example.group35_base.repo;

import com.example.group35_base.model.GameProgress;
import com.example.group35_base.model.Puzzle;
import com.example.group35_base.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GameProgressRepository extends JpaRepository<GameProgress, Long> {
    @Query("SELECT p FROM Puzzle p WHERE p NOT IN " +
            "(SELECT gp.puzzle FROM GameProgress gp WHERE gp.user = :user AND gp.completed = true)")
    List<Puzzle> findIncompletePuzzlesByUser(@Param("user") User user);
    List<GameProgress> findByUser(User user);
    List<GameProgress> findByUserAndCompletedOrderByCompletionTimeInSecondsAsc(User user, boolean completed);
    List<GameProgress> findByUserAndCompleted(User user, boolean completed);
    Optional<GameProgress> findByUserAndPuzzle(User user, Puzzle puzzle);
    @Query("SELECT COUNT(gp) FROM GameProgress gp WHERE gp.user = :user AND gp.completed = true")
    int countByUserAndCompleted(@Param("user") User user, @Param("completed") boolean completed);

}

