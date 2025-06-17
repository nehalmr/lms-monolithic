package com.library.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Schema(description = "Borrowing transaction entity representing a book borrowing record")
@Entity
@Table(name = "borrowing_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingTransaction {
    @Schema(description = "Unique identifier of the transaction", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    
    @Schema(description = "Book being borrowed")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    
    @Schema(description = "Member borrowing the book")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @Schema(description = "Date when the book was borrowed", example = "2024-11-01")
    @Column(nullable = false)
    private LocalDate borrowDate;
    
    @Schema(description = "Due date for returning the book", example = "2024-11-15")
    private LocalDate dueDate;
    
    @Schema(description = "Date when the book was returned", example = "2024-11-14")
    private LocalDate returnDate;
    
    @Schema(description = "Current status of the transaction", example = "BORROWED", allowableValues = {"BORROWED", "RETURNED", "OVERDUE"})
    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.BORROWED;
    
    public enum TransactionStatus {
        BORROWED, RETURNED, OVERDUE
    }
}
