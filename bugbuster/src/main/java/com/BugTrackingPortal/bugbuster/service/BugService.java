package com.BugTrackingPortal.bugbuster.service;

import com.BugTrackingPortal.bugbuster.dto.BugRequest;
import com.BugTrackingPortal.bugbuster.dto.BugResponse;
import com.BugTrackingPortal.bugbuster.dto.BugStatusUpdateRequest;
import com.BugTrackingPortal.bugbuster.exception.ResourceNotFoundException;
import com.BugTrackingPortal.bugbuster.model.*;
import com.BugTrackingPortal.bugbuster.model.enums.BugStatus;
import com.BugTrackingPortal.bugbuster.model.enums.Severity;
import com.BugTrackingPortal.bugbuster.repository.BugRepository;
import com.BugTrackingPortal.bugbuster.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BugService {

    private final BugRepository bugRepository;
    private final UserRepository userRepository;

    @Value("${bug.upload.dir:uploads}")
    private String uploadDir;

    @Transactional
    public BugResponse reportBug(BugRequest request, MultipartFile file, Authentication auth) throws IOException {
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        String filePath = null;
        if (file != null && !file.isEmpty()) {
            filePath = saveFile(file);
        }

        Bug bug = new Bug();
        bug.setTitle(request.getTitle());
        bug.setDescription(request.getDescription());
        bug.setSeverity(request.getSeverity());
        bug.setStatus(BugStatus.OPEN);
        bug.setStepsToReproduce(request.getStepsToReproduce());
        bug.setCreatedAt(LocalDateTime.now());
        bug.setReportedBy(user);
        bug.setFilePath(filePath);

        bug = bugRepository.save(bug);
        return toResponse(bug);
    }

    private String saveFile(MultipartFile file) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return path.toString();
    }

    public List<BugResponse> getBugsReportedByUser(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        return bugRepository.findByReportedBy(user)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private BugResponse toResponse(Bug bug) {
        return new BugResponse(
                bug.getId(),
                bug.getTitle(),
                bug.getDescription(),
                bug.getSeverity(),
                bug.getStatus(),
                bug.getReportedBy().getName(),
                bug.getStepsToReproduce(),
                bug.getCreatedAt(),
                bug.getAssignedTo() != null ? bug.getAssignedTo().getName() : null
        );
    }
    @Transactional
    public BugResponse assignBugToDeveloper(Long bugId, Long developerId) {
        Bug bug = bugRepository.findById(bugId)
                .orElseThrow(() -> new RuntimeException("Bug not found"));

        User developer = userRepository.findById(developerId)
                .orElseThrow(() -> new RuntimeException("Developer not found"));

        boolean isDeveloper = developer.getRoles().stream()
                .anyMatch(role -> role.getName().equals("DEVELOPER"));
        if (!isDeveloper) {
            throw new RuntimeException("Assigned user is not a developer");
        }

        bug.setAssignedTo(developer);
        bug.setStatus(BugStatus.IN_PROGRESS); // auto update status
        return toResponse(bugRepository.save(bug));
    }

    public List<BugResponse> getAllBugs() {
        return bugRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    public List<User> getAllDevelopers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("DEVELOPER")))
                .collect(Collectors.toList());
    }
    public List<BugResponse> getBugsAssignedToDeveloper(Authentication auth) {
        User developer = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bugRepository.findAll().stream()
                .filter(bug -> bug.getAssignedTo() != null &&
                        bug.getAssignedTo().getId().equals(developer.getId()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BugResponse updateBugStatus(Long bugId, BugStatusUpdateRequest request, Authentication auth) {
        User developer = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Bug bug = bugRepository.findById(bugId)
                .orElseThrow(() -> new RuntimeException("Bug not found"));

        if (bug.getAssignedTo() == null || !bug.getAssignedTo().getId().equals(developer.getId())) {
            throw new RuntimeException("Unauthorized: This bug is not assigned to you");
        }

        bug.setStatus(request.getStatus());
        return toResponse(bugRepository.save(bug));
    }

    public Bug addComment(Long bugId, String commentText, String author) {
        Bug bug = bugRepository.findById(bugId)
                .orElseThrow(() -> new ResourceNotFoundException("Bug not found"));

        Comment comment = new Comment(author, commentText);
        bug.getComments().add(comment);
        return bugRepository.save(bug);
    }

    public void deleteBug(Long bugId) {
        Bug bug = bugRepository.findById(bugId)
                .orElseThrow(() -> new ResourceNotFoundException("Bug not found"));

        bugRepository.delete(bug);
    }



    public List<Bug> searchBugs(String keyword, String status, String severity, String priority) {
        List<Bug> allBugs = bugRepository.findAll();

        return allBugs.stream()
                .filter(bug -> {
                    if (keyword == null) return true;
                    String lowerKeyword = keyword.toLowerCase();
                    return bug.getTitle().toLowerCase().contains(lowerKeyword) ||
                            bug.getDescription().toLowerCase().contains(lowerKeyword);
                })
                .filter(bug -> {
                    if (status == null) return true;
                    try {
                        return bug.getStatus() == BugStatus.valueOf(status.toUpperCase());
                    } catch (Exception e) {
                        return false;
                    }
                })
                .filter(bug -> {
                    if (severity == null) return true;
                    try {
                        return bug.getSeverity() == Severity.valueOf(severity.toUpperCase());
                    } catch (Exception e) {
                        return false;
                    }
                })

                .collect(Collectors.toList());
    }


}
