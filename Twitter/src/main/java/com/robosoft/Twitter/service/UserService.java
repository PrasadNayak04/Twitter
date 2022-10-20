package com.robosoft.Twitter.service;

import com.robosoft.Twitter.entity.User;
import com.robosoft.Twitter.model.CommentModel;
import com.robosoft.Twitter.model.TweetModel;
import com.robosoft.Twitter.model.UserProfileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

@Service
public class UserService extends UserProfileService{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String query;

    private int authorizedId = (int)(Math.random() * 5000);

    private String loggedInUsername;

    public String signUp(User user) {
        query = "insert into Users values(?,?,?)";
        jdbcTemplate.update(query,user.getUsername(), user.getName(), user.getPassword());
        return "Welcome to Twitter. Please login at and complete your profile at http://localhost:8080/profileCompletion.";
    }

    public String completeProfile(UserProfileModel userProfile) throws IOException {
        query = "insert into UsersProfile (UserName, Name, Icon, Bio) values (?,?,?,?)";
        jdbcTemplate.update(query, userProfile.getUsername(), userProfile.getName(), userProfile.getIcon().getBytes(), userProfile.getBio());
        return "Profile completion successful.";
    }

    public String signIn(String username, String givenPassword){
        String originalPassword = getPasswordByUsername(username);
        if(givenPassword.equals(originalPassword)){
            loggedInUsername = username;
            authorizedId = (int)(Math.random() * 5000);
            return "Login successful. Please use authorization Id " + authorizedId + " for using twitter services.";
        }
        return "Login unsuccessful.";
    }

    public boolean authorized(int authorizationId){
        if(authorizationId == authorizedId){
            return true;
        }
        return false;
    }

    public String getPasswordByUsername(String username){
        query = "select Password from users where Username = '" + username + "'";
        return jdbcTemplate.queryForObject(query, String.class);
    }

    public String tweet(TweetModel tweet) throws IOException {
        /*byte[] icon = getIcon(tweet.getUsername());
        String name = getNameByUsername(tweet.getUsername());*/
        query = "insert into Tweets (UserName, Hashtag, Description, photo, tweetDate) values (?,?,?,?,?,?,?)";
        jdbcTemplate.update(query, loggedInUsername, tweet.getHashtag(), tweet.getDescription(), tweet.getPhoto().getBytes(), Timestamp.from(Instant.now()));
        return "Tweet successfully posted.";
    }

    public String addFollower(String followerUsername){
        query = "insert into Follows values (?,?)";
        jdbcTemplate.update(query, loggedInUsername, followerUsername);
        return "New follower added";
    }

    public String replyTweet(int tweetId, TweetModel tweet, int authorizationId) throws IOException {
        if(authorized(authorizationId)) {
            query = "insert into Comments (TweetId, Username, HashTag, Description, Photo) values (?,?,?,?,?)";
            jdbcTemplate.update(query, tweetId, loggedInUsername, tweet.getHashtag(), tweet.getDescription(), tweet.getPhoto().getBytes());
            return "Comment posted successfully.";
        }
        return "Unauthorized user";
    }

    public String likeTweet(int tweetId, int authorizationId){
        if(authorized(authorizationId)) {
            query = "insert into TweetsLike values (?,?)";
            jdbcTemplate.update(query, tweetId, loggedInUsername);
            query = "update Tweets set likes = likes + 1 where tweetId = " + tweetId;
            jdbcTemplate.update(query);
            return "Like posted to the tweet with id " + tweetId;
        }
        return "Unauthorized user";
    }

    public String likeRetweet(int retweetId, int authorizationId){
        if(authorized(authorizationId)) {
            query = "insert into CommentsLike values (?,?)";
            jdbcTemplate.update(query, retweetId, loggedInUsername);
            query = "update Comments set likes = likes + 1 where commentId = " + retweetId;
            jdbcTemplate.update(query);
            return "Like posted to the comment with id " + retweetId;
        }
        return "Unauthorized user";
    }

}
