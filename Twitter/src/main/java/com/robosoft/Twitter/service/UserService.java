package com.robosoft.Twitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public String signup(String user, MultipartFile file) {
        jdbcTemplate.update("insert into user(username,dp) values(?,?)",user,file.getOriginalFilename());
        return "Sign up Completed Welcome to Twitter";
    }

    public byte[] getuser(String username) {
        return new byte[0];
    }
}
