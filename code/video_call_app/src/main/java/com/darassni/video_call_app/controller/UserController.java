package com.darassni.video_call_app.controller;

import com.darassni.video_call_app.model.User;
import com.darassni.video_call_app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Controller
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {
    private final UserService userService;
    private String username = "user1";
    private String role = "ROLE_TEACHER";

    @GetMapping("/cof")
    // feat: add token (role) + username
    public String setUsername() {
        this.username = username;
        this.role= role;
        return "redirect:/index.html";
    }


    @GetMapping("/username")
    @ResponseBody
    public UseR getUserInfo() {
        return new UseR(this.username, this.role);
    }

    @PostMapping
    @ResponseBody
    public void register(@RequestBody User user) {
        System.out.println("Registering user :" + user);
        userService.register(user);
    }

    @PostMapping("/login")
    @ResponseBody
    public User login(@RequestBody User user) {
        return userService.login(user);
    }


    @PostMapping("/logout")
    @ResponseBody
    public void logout(@RequestBody User user) {
        userService.logout(user.getEmail());
    }

    @GetMapping
    @ResponseBody
    public List<User> findAll() {
        return userService.findAll();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
}
