package com.BugTrackingPortal.bugbuster.controller;

import com.BugTrackingPortal.bugbuster.model.Bug;
import com.BugTrackingPortal.bugbuster.model.enums.BugStatus;
import com.BugTrackingPortal.bugbuster.model.enums.Severity;
import com.BugTrackingPortal.bugbuster.service.DashboardService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/bug-stats")
    public ResponseEntity<Map<BugStatus, Long>> getBugStats() {
        return ResponseEntity.ok(dashboardService.getBugCountByStatus());
    }

    @GetMapping("/developer-load")
    public ResponseEntity<Map<String, Long>> getDeveloperBugCounts() {
        return ResponseEntity.ok(dashboardService.getBugsPerDeveloper());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Bug>> getRecentBugs() {
        return ResponseEntity.ok(dashboardService.getRecentBugs());
    }

    @GetMapping("/severity")
    public ResponseEntity<Map<Severity, Long>> getBugBySeverity() {
        return ResponseEntity.ok(dashboardService.getBugCountBySeverity());
    }
}
