package com.example.group35_base.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    private String username;
    private String password;
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GameProgress> gameProgresses;



    //Profile Fields by Josh
    private String name;
    private String email;
    private String country;

    //XP and level fields by Tobi
    private int xp = 0;
    private int level = 1;

    //XP threshold for levels
    private static final int base_xp_threshold = 150;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    private boolean easyCompleted = false;
    private boolean mediumCompleted = false;
    private boolean hardCompleted = false;


    public boolean isEasyCompleted() { return easyCompleted; }
    public void setEasyCompleted(boolean easyCompleted) { this.easyCompleted = easyCompleted; }

    public boolean isMediumCompleted() { return mediumCompleted; }
    public void setMediumCompleted(boolean mediumCompleted) { this.mediumCompleted = mediumCompleted; }

    public boolean isHardCompleted() { return hardCompleted; }
    public void setHardCompleted(boolean hardCompleted) { this.hardCompleted = hardCompleted; }





    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



    public int getXp() {return xp;}
    public void setXp(int xp) { this.xp = xp; }
    public int getLevel() {return level;}
    public void setLevel(int level) { this.level = level; }

    public void addXp(int amount) {
        this.xp += amount;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int requiredXp = getXpThreshold();
        while (xp >= requiredXp) {
            level++;
            xp -= requiredXp;
            requiredXp = getXpThreshold();
        }
    }

    private int getXpThreshold() {
        return base_xp_threshold;
    }

    public List<GameProgress> getGameProgresses() {
        return gameProgresses;
    }

    public void setGameProgresses(List<GameProgress> gameProgresses) {
        this.gameProgresses = gameProgresses;
    }
}
