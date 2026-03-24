package org.example.lld.splitwise.repository;

import org.example.lld.splitwise.models.User;

import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    
    public ConcurrentHashMap<Long, User> users;
    
    public UserRepository() {
        this.users = new ConcurrentHashMap<>();
    }
}
