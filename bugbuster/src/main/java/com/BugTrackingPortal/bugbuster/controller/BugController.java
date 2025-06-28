package com.BugTrackingPortal.bugbuster.controller;

import com.BugTrackingPortal.bugbuster.dto.BugRequest;
import com.BugTrackingPortal.bugbuster.dto.BugResponse;
import com.BugTrackingPortal.bugbuster.dto.CreateCommentRequest;
import com.BugTrackingPortal.bugbuster.model.Bug;
import com.BugTrackingPortal.bugbuster.service.BugService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bugs")
@RequiredArgsConstructor
public class BugController {

    private final BugService bugService;

    @PostMapping(value = "/report", consumes = "multipart/form-data")
    public ResponseEntity<BugResponse> reportBug(
            @ModelAttribute BugRequest bugRequest,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Authentication auth
    ) throws Exception {
        return ResponseEntity.ok(bugService.reportBug(bugRequest, file, auth));
    }


    @GetMapping("/my")
    public ResponseEntity<List<BugResponse>> getMyBugs(Authentication auth) {
        return ResponseEntity.ok(bugService.getBugsReportedByUser(auth));
    }

    @PostMapping("/{bugId}/comments")
    public ResponseEntity<Bug> addComment(
            @PathVariable Long bugId,
            @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        String author = userDetails.getUsername();
        Bug updatedBug = bugService.addComment(bugId, request.getText(), author);
        return ResponseEntity.ok(updatedBug);
    }

    @DeleteMapping("/{bugId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBug(@PathVariable Long bugId) {
        bugService.deleteBug(bugId);
        return ResponseEntity.ok(Map.of("message", "Bug deleted successfully"));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<Bug>> searchBugs(
            @RequestParam(value = "q", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "severity", required = false) String severity,
            @RequestParam(value = "priority", required = false) String priority
    ) {
        List<Bug> bugs = bugService.searchBugs(keyword, status, severity, priority);
        return ResponseEntity.ok(bugs);
    }



}
