package com.robosoft.Twitter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    private String username;
    @Lob
    private byte[] dp;
    private String name;
    private String bio;
    private int followersCount;
    private int followingCount;

}
