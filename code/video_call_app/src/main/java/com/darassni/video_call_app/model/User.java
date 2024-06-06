package com.darassni.video_call_app.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;



@Entity
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class User {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String username;
    private String email;
    private String password;
    private String status;

}
