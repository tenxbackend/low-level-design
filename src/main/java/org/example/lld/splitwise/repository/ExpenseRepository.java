package org.example.lld.splitwise.repository;

import org.example.lld.splitwise.models.Expense;

import java.util.concurrent.ConcurrentHashMap;

public class ExpenseRepository {
    
    public ConcurrentHashMap<Long, Expense> expenses;
    
    public ExpenseRepository() {
        this.expenses = new ConcurrentHashMap<>();
    }
}
