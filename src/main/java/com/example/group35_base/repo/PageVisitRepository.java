package com.example.group35_base.repo;


import com.example.group35_base.model.PageVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageVisitRepository extends JpaRepository<PageVisit, Long> {
    PageVisit findFirstByUsernameAndPageUrlOrderByStartTimeDesc(String username, String pageUrl);
}
