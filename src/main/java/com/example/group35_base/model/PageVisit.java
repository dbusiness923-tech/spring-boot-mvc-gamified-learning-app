package com.example.group35_base.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PageVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String pageUrl; // URL or identifier of the page
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public PageVisit() {}

    public PageVisit(String username, String pageUrl, LocalDateTime startTime) {
        this.username = username;
        this.pageUrl = pageUrl;
        this.startTime = startTime;
    }

    // Getters and Setters
    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPageUrl() {

        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {

        this.pageUrl = pageUrl;
    }

    public LocalDateTime getStartTime() {

        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {

        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {

        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {

        this.endTime = endTime;
    }
}
