package com.example.cms.repositories;

import com.example.cms.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT c FROM Card c WHERE c.remainAmount >= :minAmount ORDER BY c.remainAmount DESC")
    List<Card> findAllActiveCards(@Param("minAmount") Long minAmount);

    @Query("SELECT SUM(c.remainAmount) FROM Card c WHERE c.remainAmount >= :minAmount")
    Long accountBalance(@Param("minAmount") Long minAmount);

    @Modifying
    @Transactional
    @Query("UPDATE Card c SET c.remainAmount = :remainAmount")
    void resetRemainAmounts(@Param("remainAmount") Long remainAmount);
}
