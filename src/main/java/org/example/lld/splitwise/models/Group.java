package org.example.lld.splitwise.models;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class Group {
    private Long groupId;
    private String groupName;
    private List<User> users;

    public Group(){
        this.users = new ArrayList<>();
    }

    public void addUser(User user){
        this.users.add(user);
    }
}
