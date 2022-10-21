package com.robosoft.Twitter.service;

import com.robosoft.Twitter.model.Follow;
import com.robosoft.Twitter.model.Tweet;
import com.robosoft.Twitter.model.User;
import com.robosoft.Twitter.model.UserProfile;
import com.robosoft.Twitter.modelAttribute.TweetModel;
import com.robosoft.Twitter.modelAttribute.UserProfileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserServices{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String query;

    private int authorizedId = (int)(Math.random() * 5000);

    private String loggedInUsername;
    @Override
    public String signUp(User user) {
        query = "insert into Users values(?,?,?)";
        jdbcTemplate.update(query,user.getUsername(), user.getName(), user.getPassword());
        return "Welcome to Twitter. Please login at http://localhost:8080/user/login and complete your profile at http://localhost:8080/user/profileCompletion/{authorizationId}.";
    }

    @Override
    public String completeProfile(UserProfileModel userProfile, int authorizationId){
        if(authorized(authorizationId)) {
            query = "insert into UsersProfile (UserName, Name, Bio) values (?,?,?)";
            jdbcTemplate.update(query, userProfile.getUsername(), userProfile.getName(), userProfile.getBio());
            try {
                updateProfileDetails(userProfile.getIcon());
                return "Profile completion successful.";
            }catch (Exception e){
                return e.getLocalizedMessage();
            }
        }
        return "Unauthorized user.";
    }

    @Override
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

    @Override
    public String tweet(TweetModel tweet, int authorizationId) throws IOException {
        if(authorized(authorizationId)) {
            query = "insert into Tweets (UserName, Hashtag, Description, tweetDate) values (?,?,?,?)";
            jdbcTemplate.update(query, loggedInUsername, tweet.getHashtag(), tweet.getDescription(), Timestamp.from(Instant.now()));
            int tweetId = jdbcTemplate.queryForObject("select max(TweetId) from Tweets", Integer.class);
            updateTweetPhotoDetails(tweetId, tweet.getPhoto());
            return "Tweet successfully posted.";
        }
        return "Unauthorized user.";
    }

    @Override
    public String deleteTweet(int tweetId, int authorizationId){
        if(authorized(authorizationId)) {
            if(tweetBelongsToUser(tweetId, loggedInUsername)) {
                query = "delete from Tweets where tweetId = ? and Username = ?";
                jdbcTemplate.update(query, tweetId, loggedInUsername);
                return "Tweet successfully deleted.";
            }
            return "Sorry you cannot delete other users tweet";
        }
        return "Unauthorized user.";
    }

    @Override
    public String addFollower(String followerUsername, int authorizationId){
        if(authorized(authorizationId)){
            query = "insert into Follows values (?,?)";
            jdbcTemplate.update(query, loggedInUsername, followerUsername);
            query = "update UsersProfile set followingCount = " + getFollowingsCount(loggedInUsername) + " where Username = '" + loggedInUsername + "'";
            jdbcTemplate.update(query);
            query = "update UsersProfile set followersCount = " + getFollowersCount(followerUsername) + " where Username = '" + followerUsername + "'";
            jdbcTemplate.update(query);
            return "New follower added";
        }
        return "Unauthorized user.";
    }

    @Override
    public String removeFollower(String followerUsername, int authorizationId){
        if(authorized(authorizationId)) {
            if (isFollowing(loggedInUsername, followerUsername)) {
                query = "delete from Follows where followedBy = ? and followed = ?";
                jdbcTemplate.update(query, loggedInUsername, followerUsername);
                query = "update UsersProfile set followingCount = " + getFollowingsCount(loggedInUsername) + " where Username = '" + loggedInUsername + "'";
                jdbcTemplate.update(query);
                query = "update UsersProfile set followersCount = " + getFollowersCount(followerUsername) + " where Username = '" + followerUsername + "'";
                jdbcTemplate.update(query);
                return "You unfollowed " + followerUsername;
            }
            return "You are not following this user";
        }
        return "Unauthorized user.";
    }

    @Override
    public String replyTweet(int tweetId, TweetModel tweet, int authorizationId) throws IOException {
        if(authorized(authorizationId)) {
            query = "insert into ReplyTweets (TweetId, Username, HashTag, Description) values (?,?,?,?)";
            jdbcTemplate.update(query, tweetId, loggedInUsername, tweet.getHashtag(), tweet.getDescription());
            int replyTweetId = jdbcTemplate.queryForObject("select max(ReplyTweetId) from ReplyTweets", Integer.class);
            updateReplyTweetPhotoDetails(replyTweetId, tweet.getPhoto());
            return "Reply tweet posted successfully.";
        }
        return "Unauthorized user";
    }

    @Override
    public String deleteTweetReply(int replyTweetId, int authorizationId){
        if(authorized(authorizationId)) {
            if(tweetReplyBelongsToUser(replyTweetId, loggedInUsername)) {
                query = "delete from ReplyTweets where ReplyTweetId = ? and Username = ?";
                jdbcTemplate.update(query, replyTweetId, loggedInUsername);
                return "Tweet reply deleted.";
            }
            return "Sorry you cannot delete other users reply";
        }
        return "Unauthorized user.";
    }

    @Override
    public String likeTweet(int tweetId, int authorizationId){
        if(authorized(authorizationId)) {
            query = "insert into TweetLikes values (?,?)";
            try {
                jdbcTemplate.update(query, tweetId, loggedInUsername);
            }catch(Exception e){
                return "Yo have already liked this tweet";
            }
            query = "update Tweets set likes = ? where tweetId = ?";
            jdbcTemplate.update(query, getTweetLikes(tweetId), tweetId);
            return "Like posted to the tweet with id " + tweetId;
        }
        return "Unauthorized user";
    }

    @Override
    public String removeTweetLike(int tweetId, int authorizationId){
        if(authorized(authorizationId)) {
            query = "delete from TweetLikes where tweetId = ? and username = ?";
            jdbcTemplate.update(query, tweetId, loggedInUsername);
            query = "Update Tweets set likes = ? where tweetId = ?";
            jdbcTemplate.update(query, getTweetLikes(tweetId), tweetId);
            return "Ok";
        }
        return "Unauthorized user";
    }

    @Override
    public String likeRetweet(int retweetId, int authorizationId){
        if(authorized(authorizationId)) {
            query = "insert into ReplyTweetLikes values (?,?)";
            try {
                jdbcTemplate.update(query, retweetId, loggedInUsername);
            }catch (Exception e){
                return "You already posted like.";
            }
            query = "update ReplyTweets set likes = ? where replyTweetId = ?";
            jdbcTemplate.update(query, getReplyTweetLikes(retweetId), retweetId);
            return "Like posted to the  reply tweet with id " + retweetId;
        }
        return "Unauthorized user";
    }

    @Override
    public String unlikeRetweet(int retweetId, int authorizationId){
        if(authorized(authorizationId)) {
            query = "delete from ReplyTweetLikes where replyTweetId = ? and username = ?";
            jdbcTemplate.update(query, retweetId, loggedInUsername);
            query = "update ReplyTweets set likes = ? where replyTweetId = ?";
            jdbcTemplate.update(query, getReplyTweetLikes(retweetId), retweetId);
            return "Ok";
        }
        return "Unauthorized user";
    }

    public String updateProfileDetails(MultipartFile file){
        String url = generateIconImageUrl(file);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(url);
        query = "insert into ProfileIconDetails values (?,?,?,?,?)";
        try {
            jdbcTemplate.update(query, loggedInUsername, file.getBytes(), url, fileName, file.getContentType());
            return "Profile photo details updated";
        } catch(Exception e){
            return e.getLocalizedMessage();
        }
    }

    public String updateReplyTweetPhotoDetails(int replyTweetId, MultipartFile file) throws IOException {
        String url = generateReplyTweetImageUrl(file, replyTweetId);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        query = "insert into ReplyTweetPhotoDetails values (?,?,?,?,?)";
        jdbcTemplate.update(query, replyTweetId, file.getBytes(), url, fileName, file.getContentType());
        return "Reply tweet photo details updated";
    }

    public String updateTweetPhotoDetails(int tweetId, MultipartFile file) throws IOException {
        String url = generateTweetImageUrl(file, tweetId);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        query = "insert into TweetPhotoDetails values (?,?,?,?,?)";
        jdbcTemplate.update(query, tweetId, file.getBytes(), url, fileName, file.getContentType());
        return "Tweet photo details updated";
    }

    public String generateIconImageUrl(MultipartFile file){
        if(file.equals(null)){
            return null;
        }
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("user/profilePhoto/")
                .path(loggedInUsername)
                .toUriString();
        return url;
    }

    public String generateTweetImageUrl(MultipartFile file, int tweetId){
        if(file.equals(null)){
            return null;
        }
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("user/tweetPhoto/")
                .path(String.valueOf(tweetId))
                .toUriString();
        return url;
    }

    public String generateReplyTweetImageUrl(MultipartFile file, int replyTweetId){
        if(file.equals(null)){
            return null;
        }
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("user/replyTweetPhoto/")
                .path(String.valueOf(replyTweetId))
                .toUriString();
        return url;
    }

    @Override
    public byte[] getIconImageBytes(String username) {
        query = "select PhotoData from ProfileIconDetails where username = '" + username + "'";
        return jdbcTemplate.queryForObject(query, byte[].class);
    }

    @Override
    public byte[] getTweetImageBytes(int tweetId) {
        query = "select PhotoData from TweetPhotoDetails where tweetId = " + tweetId;
        return jdbcTemplate.queryForObject(query, byte[].class);
    }

    @Override
    public byte[] getReplyTweetImageBytes(int replyTweetId) {
        query = "select PhotoData from ReplyTweetPhotoDetails where ReplyTweetId = " + replyTweetId;
        return jdbcTemplate.queryForObject(query, byte[].class);
    }

    @Override
    public List<Tweet> getRecentTweets(int authorizationId){
        if(authorized(authorizationId)){
            List<Tweet> tweets = new ArrayList<>();
            query = "select TweetId, UserName, Hashtag, Description, TweetDate, likes from Tweets Order by(TweetDate) desc";
                jdbcTemplate.query(query, (resultSet, no) -> {
                Tweet tweet = new Tweet();
                tweet.setTweetId(resultSet.getInt(1));
                tweet.setUsername(resultSet.getString(2));
                tweet.setName(getNameByUsername(resultSet.getString(2)));
                tweet.setUserIconUrl(getUrlByUsername(resultSet.getString(2)));
                tweet.setHashtag(resultSet.getString(3));
                tweet.setDescription(resultSet.getString(4));
                tweet.setTweetPhotoUrl(getUrlByTweetId(resultSet.getInt(1)));
                tweet.setTweetDate(resultSet.getTimestamp(5));
                tweet.setLikes(resultSet.getInt(6));

                tweets.add(tweet);

                return tweet;
            });
                return tweets;
        }
        return null;
    }

    @Override
    public List<Tweet> getReplyTweets(int tweetId, int authorizationId){
        if(authorized(authorizationId)){
            List<Tweet> replyTweets = new ArrayList<>();
            query = "select ReplyTweetId, UserName, Hashtag, Description, likes from ReplyTweets where TweetId = " + tweetId + " Order by ReplyTweetId desc";
            jdbcTemplate.query(query, (resultSet, no) -> {
                Tweet tweet = new Tweet();
                tweet.setTweetId(resultSet.getInt(1));
                tweet.setUsername(resultSet.getString(2));
                tweet.setName(getNameByUsername(resultSet.getString(2)));
                tweet.setUserIconUrl(getUrlByUsername(resultSet.getString(2)));
                tweet.setHashtag(resultSet.getString(3));
                tweet.setDescription(resultSet.getString(4));
                tweet.setTweetPhotoUrl(getUrlByReplyTweetId(resultSet.getInt(1)));
                //tweet.setTweetDate(resultSet.getTimestamp(5));
                tweet.setLikes(resultSet.getInt(5));

                replyTweets.add(tweet);

                return tweet;
            });
            return replyTweets;
        }
        return null;
    }

    @Override
    public List<Follow> getFollowers(int authorizationId){
        if(authorized(authorizationId)){
            List<Follow> followers = new ArrayList<>();
            query = "select followedBy from Follows where followed = '" + loggedInUsername + "'";
            jdbcTemplate.query(query, (resultSet, no) -> {
                Follow follow = new Follow();
                follow.setFollowed(resultSet.getString(1));
                follow.setFollowedName(getNameByUsername(resultSet.getString(1)));
                follow.setFollowedDp(getUrlByUsername(resultSet.getString(1)));
                followers.add(follow);
                return follow;
            } );
            return followers;
        }
        return null;
    }

    @Override
    public List<Follow> getFollowings(int authorizationId){
        if(authorized(authorizationId)){
            List<Follow> followings = new ArrayList<>();
            query = "select followed from Follows where followedBy = '" + loggedInUsername + "'";
            jdbcTemplate.query(query, (resultSet, no) -> {
                Follow follow = new Follow();
                follow.setFollowed(resultSet.getString(1));
                follow.setFollowedName(getNameByUsername(resultSet.getString(1)));
                follow.setFollowedDp(getUrlByUsername(resultSet.getString(1)));
                followings.add(follow);
                return follow;
            } );
            return followings;
        }
        return null;
    }

    @Override
    public List<Tweet> getMyTweets(int authorizationId){
        if(authorized(authorizationId)) {
            List<Tweet> myTweets = new ArrayList<>();
            query = "select TweetId, UserName, Hashtag, Description, TweetDate, likes from Tweets where username = '" + loggedInUsername + "' Order by(TweetDate) desc";
            jdbcTemplate.query(query, (resultSet, no) -> {
                Tweet tweet = new Tweet();
                tweet.setTweetId(resultSet.getInt(1));
                tweet.setUsername(resultSet.getString(2));
                tweet.setName(getNameByUsername(resultSet.getString(2)));
                tweet.setUserIconUrl(getUrlByUsername(resultSet.getString(2)));
                tweet.setHashtag(resultSet.getString(3));
                tweet.setDescription(resultSet.getString(4));
                tweet.setTweetPhotoUrl(getUrlByTweetId(resultSet.getInt(1)));
                tweet.setTweetDate(resultSet.getTimestamp(5));
                tweet.setLikes(resultSet.getInt(6));

                myTweets.add(tweet);

                return tweet;
            });
            return myTweets;
        }
        return null;
    }

    @Override
    public UserProfile showFollowerProfile(String followerUsername, int authorizationId){
        if(authorized(authorizationId)) {
            if (isFollowing(loggedInUsername, followerUsername) || loggedInUsername.equals(followerUsername)) {
                query = "select username, name, bio, followersCount, followingCount, verified from usersProfile where userName = '" + followerUsername + "'";
                UserProfile userProfile = jdbcTemplate.queryForObject(query, (resultSet, no) -> {
                    UserProfile userProfile1 = new UserProfile();

                    userProfile1.setUsername(resultSet.getString(1));
                    userProfile1.setName(resultSet.getString(2));
                    userProfile1.setIconUrl(getUrlByUsername(resultSet.getString(1)));
                    userProfile1.setBio(resultSet.getString(3));
                    userProfile1.setFollowersCount(resultSet.getInt(4));
                    userProfile1.setFollowingCount(resultSet.getInt(5));
                    userProfile1.setVerified(resultSet.getBoolean(6));
                    return userProfile1;
                });
                return userProfile;
            }
            return null;
        }
        return null;

    }

    public String getUrlByUsername(String username){
        query = "select PhotoUrl from ProfileIconDetails where username = '" + username + "'";
        try {
            return jdbcTemplate.queryForObject(query, String.class);
        }catch (Exception e){
            System.out.println("No profile photo found for the user " + username);
            return null;
        }
    }

    public String getUrlByTweetId(int tweetId){
        query = "select PhotoUrl from TweetPhotoDetails where tweetId = " + tweetId;
        try {
            return jdbcTemplate.queryForObject(query, String.class);
        }catch(Exception e){
            return null;
        }
    }

    public String getUrlByReplyTweetId(int replyTweetId){
        query = "select PhotoUrl from ReplyTweetPhotoDetails where ReplyTweetId = " + replyTweetId;
        try {
            return jdbcTemplate.queryForObject(query, String.class);
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public int getFollowingsCount(String username){
        query = "select count(*) from Follows where followedBy = '" + username + "'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    @Override
    public int getFollowersCount(String username){
        query = "select count(*) from Follows where followed = '" + username + "'";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public boolean isFollowing(String followedBy, String followed){
        query = "select count(*) from follows where followedBy = '" + followedBy + "' and followed = '" + followed + "'";
        int count = 0;
        try {
            count = jdbcTemplate.queryForObject(query, Integer.class);
        } catch (Exception e) {
            count = 0;
        }
        finally{
            if(count == 0)
                return false;
            return true;
        }
    }

    public String getNameByUsername(String username) {
        query = "select Name from usersProfile where username = '" + username +"'";
        return jdbcTemplate.queryForObject(query, String.class);
    }

    public int getTweetLikes(int tweetId){
        query = "select count(*) from TweetLikes where tweetId = " + tweetId;
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public int getReplyTweetLikes(int replyTweetId){
        query = "select count(*) from ReplyTweetLikes where ReplyTweetId = " + replyTweetId;
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public boolean tweetBelongsToUser(int tweetId, String username){
        query = "select count(*) from Tweets where tweetId = " + tweetId + " and Username = '" + username + "'";
        int count = 0;
        try {
            count = jdbcTemplate.queryForObject(query, Integer.class);
        } catch (Exception e) {
            count = 0;
        }
        finally{
            if(count == 0)
                return false;
            return true;
        }
    }

    public boolean tweetReplyBelongsToUser(int replyTweetId, String username){
        query = "select count(*) from ReplyTweets where ReplyTweetId = " + replyTweetId + " and Username = '" + username + "'";
        int count = 0;
        try {
            count = jdbcTemplate.queryForObject(query, Integer.class);
        } catch (Exception e) {
            count = 0;
        }
        finally{
            if(count == 0)
                return false;
            return true;
        }
    }

}