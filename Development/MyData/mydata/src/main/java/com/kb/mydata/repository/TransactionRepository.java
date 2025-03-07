package com.kb.mydata.repository;

import com.kb.mydata.entity.UserIncomeOutcomeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionRepository extends JpaRepository<UserIncomeOutcomeTransaction, Integer> {
    List<UserIncomeOutcomeTransaction> findByUser_UserId(Long userId);
}