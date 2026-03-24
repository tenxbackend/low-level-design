package org.example.lld.splitwise.service;

import org.example.lld.splitwise.models.*;
import org.example.lld.splitwise.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class ExpenseService {

    private ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public void addExpense(BigDecimal amount,
                           User paidBy,
                           ExpenseType expenseType,
                           String description,
                           Group group,
                           SplitStrategyType splitStrategyType,
                           List<SplitInput> splitInputList
    ) {

        Expense expense = Expense.builder()
                .expenseId(new Random().nextLong())
                .paidBy(paidBy)
                .paidAt(LocalDateTime.now())
                .amount(amount)
                .group(group)
                .build();

        // splits -> compute splits and then save

        List<Split> splits = SplitStrategyFactory.getSplitStrategyInstance(splitStrategyType).computeSplits(amount, splitInputList);
        expense.setSplits(splits);
        expenseRepository.expenses.put(expense.getExpenseId(), expense);
        System.out.println("Expense created " + expense);
    }
}
