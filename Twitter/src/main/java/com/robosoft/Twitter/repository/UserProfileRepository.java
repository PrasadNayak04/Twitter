package com.robosoft.Twitter.repository;

import com.robosoft.Twitter.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    /*byte[] findIconByUsername(String username);
    String findNameByUsername(String username);*/

}
