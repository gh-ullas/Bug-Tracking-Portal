package com.BugTrackingPortal.bugbuster.dto;


import com.BugTrackingPortal.bugbuster.model.enums.Severity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BugRequest {
    private String title;
    private String description;
    private Severity severity;
    private String stepsToReproduce;
}


