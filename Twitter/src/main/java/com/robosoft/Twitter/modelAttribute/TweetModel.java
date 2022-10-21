package com.robosoft.Twitter.modelAttribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetModel implements Serializable {

    private String hashtag;
    private String description;
    private MultipartFile photo;

}
