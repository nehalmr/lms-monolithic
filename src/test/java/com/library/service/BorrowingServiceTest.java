package com.library.service;

import com.library.entity.Book;
import com.library.entity.BorrowingTransaction;
import com.library.entity.Member;
import com.library.repository.BorrowingTransactionRepository;
import com.library.testdata.BookTestDataBuilder;
import com.library.testdata.BorrowingTransactionTestDataBuilder;
import com.library.testdata.MemberTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BorrowingService Tests")
class BorrowingServiceTest {

    @Mock
    private BorrowingTransactionRepository borrowingRepository;

    @Mock
    private BookService bookService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BorrowingService borrowingService;

    private Book testBook;
    private Member testMember;
    private BorrowingTransaction testTransaction;

    @BeforeEach
    void setUp() {
        testBook = BookTestDataBuilder.aBook()
                .withId(1L)
                .withTitle("Test Book")
                .withAvailableCopies(5)
                .build();

        testMember = MemberTestDataBuilder.aMember()
                .withId(1L)
                .withName("Test Member")
                .build();

        testTransaction = BorrowingTransactionTestDataBuilder.aTransaction()
                .withId(1L)
                .withBook(testBook)
                .withMember(testMember)
                .build();
    }

    @Nested
    @DisplayName("Get All Transactions")
    class GetAllTransactionsTests {

        @Test
        @DisplayName("Should return all transactions")
        void shouldReturnAllTransactions() {
            // Given
            List<BorrowingTransaction> expectedTransactions = Arrays.asList(testTransaction);
            when(borrowingRepository.findAll()).thenReturn(expectedTransactions);

            // When
            List<BorrowingTransaction> result = borrowingService.getAllTransactions();

            // Then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(testTransaction);
            verify(borrowingRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Borrow Book")
    class BorrowBookTests {

        @Test
        @DisplayName("Should successfully borrow book when conditions are met")
        void shouldSuccessfullyBorrowBookWhenConditionsAreMet() {
            // Given
            Long bookId = 1L;
            Long memberId = 1L;
            
            when(borrowingRepository.countActiveBorrowingsByMember(any(Member.class))).thenReturn(2L);
            when(bookService.isBookAvailable(bookId)).thenReturn(true);
            when(borrowingRepository.save(any(BorrowingTransaction.class))).thenReturn(testTransaction);

            // When
            BorrowingTransaction result = borrowingService.borrowBook(bookId, memberId);

            // Then
            assertThat(result).isNotNull();
            verify(borrowingRepository).countActiveBorrowingsByMember(any(Member.class));
            verify(bookService).isBookAvailable(bookId);
            verify(bookService).decreaseAvailableCopies(bookId);
            verify(borrowingRepository).save(any(BorrowingTransaction.class));
            verify(notificationService).sendBorrowingConfirmation(any(Member.class), any(Book.class));
        }

        @Test
        @DisplayName("Should throw exception when member has reached borrowing limit")
        void shouldThrowExceptionWhenMemberHasReachedBorrowingLimit() {
            // Given
            Long bookId = 1L;
            Long memberId = 1L;
            
            when(borrowingRepository.countActiveBorrowingsByMember(any(Member.class))).thenReturn(5L);

            // When & Then
            assertThatThrownBy(() -> borrowingService.borrowBook(bookId, memberId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Member has reached maximum borrowing limit");

            verify(borrowingRepository).countActiveBorrowingsByMember(any(Member.class));
            verify(bookService, never()).isBookAvailable(any());
            verify(borrowingRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when book is not available")
        void shouldThrowExceptionWhenBookIsNotAvailable() {
            // Given
            Long bookId = 1L;
            Long memberId = 1L;
            
            when(borrowingRepository.countActiveBorrowingsByMember(any(Member.class))).thenReturn(2L);
            when(bookService.isBookAvailable(bookId)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> borrowingService.borrowBook(bookId, memberId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Book is not available for borrowing");

            verify(borrowingRepository).countActiveBorrowingsByMember(any(Member.class));
            verify(bookService).isBookAvailable(bookId);
            verify(borrowingRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Return Book")
    class ReturnBookTests {

        @Test
        @DisplayName("Should successfully return book when transaction exists and is borrowed")
        void shouldSuccessfullyReturnBookWhenTransactionExistsAndIsBorrowed() {
            // Given
            Long transactionId = 1L;
            BorrowingTransaction borrowedTransaction = BorrowingTransactionTestDataBuilder.aTransaction()
                    .withId(transactionId)
                    .withStatus(BorrowingTransaction.TransactionStatus.BORROWED)
                    .build();
            
            BorrowingTransaction returnedTransaction = BorrowingTransactionTestDataBuilder.aTransaction()
                    .withId(transactionId)
                    .returned()
                    .build();

            when(borrowingRepository.findById(transactionId)).thenReturn(Optional.of(borrowedTransaction));
            when(borrowingRepository.save(any(BorrowingTransaction.class))).thenReturn(returnedTransaction);

            // When
            BorrowingTransaction result = borrowingService.returnBook(transactionId);

            // Then
            assertThat(result.getStatus()).isEqualTo(BorrowingTransaction.TransactionStatus.RETURNED);
            assertThat(result.getReturnDate()).isEqualTo(LocalDate.now());
            verify(borrowingRepository).findById(transactionId);
            verify(bookService).increaseAvailableCopies(borrowedTransaction.getBook().getBookId());
            verify(borrowingRepository).save(any(BorrowingTransaction.class));
            verify(notificationService).sendReturnConfirmation(any(Member.class), any(Book.class));
        }

        @Test
        @DisplayName("Should throw exception when transaction not found")
        void shouldThrowExceptionWhenTransactionNotFound() {
            // Given
            Long transactionId = 999L;
            when(borrowingRepository.findById(transactionId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> borrowingService.returnBook(transactionId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Transaction not found");

            verify(borrowingRepository).findById(transactionId);
            verify(borrowingRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when book is already returned")
        void shouldThrowExceptionWhenBookIsAlreadyReturned() {
            // Given
            Long transactionId = 1L;
            BorrowingTransaction returnedTransaction = BorrowingTransactionTestDataBuilder.aTransaction()
                    .withId(transactionId)
                    .returned()
                    .build();

            when(borrowingRepository.findById(transactionId)).thenReturn(Optional.of(returnedTransaction));

            // When & Then
            assertThatThrownBy(() -> borrowingService.returnBook(transactionId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Book is already returned");

            verify(borrowingRepository).findById(transactionId);
            verify(borrowingRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Get Member Borrowings")
    class GetMemberBorrowingsTests {

        @Test
        @DisplayName("Should return member's active borrowings")
        void shouldReturnMembersActiveBorrowings() {
            // Given
            Long memberId = 1L;
            List<BorrowingTransaction> expectedTransactions = Arrays.asList(testTransaction);
            when(borrowingRepository.findByMemberAndStatus(any(Member.class), 
                    eq(BorrowingTransaction.TransactionStatus.BORROWED)))
                    .thenReturn(expectedTransactions);

            // When
            List<BorrowingTransaction> result = borrowingService.getMemberBorrowings(memberId);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(testTransaction);
            verify(borrowingRepository).findByMemberAndStatus(any(Member.class), 
                    eq(BorrowingTransaction.TransactionStatus.BORROWED));
        }
    }

    @Nested
    @DisplayName("Get Overdue Transactions")
    class GetOverdueTransactionsTests {

        @Test
        @DisplayName("Should return overdue transactions")
        void shouldReturnOverdueTransactions() {
            // Given
            BorrowingTransaction overdueTransaction = BorrowingTransactionTestDataBuilder.aTransaction()
                    .overdue()
                    .build();
            List<BorrowingTransaction> expectedTransactions = Arrays.asList(overdueTransaction);
            when(borrowingRepository.findOverdueTransactions(any(LocalDate.class)))
                    .thenReturn(expectedTransactions);

            // When
            List<BorrowingTransaction> result = borrowingService.getOverdueTransactions();

            // Then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(overdueTransaction);
            verify(borrowingRepository).findOverdueTransactions(any(LocalDate.class));
        }
    }
}
