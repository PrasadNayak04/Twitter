package com.robosoft.Twitter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentLike {

    private int commentId;
    private String userName;
    private String name;
    @Lob
    private byte[] icon;

}
