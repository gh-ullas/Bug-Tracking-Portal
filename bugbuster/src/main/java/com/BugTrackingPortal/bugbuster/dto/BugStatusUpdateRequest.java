package com.BugTrackingPortal.bugbuster.dto;

import com.BugTrackingPortal.bugbuster.model.enums.BugStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BugStatusUpdateRequest {
    private BugStatus status;
}
