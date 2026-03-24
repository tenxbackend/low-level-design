package org.example.lld.splitwise.repository;

import org.example.lld.splitwise.models.Balance;
import org.example.lld.splitwise.models.User;

import java.util.concurrent.ConcurrentHashMap;

public class BalanceRepository {
    
    private final ConcurrentHashMap<User, Balance> balances;
    
    public BalanceRepository() {
        this.balances = new ConcurrentHashMap<>();
    }
}
