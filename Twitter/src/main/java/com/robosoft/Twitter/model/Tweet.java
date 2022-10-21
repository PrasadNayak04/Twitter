package com.robosoft.Twitter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tweet {

    private int tweetId;
    private String username;
    private String name;
    private String userIconUrl;
    private String hashtag;
    private String description;
    private String tweetPhotoUrl;
    private Timestamp tweetDate;
    private int likes;

}
