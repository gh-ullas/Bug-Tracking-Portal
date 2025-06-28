package com.BugTrackingPortal.bugbuster.repository;

import com.BugTrackingPortal.bugbuster.model.Bug;
import com.BugTrackingPortal.bugbuster.model.User;
import com.BugTrackingPortal.bugbuster.model.enums.BugStatus;
import com.BugTrackingPortal.bugbuster.model.enums.Severity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BugRepository extends JpaRepository<Bug, Long> {
    List<Bug> findByReportedBy(User user);

    long countByStatus(BugStatus status);

    long countByAssignedTo(User user);

    List<Bug> findByCreatedAtAfter(LocalDateTime dateTime);

    long countBySeverity(Severity severity);


}
