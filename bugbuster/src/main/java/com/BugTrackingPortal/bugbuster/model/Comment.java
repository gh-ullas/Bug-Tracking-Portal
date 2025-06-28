package com.BugTrackingPortal.bugbuster.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Comment {
    private String id;
    private String author;
    private String text;
    private LocalDateTime createdAt;

    public Comment(String author, String text) {
        this.id = UUID.randomUUID().toString();
        this.author = author;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }
}
