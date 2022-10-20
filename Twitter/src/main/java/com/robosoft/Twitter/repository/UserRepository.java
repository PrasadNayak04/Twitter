package com.robosoft.Twitter.repository;

import com.robosoft.Twitter.entity.User;
import com.robosoft.Twitter.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
   /* String getPasswordByUsername(String username);*/
}
