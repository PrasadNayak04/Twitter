package com.robosoft.Twitter.service;

import com.robosoft.Twitter.model.Follow;
import com.robosoft.Twitter.model.Tweet;
import com.robosoft.Twitter.model.User;
import com.robosoft.Twitter.model.UserProfile;
import com.robosoft.Twitter.modelAttribute.TweetModel;
import com.robosoft.Twitter.modelAttribute.UserProfileModel;

import java.io.IOException;
import java.util.List;

public interface UserServices {

    String signUp(User user);
    String signIn(String username, String givenPassword);
    String completeProfile(UserProfileModel userProfile, int authorizationId);
    String tweet(TweetModel tweet, int authorizationId) throws IOException;
    String deleteTweet(int tweetId, int authorizationId);
    String addFollower(String followerUsername, int authorizationId);
    String removeFollower(String followerUsername, int authorizationId);
    String replyTweet(int tweetId, TweetModel tweet, int authorizationId) throws IOException ;
    String deleteTweetReply(int replyTweetId, int authorizationId);
    String likeTweet(int tweetId, int authorizationId);
    String removeTweetLike(int tweetId, int authorizationId);
    String likeRetweet(int retweetId, int authorizationId);
    String unlikeRetweet(int retweetId, int authorizationId);
    List<Tweet> getRecentTweets(int authorizationId);
    List<Tweet> getReplyTweets(int tweetId, int authorizationId);
    List<Follow> getFollowers(int authorizationId);
    List<Follow> getFollowings(int authorizationId);
    List<Tweet> getMyTweets(int authorizationId);
    UserProfile showFollowerProfile(String followerUsername, int authorizationId);
    int getFollowingsCount(String username);
    public int getFollowersCount(String username);
    int getTweetLikes(int tweetId);
    int getReplyTweetLikes(int replyTweetId);
    byte[] getIconImageBytes(String username);
    byte[] getTweetImageBytes(int tweetId);
    public byte[] getReplyTweetImageBytes(int replyTweetId);

}
