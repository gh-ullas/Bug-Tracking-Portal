package com.BugTrackingPortal.bugbuster.model;

import com.BugTrackingPortal.bugbuster.model.enums.BugStatus;
import com.BugTrackingPortal.bugbuster.model.enums.Severity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 3000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Column(name = "file_path")
    private String filePath;


    @Enumerated(EnumType.STRING)
    private BugStatus status = BugStatus.OPEN;

    @Column(name = "reproduction_steps", length = 3000)
    private String stepsToReproduce;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "reported_by")
    private User reportedBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ElementCollection
    @CollectionTable(name = "bug_comments", joinColumns = @JoinColumn(name = "bug_id"))
    private List<Comment> comments = new ArrayList<>();

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

}
