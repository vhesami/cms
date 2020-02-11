package com.example.cms.repositories;

import com.example.cms.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT MAX(t.initialDateTime) FROM Transaction t WHERE t.initialDateTime >= :dateTime")
    Date lastTransactionTime(@Param("dateTime") Date dateTime);
}
