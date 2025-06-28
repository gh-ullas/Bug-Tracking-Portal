package com.BugTrackingPortal.bugbuster.dto;

import com.BugTrackingPortal.bugbuster.model.enums.BugStatus;
import com.BugTrackingPortal.bugbuster.model.enums.Severity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BugResponse {
    private Long id;
    private String title;
    private String description;
    private Severity severity;
    private BugStatus status;
    private String reportedBy;
    private String stepsToReproduce;
    private LocalDateTime createdAt;
    private String assignedTo;

}
