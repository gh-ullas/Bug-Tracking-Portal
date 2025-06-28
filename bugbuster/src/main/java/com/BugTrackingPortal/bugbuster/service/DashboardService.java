package com.BugTrackingPortal.bugbuster.service;

import com.BugTrackingPortal.bugbuster.model.Bug;
import com.BugTrackingPortal.bugbuster.model.User;
import com.BugTrackingPortal.bugbuster.model.enums.BugStatus;
import com.BugTrackingPortal.bugbuster.model.enums.Severity;
import com.BugTrackingPortal.bugbuster.repository.BugRepository;
import com.BugTrackingPortal.bugbuster.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final BugRepository bugRepo;
    private final UserRepository userRepo;

    public Map<BugStatus, Long> getBugCountByStatus() {
        return Arrays.stream(BugStatus.values())
                .collect(Collectors.toMap(
                        status -> status,
                        status -> bugRepo.countByStatus(status)
                ));
    }

    public Map<String, Long> getBugsPerDeveloper() {
        return userRepo.findAll().stream()
                .filter(user -> user.getRoles().stream().anyMatch(r -> r.getName().equals("DEVELOPER")))
                .collect(Collectors.toMap(
                        User::getName,
                        user -> bugRepo.countByAssignedTo(user)
                ));
    }

    public List<Bug> getRecentBugs() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        return bugRepo.findByCreatedAtAfter(oneWeekAgo);
    }

    public Map<Severity, Long> getBugCountBySeverity() {
        return Arrays.stream(Severity.values())
                .collect(Collectors.toMap(
                        severity -> severity,
                        severity -> bugRepo.countBySeverity(severity)
                ));
    }
}
