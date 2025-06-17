package com.library.repository;

import com.library.entity.Notification;
import com.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByMemberOrderByDateSentDesc(Member member);
    List<Notification> findByMemberAndIsReadOrderByDateSentDesc(Member member, boolean isRead);
    long countByMemberAndIsRead(Member member, boolean isRead);
}
