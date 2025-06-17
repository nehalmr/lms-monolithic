package com.library.repository;

import com.library.entity.Fine;
import com.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByMemberAndStatus(Member member, Fine.FineStatus status);
    List<Fine> findByStatus(Fine.FineStatus status);
    
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Fine f WHERE f.member = :member AND f.status = 'PENDING'")
    BigDecimal getTotalPendingFinesByMember(Member member);
}
