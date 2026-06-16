package com.example.group35_base.controller;


import com.example.group35_base.service.PageVisitService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/page-visit")
public class PageVisitController {

    @Autowired
    private PageVisitService visitService;

    @GetMapping("/dashboard")
    public String loadDashboard(HttpSession session) {
        String username = "guest"; // Replace with actual logged-in user if needed
        session.setAttribute("startTime", LocalDateTime.now());
        visitService.recordPageEntry(username, "/dashboard");
        return "dashboard";
    }

    @PostMapping("/dashboard/exit")
    public String exitDashboard(HttpSession session) {
        String username = "guest";
        LocalDateTime startTime = (LocalDateTime) session.getAttribute("startTime");

        if (startTime != null) {
            visitService.recordPageExit(username, "/dashboard");
            session.removeAttribute("startTime");
        }
        return "redirect:/";
    }
}