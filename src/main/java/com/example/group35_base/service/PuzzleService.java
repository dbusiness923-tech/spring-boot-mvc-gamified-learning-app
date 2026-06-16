package com.example.group35_base.service;

import com.example.group35_base.model.Puzzle;
import com.example.group35_base.repo.PuzzleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class PuzzleService {
    @Autowired
    private PuzzleRepository puzzleRepository;

    //method to get a random puzzle
    public Puzzle getRandomPuzzle(String difficulty){
        List<Puzzle> puzzles = puzzleRepository.findByDifficulty(difficulty);
        if(puzzles.isEmpty()) throw new RuntimeException("Puzzle not found");

        return puzzles.get(new Random().nextInt(puzzles.size()));
    }

    // method to get  a puzzle by its ID
    public Puzzle getPuzzleById(Long id) {
        return puzzleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Puzzle not found!"));
    }

    public boolean checkAnswer(Long puzzleId, String userAnswer){
        Puzzle puzzle = puzzleRepository.findById(puzzleId)
                .orElseThrow(() -> new RuntimeException("Puzzle not found!"));
        return puzzle.getAnswer().equalsIgnoreCase(userAnswer.trim());


    }

}
