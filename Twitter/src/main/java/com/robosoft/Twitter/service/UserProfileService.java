package com.robosoft.Twitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String query;

    public byte[] getIcon(String username) {
        query = "select icon from usersProfile where username = '" + username + "'";
        return jdbcTemplate.queryForObject(query, byte[].class);
    }

    public String getNameByUsername(String username) {
        query = "select Name from usersProfile where username = '" + username +"'";
        return jdbcTemplate.queryForObject(query, String.class);
    }

}