package com.robosoft.Twitter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Lob;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileModel {

    private String username;
    private MultipartFile multipartFile;
    private String name;
    private String bio;
    private int followersCount;
    private int followingCount;

}
