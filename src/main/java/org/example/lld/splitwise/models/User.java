package org.example.lld.splitwise.models;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {

    private Long userId;
    private String name;
    private String email;

}
