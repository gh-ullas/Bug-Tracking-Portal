package com.BugTrackingPortal.bugbuster.controller;

import com.BugTrackingPortal.bugbuster.dto.BugRequest;
import com.BugTrackingPortal.bugbuster.dto.BugResponse;
import com.BugTrackingPortal.bugbuster.service.BugService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
}
