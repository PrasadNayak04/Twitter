package com.robosoft.Twitter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentLike {

    private int commentId;
    private String username;
    private String name;
    @Lob
    private byte[] icon;

}
