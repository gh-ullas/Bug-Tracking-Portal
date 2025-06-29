package com.BugTrackingPortal.bugbuster.controller;

import com.BugTrackingPortal.bugbuster.dto.BugAssignRequest;
import com.BugTrackingPortal.bugbuster.dto.BugResponse;
import com.BugTrackingPortal.bugbuster.model.User;
import com.BugTrackingPortal.bugbuster.service.BugService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BugService bugService;

    @GetMapping("/bugs")
    public ResponseEntity<List<BugResponse>> getAllBugs() {
        return ResponseEntity.ok(bugService.getAllBugs());
    }

    @PutMapping("/bugs/{bugId}/assign")
    public ResponseEntity<BugResponse> assignBug(
            @PathVariable Long bugId,
            @RequestBody BugAssignRequest request
    ) {
        return ResponseEntity.ok(bugService.assignBugToDeveloper(bugId, request.getDeveloperId()));
    }

}
