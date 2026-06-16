package com.example.group35_base;

import com.example.group35_base.model.Puzzle;
import com.example.group35_base.repo.PuzzleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PuzzleRepository puzzleRepository;


    //To add initial puzzles to the database
    @Override
    public void run(String... args) throws Exception {
        if (puzzleRepository.count() == 0) {  // If no puzzles in the DB, add some
            // Easy questions
            puzzleRepository.save(new Puzzle("What is 2 + 2?", "4", Puzzle.EASY));
            puzzleRepository.save(new Puzzle("What is the binary representation of 5?", "101", Puzzle.EASY));
            puzzleRepository.save(new Puzzle("What does CPU stand for?", "Central Processing Unit", Puzzle.EASY));

            // Medium questions
            puzzleRepository.save(new Puzzle("What is the time complexity of binary search?", "O(log n)", Puzzle.MEDIUM));
            puzzleRepository.save(new Puzzle("What does HTML stand for?", "HyperText Markup Language", Puzzle.MEDIUM));
            puzzleRepository.save(new Puzzle("What is 15 in hexadecimal?", "F", Puzzle.MEDIUM));

            // Hard questions
            puzzleRepository.save(new Puzzle("What sorting algorithm has the worst-case complexity of O(n²)?", "Bubble Sort", Puzzle.HARD));
            puzzleRepository.save(new Puzzle("Which data structure is used for LRU cache implementation?", "HashMap", Puzzle.HARD));
            puzzleRepository.save(new Puzzle("What is the main purpose of a mutex in multithreading?", "Prevent race conditions", Puzzle.HARD));
            puzzleRepository.save(new Puzzle("What is the space complexity of Merge Sort?", "O(n)", Puzzle.HARD));
        }
    }
}
