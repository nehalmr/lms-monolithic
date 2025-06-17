package com.library.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @Column(nullable = false, length = 1000)
    private String message;
    
    @Column(nullable = false)
    private LocalDateTime dateSent = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    
    private boolean isRead = false;
    
    public enum NotificationType {
        DUE_DATE_REMINDER, OVERDUE_NOTICE, FINE_NOTICE, GENERAL
    }
}
