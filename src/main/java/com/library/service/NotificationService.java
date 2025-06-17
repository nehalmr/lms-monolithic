package com.library.service;

import com.library.entity.Book;
import com.library.entity.Member;
import com.library.entity.Notification;
import com.library.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    public void sendBorrowingConfirmation(Member member, Book book) {
        Notification notification = new Notification();
        notification.setMember(member);
        notification.setMessage(String.format("You have successfully borrowed '%s'. Due date: %s", 
            book.getTitle(), java.time.LocalDate.now().plusDays(14)));
        notification.setType(Notification.NotificationType.GENERAL);
        notificationRepository.save(notification);
    }
    
    public void sendReturnConfirmation(Member member, Book book) {
        Notification notification = new Notification();
        notification.setMember(member);
        notification.setMessage(String.format("You have successfully returned '%s'. Thank you!", book.getTitle()));
        notification.setType(Notification.NotificationType.GENERAL);
        notificationRepository.save(notification);
    }
    
    public void sendOverdueNotice(Member member, Book book, int daysOverdue) {
        Notification notification = new Notification();
        notification.setMember(member);
        notification.setMessage(String.format("Your book '%s' is %d days overdue. Please return it immediately to avoid additional fines.", 
            book.getTitle(), daysOverdue));
        notification.setType(Notification.NotificationType.OVERDUE_NOTICE);
        notificationRepository.save(notification);
    }
    
    public List<Notification> getMemberNotifications(Long memberId) {
        Member member = new Member();
        member.setMemberId(memberId);
        return notificationRepository.findByMemberOrderByDateSentDesc(member);
    }
}
