package com.robosoft.Twitter.controller;

import com.robosoft.Twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {

    @Autowired
    private UserService service;
    @PostMapping("/signup")
    public String signup(@RequestParam String user, @RequestParam MultipartFile file)
    {
        return service.signup(user,file);
    }

    @GetMapping("/viewProfile")
    public byte[] displayProfile(@PathVariable String username)
    {
        return service.getuser(username);
    }
}
