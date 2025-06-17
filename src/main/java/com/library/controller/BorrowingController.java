package com.library.controller;

import com.library.entity.BorrowingTransaction;
import com.library.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/borrowing")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Borrowing", description = "Book borrowing and return management APIs")
public class BorrowingController {
    
    @Autowired
    private BorrowingService borrowingService;
    
    @Operation(
        summary = "Retrieve all borrowing transactions",
        description = "Get a list of all borrowing transactions in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all transactions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<BorrowingTransaction> getAllTransactions() {
        return borrowingService.getAllTransactions();
    }
    
    @Operation(
        summary = "Borrow a book",
        description = "Create a new borrowing transaction for a member to borrow a book"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book borrowed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request - book not available or member limit exceeded"),
        @ApiResponse(responseCode = "404", description = "Book or member not found")
    })
    @PostMapping("/borrow")
    public ResponseEntity<BorrowingTransaction> borrowBook(
        @Parameter(description = "ID of the book to borrow", required = true, example = "1")
        @RequestParam Long bookId,
        @Parameter(description = "ID of the member borrowing the book", required = true, example = "1")
        @RequestParam Long memberId) {
        try {
            BorrowingTransaction transaction = borrowingService.borrowBook(bookId, memberId);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(
        summary = "Return a borrowed book",
        description = "Process the return of a borrowed book by transaction ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book returned successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request - book already returned"),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @PostMapping("/return/{transactionId}")
    public ResponseEntity<BorrowingTransaction> returnBook(
        @Parameter(description = "ID of the borrowing transaction", required = true, example = "1")
        @PathVariable Long transactionId) {
        try {
            BorrowingTransaction transaction = borrowingService.returnBook(transactionId);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(
        summary = "Get member's borrowing history",
        description = "Retrieve all active borrowing transactions for a specific member"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved member borrowings"),
        @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @GetMapping("/member/{memberId}")
    public List<BorrowingTransaction> getMemberBorrowings(
        @Parameter(description = "ID of the member", required = true, example = "1")
        @PathVariable Long memberId) {
        return borrowingService.getMemberBorrowings(memberId);
    }
    
    @Operation(
        summary = "Get overdue transactions",
        description = "Retrieve all borrowing transactions that are past their due date"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved overdue transactions")
    })
    @GetMapping("/overdue")
    public List<BorrowingTransaction> getOverdueTransactions() {
        return borrowingService.getOverdueTransactions();
    }
}
