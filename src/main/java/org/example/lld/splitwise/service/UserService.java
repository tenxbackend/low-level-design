package org.example.lld.splitwise.service;

import org.example.lld.splitwise.models.User;
import org.example.lld.splitwise.repository.UserRepository;

import java.util.Random;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(String name, String email){
        User user = new User();
        user.setUserId(new Random().nextLong());
        user.setName(name);
        user.setEmail(email);
        userRepository.users.put(user.getUserId(), user);
        return user;
    }
}
