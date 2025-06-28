package com.BugTrackingPortal.bugbuster.controller;

import com.BugTrackingPortal.bugbuster.dto.BugResponse;
import com.BugTrackingPortal.bugbuster.dto.BugStatusUpdateRequest;
import com.BugTrackingPortal.bugbuster.service.BugService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class DeveloperController {

    private final BugService bugService;

    @GetMapping("/bugs/assigned")
    public ResponseEntity<List<BugResponse>> getAssignedBugs(Authentication auth) {
        return ResponseEntity.ok(bugService.getBugsAssignedToDeveloper(auth));
    }

    @PutMapping("/bugs/{bugId}/status")
    public ResponseEntity<BugResponse> updateBugStatus(
            @PathVariable Long bugId,
            @RequestBody BugStatusUpdateRequest request,
            Authentication auth
    ) {
        return ResponseEntity.ok(bugService.updateBugStatus(bugId, request, auth));
    }
}
