package com.robosoft.Twitter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserProfile {

    @Id
    private String username;
    private String name;
    @Lob
    private byte[] icon;
    private String bio;
    private int followersCount;
    private int followingCount;

}
