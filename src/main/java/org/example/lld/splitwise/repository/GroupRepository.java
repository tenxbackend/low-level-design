package org.example.lld.splitwise.repository;

import org.example.lld.splitwise.models.Group;

import java.util.concurrent.ConcurrentHashMap;

public class GroupRepository {
    
    public ConcurrentHashMap<Long, Group> groups;
    
    public GroupRepository() {
        this.groups = new ConcurrentHashMap<>();
    }
}
