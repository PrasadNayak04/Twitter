package com.robosoft.Twitter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Lob;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetModel implements Serializable {

    private String hashtag;
    private String description;
    private MultipartFile photo;

}
