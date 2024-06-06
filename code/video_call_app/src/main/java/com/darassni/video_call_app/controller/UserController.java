package com.darassni.video_call_app.controller;

import com.darassni.video_call_app.model.User;
import com.darassni.video_call_app.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {
    private final UserService userService;
    private String email;
    private String roles;

@PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping("/create-conf")
    public ResponseEntity conf( @RequestHeader("Authorization") String authorizationHeader){

      email = userService.getEmailFromAuthorizationHeader(authorizationHeader).get(0);
      roles=userService.getEmailFromAuthorizationHeader(authorizationHeader).get(1);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");

};

    @PreAuthorize("hasRole('ROLE_STUDENT')")
            @GetMapping("/join-conf")
    public void join( @RequestHeader("Authorization") String authorizationHeader){

        email = userService.getEmailFromAuthorizationHeader(authorizationHeader).get(0);
        roles=userService.getEmailFromAuthorizationHeader(authorizationHeader).get(1);


    };


    @PostMapping
    public void register(@RequestBody User user) {
        System.out.println("Registering user :" + user);
        userService.register(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userService.login(user);
    }


    @PostMapping("/logout")
    public void logout(@RequestBody User user) {
        userService.logout(user.getEmail());
    }

    @GetMapping
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
