package com.robosoft.Twitter.controller;

import com.robosoft.Twitter.model.Follow;
import com.robosoft.Twitter.model.Tweet;
import com.robosoft.Twitter.model.User;
import com.robosoft.Twitter.model.UserProfile;
import com.robosoft.Twitter.modelAttribute.TweetModel;
import com.robosoft.Twitter.modelAttribute.UserProfileModel;
import com.robosoft.Twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userServices;

    @PostMapping("/registration")
    public String signUp(@ModelAttribute User user)
    {
        return userServices.signUp(user);
    }

    @PostMapping("/profileCompletion/{authorizationId}")
    public String completeProfile(@ModelAttribute UserProfileModel userProfileModel, @PathVariable int authorizationId) throws IOException {
        return userServices.completeProfile(userProfileModel, authorizationId);
    }

    @GetMapping("/login")
    public ResponseEntity<String> signIn(@RequestParam String username, @RequestParam String password){
        String message = userServices.signIn(username, password);
        if(message.equalsIgnoreCase("Login unsuccessful.")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/profilePhoto/{username}")
    public ResponseEntity<Resource> getProfilePhoto(@PathVariable String username) {
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/png")).header(HttpHeaders.CONTENT_DISPOSITION,"filename=\"" + username + ".png" + "\"").body(new ByteArrayResource(userServices.getIconImageBytes(username)));
    }

    @GetMapping("/tweetPhoto/{tweetId}")
    public ResponseEntity<Resource> getTweetPhoto(@PathVariable int tweetId) {
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/png")).header(HttpHeaders.CONTENT_DISPOSITION,"filename=\"" + tweetId + ".png" + "\"").body(new ByteArrayResource(userServices.getTweetImageBytes(tweetId)));
    }

    @GetMapping("/replyTweetPhoto/{replyTweetId}")
    public ResponseEntity<Resource> getReplyTweetPhoto(@PathVariable int replyTweetId) {
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/png")).header(HttpHeaders.CONTENT_DISPOSITION,"filename=\"" + replyTweetId + ".png" + "\"").body(new ByteArrayResource(userServices.getReplyTweetImageBytes(replyTweetId)));
    }

    @PostMapping("/follow/{authorizationId}")
    public String addFollower(@RequestParam String username, @PathVariable int authorizationId){
        return userServices.addFollower(username, authorizationId);
    }

    @DeleteMapping("unfollow/{authorizationId}")
    public String removeFollower(@RequestParam String username, @PathVariable int authorizationId){
        return userServices.removeFollower(username, authorizationId);
    }

    @GetMapping("/tweets/{authorizationId}")
    public List<Tweet> getTweets(@PathVariable int authorizationId){
        return userServices.getRecentTweets(authorizationId);
    }

    @GetMapping("/tweet/replies/{authorizationId}")
    public List<Tweet> getTweets(@RequestParam int tweetId, @PathVariable int authorizationId){
        return userServices.getReplyTweets(tweetId, authorizationId);
    }

    @GetMapping("/followers/{authorizationId}")
    public List<Follow> getFollowers(@PathVariable int authorizationId){
        return userServices.getFollowers(authorizationId);
    }

    @GetMapping("/followings/{authorizationId}")
    public List<Follow> getFollowings(@PathVariable int authorizationId){
        return userServices.getFollowings(authorizationId);
    }

    @GetMapping("/myTweets/{authorizationId}")
    public List<Tweet> getMyTweets(@PathVariable int authorizationId){
        return userServices.getMyTweets(authorizationId);
    }

    @GetMapping("/profile/{authorizationId}")
    public UserProfile viewFollowerProfile(@RequestParam String username, @PathVariable int authorizationId){
        return userServices.showFollowerProfile(username, authorizationId);
    }

    @PostMapping("/tweet/{authorizationId}")
    public String postTweet(@ModelAttribute TweetModel tweetModel, @PathVariable int authorizationId) throws IOException {
        return userServices.tweet(tweetModel, authorizationId);
    }

    @DeleteMapping("tweet/delete/{authorizationId}")
    public String deleteTweet(@RequestParam int tweetId, @PathVariable int authorizationId) {
        return userServices.deleteTweet(tweetId, authorizationId);
    }


    @PostMapping("/tweet/like/{authorizationId}")
    public String likeTweet(@RequestParam int tweetId, @PathVariable int authorizationId){
        return userServices.likeTweet(tweetId, authorizationId);
    }

    @DeleteMapping("tweet/unlike/{authorizationId}")
    public String unlikeTweet(@RequestParam int tweetId, @PathVariable int authorizationId) {
        return userServices.removeTweetLike(tweetId, authorizationId);
    }

    @PostMapping("/tweet/reply/like/{authorizationId}")
    public String likeTweetReply(@RequestParam int replyTweetId, @PathVariable int authorizationId){
        return userServices.likeRetweet(replyTweetId, authorizationId);
    }

    @DeleteMapping("tweet/reply/unlike/{authorizationId}")
    public String unlikeRetweet(@RequestParam int replyTweetId, @PathVariable int authorizationId){
        return userServices.unlikeRetweet(replyTweetId, authorizationId);
    }

    @PostMapping("/tweet/reply/{authorizationId}")
    public String reTweet(@RequestParam int tweetId, @ModelAttribute TweetModel tweetModel, @PathVariable int authorizationId) throws IOException {
        return userServices.replyTweet(tweetId, tweetModel, authorizationId);
    }

    @DeleteMapping("/tweet/reply/delete/{authorizationId}")
    public String removeRetweet(@RequestParam int replyTweetId, @PathVariable int authorizationId) {
        return userServices.deleteTweetReply(replyTweetId, authorizationId);
    }

    @GetMapping("/followers/count")
    public int getFollowersCount(@RequestParam String username) {
        return userServices.getFollowersCount(username);
    }

    @PostMapping("/following/count")
    public int getFollowingsCount(@RequestParam String username) {
        return userServices.getFollowingsCount(username);
    }

}
