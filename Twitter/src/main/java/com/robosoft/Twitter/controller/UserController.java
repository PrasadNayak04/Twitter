package com.robosoft.Twitter.controller;

import com.robosoft.Twitter.entity.User;
import com.robosoft.Twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/profileCompletion/{authorizationId}")
    public byte[] displayProfile(@PathVariable String username)
    {
        return new byte[0];
    }
}
