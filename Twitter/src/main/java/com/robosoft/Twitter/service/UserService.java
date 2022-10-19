package com.robosoft.Twitter.service;

import com.robosoft.Twitter.entity.User;
import com.robosoft.Twitter.entity.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String query;

    public String signUp(User user) {
        query = "insert into Users values(?,?,?)";
        jdbcTemplate.update(query,user.getUsername(), user.getName(), user.getPassword());
        return "Welcome to Twitter. Please login at and complete your profile at http://localhost:8080/profileCompletion.";
    }

    public String completeProfile(UserProfile userProfile){
        query = "insert into UsersProfile (UserName, Name, Icon, Bio) values (?,?,?,?)";
        jdbcTemplate.update(query, userProfile.getUsername(), userProfile.getName(), userProfile.getIcon(), userProfile.getBio());
        return "Profile completion successful.";
    }
}
