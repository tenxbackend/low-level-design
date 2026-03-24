package org.example.lld.splitwise.service;

import org.example.lld.splitwise.models.Group;
import org.example.lld.splitwise.models.User;
import org.example.lld.splitwise.repository.GroupRepository;

import java.util.Random;

public class GroupService {

    private GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group createGroup(String groupName) {
        Group group = new Group();
        group.setGroupId(new Random().nextLong());
        group.setGroupName(groupName);
        groupRepository.groups.put(group.getGroupId(), group);
        return group;
    }

    public void addUserToGroup(Long groupId, User user) {
        Group group = this.groupRepository.groups.get(groupId);
        if (group == null) {
            throw new RuntimeException("Group not found");
        }
        group.addUser(user);
        System.out.println("User " + user.getName() + "added to group");
    }
}
