package com.min.hbase.example;

import lombok.Data;

@Data
public class User {
    private String id;
    private String username;
    private String password;
    private String gender;
    private String age;
    private String phone;
    private String email;
}
