package com.BugTrackingPortal.bugbuster.service;

import com.BugTrackingPortal.bugbuster.dto.BugRequest;
import com.BugTrackingPortal.bugbuster.dto.BugResponse;
import com.BugTrackingPortal.bugbuster.model.*;
import com.BugTrackingPortal.bugbuster.model.enums.BugStatus;
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
                bug.getCreatedAt()
        );
    }
}
