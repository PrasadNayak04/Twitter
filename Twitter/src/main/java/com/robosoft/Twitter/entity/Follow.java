package com.robosoft.Twitter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

    private String followedBy;
    private String followed;
    private String followedName;
    @Lob
    private byte[] followedDp;

}
