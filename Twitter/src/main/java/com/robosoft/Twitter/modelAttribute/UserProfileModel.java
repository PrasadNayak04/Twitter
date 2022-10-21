package com.robosoft.Twitter.modelAttribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileModel {

    private String username;
    private MultipartFile icon;
    private String name;
    private String bio;

}
