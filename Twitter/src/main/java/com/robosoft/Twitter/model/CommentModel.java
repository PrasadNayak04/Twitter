package com.robosoft.Twitter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Lob;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentModel {

    private int tweetId;
    private String hashtag;
    private String description;
    private MultipartFile photo;

}
