package com.example.group35_base.service;


import com.example.group35_base.model.PageVisit;
import com.example.group35_base.repo.PageVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PageVisitService {

    @Autowired
    private PageVisitRepository repository;

    public void recordPageEntry(String username, String pageUrl) {
        PageVisit visit = new PageVisit(username, pageUrl, LocalDateTime.now());
        repository.save(visit);
    }

    public void recordPageExit(String username, String pageUrl) {
        PageVisit visit = repository.findFirstByUsernameAndPageUrlOrderByStartTimeDesc(username, pageUrl);
        if (visit != null) {
            visit.setEndTime(LocalDateTime.now());
            repository.save(visit);
        }
    }

}
