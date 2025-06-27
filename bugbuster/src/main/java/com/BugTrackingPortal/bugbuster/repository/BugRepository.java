package com.BugTrackingPortal.bugbuster.repository;

import com.BugTrackingPortal.bugbuster.model.Bug;
import com.BugTrackingPortal.bugbuster.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BugRepository extends JpaRepository<Bug, Long> {
    List<Bug> findByReportedBy(User user);
}
