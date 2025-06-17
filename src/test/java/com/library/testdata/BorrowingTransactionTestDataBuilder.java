package com.library.testdata;

import com.library.entity.Book;
import com.library.entity.BorrowingTransaction;
import com.library.entity.Member;
import java.time.LocalDate;

public class BorrowingTransactionTestDataBuilder {
    private Long transactionId = 1L;
    private Book book = BookTestDataBuilder.aBook().build();
    private Member member = MemberTestDataBuilder.aMember().build();
    private LocalDate borrowDate = LocalDate.of(2024, 1, 15);
    private LocalDate dueDate = LocalDate.of(2024, 1, 29);
    private LocalDate returnDate = null;
    private BorrowingTransaction.TransactionStatus status = BorrowingTransaction.TransactionStatus.BORROWED;
    
    public static BorrowingTransactionTestDataBuilder aTransaction() {
        return new BorrowingTransactionTestDataBuilder();
    }
    
    public BorrowingTransactionTestDataBuilder withId(Long transactionId) {
        this.transactionId = transactionId;
        return this;
    }
    
    public BorrowingTransactionTestDataBuilder withBook(Book book) {
        this.book = book;
        return this;
    }
    
    public BorrowingTransactionTestDataBuilder withMember(Member member) {
        this.member = member;
        return this;
    }
    
    public BorrowingTransactionTestDataBuilder withBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
        return this;
    }
    
    public BorrowingTransactionTestDataBuilder withDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }
    
    public BorrowingTransactionTestDataBuilder withReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        return this;
    }
    
    public BorrowingTransactionTestDataBuilder withStatus(BorrowingTransaction.TransactionStatus status) {
        this.status = status;
        return this;
    }
    
    public BorrowingTransactionTestDataBuilder returned() {
        this.status = BorrowingTransaction.TransactionStatus.RETURNED;
        this.returnDate = LocalDate.now();
        return this;
    }
    
    public BorrowingTransactionTestDataBuilder overdue() {
        this.status = BorrowingTransaction.TransactionStatus.OVERDUE;
        this.dueDate = LocalDate.now().minusDays(5);
        return this;
    }
    
    public BorrowingTransaction build() {
        return new BorrowingTransaction(transactionId, book, member, borrowDate, dueDate, returnDate, status);
    }
}
