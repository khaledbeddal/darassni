package com.esi.mscours.API;


import com.esi.mscours.DTO.LectureDTO;
import com.esi.mscours.entities.Groupe;
import com.esi.mscours.entities.Lecture;
import com.esi.mscours.model.Debited;
import com.esi.mscours.proxy.PaymentProxy;
import com.esi.mscours.repository.GroupeRepository;
import com.esi.mscours.repository.LectureRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1")
public class LectureController {



    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private GroupeRepository groupeRepository;
    @Autowired
    private  PaymentProxy paymentProxy;

    @GetMapping("/lectures")
    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }

    //Techer add lecture

    @PostMapping("/add-Lecture")
    public Lecture addLecture(@RequestBody LectureDTO lectureDTO) {
        Groupe groupe = groupeRepository.findById(lectureDTO.getIdGroupe()).get();
        if(groupe!=null){
            Lecture lecture = new Lecture();
            lecture.setGroupe(groupe);
            lecture.setDate(new Date(lectureDTO.getDate()));
            lecture.setTitle(lectureDTO.getTitle());
            return  lectureRepository.save(lecture);
        }
        return null;
    }


    @GetMapping("/student-get-lecture/{id}")

    public Lecture getLecture(@PathVariable(value="id") Long idLecture
    ,@RequestParam(value = "idStudent") Long idStudent) {
        System.out.println(idLecture);
        Lecture lecture = lectureRepository.findById(idLecture).get();
        System.out.println(lecture);

        if(lecture != null) {
            Debited debited = paymentProxy.getPayment(idLecture, idStudent);
            if(debited!=null){
               return lecture;
           }
           else {
               return null;
           }
        }
        else return null;

    }

    @GetMapping("/lecture/{id}")

    public Lecture getAllGroupe(@PathVariable(value="id") Long idLecture) {
        return lectureRepository.findById(idLecture).get();
    }




}
