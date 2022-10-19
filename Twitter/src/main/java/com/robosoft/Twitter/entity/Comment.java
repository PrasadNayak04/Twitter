package com.robosoft.Twitter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private int CommentId;
    private int tweetId;
    private String hashtag;
    private String userName;
    private String name;
    @Lob
    private byte[] icon;
    private int likes;

}
