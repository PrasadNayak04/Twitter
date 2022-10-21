package com.robosoft.Twitter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    private String username;
    private String name;
    private String iconUrl;
    private String bio;
    private int followersCount;
    private int followingCount;
    private boolean verified;

}
