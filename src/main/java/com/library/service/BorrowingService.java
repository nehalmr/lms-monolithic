package com.library.service;

import com.library.entity.BorrowingTransaction;
import com.library.entity.Book;
import com.library.entity.Member;
import com.library.repository.BorrowingTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowingService {
    
    @Autowired
    private BorrowingTransactionRepository borrowingRepository;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private NotificationService notificationService;
    
    private static final int MAX_BORROWING_LIMIT = 5;
    private static final int BORROWING_PERIOD_DAYS = 14;
    
    public List<BorrowingTransaction> getAllTransactions() {
        return borrowingRepository.findAll();
    }
    
    public Optional<BorrowingTransaction> getTransactionById(Long id) {
        return borrowingRepository.findById(id);
    }
    
    @Transactional
    public BorrowingTransaction borrowBook(Long bookId, Long memberId) {
        // Check if member has reached borrowing limit
        Member member = new Member();
        member.setMemberId(memberId);
        long activeBorrowings = borrowingRepository.countActiveBorrowingsByMember(member);
        
        if (activeBorrowings >= MAX_BORROWING_LIMIT) {
            throw new RuntimeException("Member has reached maximum borrowing limit");
        }
        
        // Check if book is available
        if (!bookService.isBookAvailable(bookId)) {
            throw new RuntimeException("Book is not available for borrowing");
        }
        
        // Create borrowing transaction
        BorrowingTransaction transaction = new BorrowingTransaction();
        Book book = new Book();
        book.setBookId(bookId);
        transaction.setBook(book);
        transaction.setMember(member);
        transaction.setBorrowDate(LocalDate.now());
        transaction.setDueDate(LocalDate.now().plusDays(BORROWING_PERIOD_DAYS));
        transaction.setStatus(BorrowingTransaction.TransactionStatus.BORROWED);
        
        // Decrease available copies
        bookService.decreaseAvailableCopies(bookId);
        
        // Save transaction
        BorrowingTransaction savedTransaction = borrowingRepository.save(transaction);
        
        // Send notification
        notificationService.sendBorrowingConfirmation(member, book);
        
        return savedTransaction;
    }
    
    @Transactional
    public BorrowingTransaction returnBook(Long transactionId) {
        Optional<BorrowingTransaction> transactionOpt = borrowingRepository.findById(transactionId);
        
        if (transactionOpt.isEmpty()) {
            throw new RuntimeException("Transaction not found");
        }
        
        BorrowingTransaction transaction = transactionOpt.get();
        
        if (transaction.getStatus() != BorrowingTransaction.TransactionStatus.BORROWED) {
            throw new RuntimeException("Book is already returned");
        }
        
        // Update transaction
        transaction.setReturnDate(LocalDate.now());
        transaction.setStatus(BorrowingTransaction.TransactionStatus.RETURNED);
        
        // Increase available copies
        bookService.increaseAvailableCopies(transaction.getBook().getBookId());
        
        // Save transaction
        BorrowingTransaction savedTransaction = borrowingRepository.save(transaction);
        
        // Send notification
        notificationService.sendReturnConfirmation(transaction.getMember(), transaction.getBook());
        
        return savedTransaction;
    }
    
    public List<BorrowingTransaction> getMemberBorrowings(Long memberId) {
        Member member = new Member();
        member.setMemberId(memberId);
        return borrowingRepository.findByMemberAndStatus(member, BorrowingTransaction.TransactionStatus.BORROWED);
    }
    
    public List<BorrowingTransaction> getOverdueTransactions() {
        return borrowingRepository.findOverdueTransactions(LocalDate.now());
    }
}
