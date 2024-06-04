package com.esi.mscours.entities;

import com.esi.mscours.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Groupe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGroupe;

    private String name;
    private int lecturePrice;
    private int max ;
    private String image; // Field to store the image URL

    @OneToMany(mappedBy = "groupe", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Lecture> lectures;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idModule")
    private Module module;

    private Long idTeacher;
    @Transient
    private User teacher;


    @OneToMany
    private List<StudentJoinGroupe> students;



}
