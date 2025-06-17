package com.library.service;

import com.library.entity.Book;
import com.library.entity.Member;
import com.library.entity.Notification;
import com.library.repository.NotificationRepository;
import com.library.testdata.BookTestDataBuilder;
import com.library.testdata.MemberTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationService Tests")
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Book testBook;
    private Member testMember;

    @BeforeEach
    void setUp() {
        testBook = BookTestDataBuilder.aBook()
                .withTitle("Test Book")
                .build();

        testMember = MemberTestDataBuilder.aMember()
                .withName("Test Member")
                .build();
    }

    @Nested
    @DisplayName("Send Borrowing Confirmation")
    class SendBorrowingConfirmationTests {

        @Test
        @DisplayName("Should send borrowing confirmation notification")
        void shouldSendBorrowingConfirmationNotification() {
            // Given
            ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);

            // When
            notificationService.sendBorrowingConfirmation(testMember, testBook);

            // Then
            verify(notificationRepository).save(notificationCaptor.capture());
            Notification savedNotification = notificationCaptor.getValue();
            
            assertThat(savedNotification.getMember()).isEqualTo(testMember);
            assertThat(savedNotification.getMessage()).contains("Test Book");
            assertThat(savedNotification.getMessage()).contains("successfully borrowed");
            assertThat(savedNotification.getType()).isEqualTo(Notification.NotificationType.GENERAL);
        }
    }

    @Nested
    @DisplayName("Send Return Confirmation")
    class SendReturnConfirmationTests {

        @Test
        @DisplayName("Should send return confirmation notification")
        void shouldSendReturnConfirmationNotification() {
            // Given
            ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);

            // When
            notificationService.sendReturnConfirmation(testMember, testBook);

            // Then
            verify(notificationRepository).save(notificationCaptor.capture());
            Notification savedNotification = notificationCaptor.getValue();
            
            assertThat(savedNotification.getMember()).isEqualTo(testMember);
            assertThat(savedNotification.getMessage()).contains("Test Book");
            assertThat(savedNotification.getMessage()).contains("successfully returned");
            assertThat(savedNotification.getType()).isEqualTo(Notification.NotificationType.GENERAL);
        }
    }

    @Nested
    @DisplayName("Send Overdue Notice")
    class SendOverdueNoticeTests {

        @Test
        @DisplayName("Should send overdue notice notification")
        void shouldSendOverdueNoticeNotification() {
            // Given
            int daysOverdue = 5;
            ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);

            // When
            notificationService.sendOverdueNotice(testMember, testBook, daysOverdue);

            // Then
            verify(notificationRepository).save(notificationCaptor.capture());
            Notification savedNotification = notificationCaptor.getValue();
            
            assertThat(savedNotification.getMember()).isEqualTo(testMember);
            assertThat(savedNotification.getMessage()).contains("Test Book");
            assertThat(savedNotification.getMessage()).contains("5 days overdue");
            assertThat(savedNotification.getType()).isEqualTo(Notification.NotificationType.OVERDUE_NOTICE);
        }
    }

    @Nested
    @DisplayName("Get Member Notifications")
    class GetMemberNotificationsTests {

        @Test
        @DisplayName("Should return member notifications")
        void shouldReturnMemberNotifications() {
            // Given
            Long memberId = 1L;
            List<Notification> expectedNotifications = Arrays.asList(new Notification());
            when(notificationRepository.findByMemberOrderByDateSentDesc(any(Member.class)))
                    .thenReturn(expectedNotifications);

            // When
            List<Notification> result = notificationService.getMemberNotifications(memberId);

            // Then
            assertThat(result).isEqualTo(expectedNotifications);
            verify(notificationRepository).findByMemberOrderByDateSentDesc(any(Member.class));
        }
    }
}
