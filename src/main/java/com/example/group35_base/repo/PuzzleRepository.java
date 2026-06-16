package com.example.group35_base.repo;

import com.example.group35_base.model.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PuzzleRepository extends JpaRepository<Puzzle, Long> {
    List<Puzzle> findByDifficulty(String difficulty);

}
