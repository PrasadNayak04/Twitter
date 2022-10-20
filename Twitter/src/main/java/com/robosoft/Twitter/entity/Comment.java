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
    private String username;
    private String name;
    private String hashtag;
    private String comments;
    @Lob
    private byte[] photo;
    private int likes;

}
