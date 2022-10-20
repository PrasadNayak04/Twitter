package com.robosoft.Twitter.controller;

import com.robosoft.Twitter.entity.User;
import com.robosoft.Twitter.model.CommentModel;
import com.robosoft.Twitter.model.TweetModel;
import com.robosoft.Twitter.model.UserProfileModel;
import com.robosoft.Twitter.service.UserProfileService;
import com.robosoft.Twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public String signUp(@RequestBody User user)
    {
        return userService.signUp(user);
    }

    @PostMapping("/profileCompletion")
    public String completeProfile(@ModelAttribute UserProfileModel userProfileModel) throws IOException {
        return userService.completeProfile(userProfileModel);
    }

    @GetMapping("/login")
    public String signIn(@RequestParam String username, @RequestParam String password){
        return userService.signIn(username, password);
    }

    @GetMapping("/profilePhoto/{username}")
    public ResponseEntity<Resource> getProfilePhoto(@PathVariable String username) {
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/png")).header(HttpHeaders.CONTENT_DISPOSITION,"filename=\"" + username + ".png" + "\"").body(new ByteArrayResource(userService.getIcon(username)));
    }

    @PostMapping("/follow")
    public String addFollower(@RequestParam String followerName){
        return userService.addFollower(followerName);
    }

    @PostMapping("/tweet")
    public String postTweet(@ModelAttribute TweetModel tweetModel) throws IOException {
        return userService.tweet(tweetModel);
    }

    @PostMapping("/tweet/like/{authorizationId}")
    public String likeTweet(@RequestParam int tweetId, @PathVariable int authorizationId){
        return userService.likeTweet(tweetId, authorizationId);
    }

    @PostMapping("/tweet/reply/{authorizationId}")
    public String reTweet(@RequestParam int tweetId, @ModelAttribute TweetModel tweetModel, @PathVariable int authorizationId) throws IOException {
        return userService.replyTweet(tweetId, tweetModel, authorizationId);
    }

    @PostMapping("tweet/reply/like/{authorizationId}")
    public String likeRetweet(@RequestParam int retweetId, @PathVariable int authorizationId) {
        return userService.likeRetweet(retweetId, authorizationId);
    }



}
