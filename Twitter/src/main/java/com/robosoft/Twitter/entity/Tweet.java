package com.robosoft.Twitter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tweet {

    private String username;
    private String name;
    private byte[] icon;
    private String hashtag;
    private String description;
    @Lob
    private byte[] photo;
    private Timestamp tweetDate;
    private int likes;

}
