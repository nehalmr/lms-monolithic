package com.library.repository;

import com.library.entity.BorrowingTransaction;
import com.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowingTransactionRepository extends JpaRepository<BorrowingTransaction, Long> {
    List<BorrowingTransaction> findByMemberAndStatus(Member member, BorrowingTransaction.TransactionStatus status);
    List<BorrowingTransaction> findByStatus(BorrowingTransaction.TransactionStatus status);
    
    @Query("SELECT bt FROM BorrowingTransaction bt WHERE bt.dueDate < :currentDate AND bt.status = 'BORROWED'")
    List<BorrowingTransaction> findOverdueTransactions(LocalDate currentDate);
    
    @Query("SELECT COUNT(bt) FROM BorrowingTransaction bt WHERE bt.member = :member AND bt.status = 'BORROWED'")
    long countActiveBorrowingsByMember(Member member);
}
