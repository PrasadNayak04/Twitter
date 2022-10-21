package com.robosoft.Twitter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private byte[] photo;
    private int likes;

}
