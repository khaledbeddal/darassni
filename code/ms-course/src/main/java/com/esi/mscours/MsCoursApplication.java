package com.esi.mscours;

import com.esi.mscours.entities.Module;
import com.esi.mscours.entities.*;
import com.esi.mscours.repository.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

@SpringBootApplication
@EnableFeignClients

public class MsCoursApplication implements CommandLineRunner {
    @Resource
    GroupeRepository groupeRepository;
    @Resource
    SpecialityRepository specialityRepository;
    @Resource
    ModuleRepository moduleRepository;
    @Resource
    LectureRepository lectureRepository;
    @Resource
    DocumentRepository documentRepository;
    @Resource
    ConferenceRepository conferenceRepository;

    public static void main(String[] args) {
        SpringApplication.run(MsCoursApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Speciality speciality = new Speciality(null, SpecialityName.Moy_2,null);
       speciality=  specialityRepository.save(speciality);

        Module module = new Module(null,"Module 1",null,speciality);
        module = moduleRepository.save(module);

        Groupe groupe = new Groupe(null,"Group 1",50,30,null,module,1L,null,null);
        groupe = groupeRepository.save(groupe);

        Groupe groupe2 = new Groupe(null,"Group 2",50,30,null,module,1L,null,null);
        groupe2 = groupeRepository.save(groupe2);

        Groupe groupe3 = new Groupe(null,"Group 3",50,30,null,module,1L,null,null);
        groupe3 = groupeRepository.save(groupe3);

        Lecture lecture = new Lecture();
        lecture.setTitle("Introduction to Spring Boot");
        lecture.setDate(new Date());
        lecture.setGroupe(groupe);
      //  lecture = lectureRepository.save(lecture);

        Document document1 = new Document();
        document1.setLink("link1");
        document1 = documentRepository.save(document1);

        Document document2 = new Document();
        document2.setLink("link2");
        document2 = documentRepository.save(document2);

        lecture.setDocumentList(Arrays.asList(document1, document2));
        lectureRepository.save(lecture);

        Conference conference = new Conference();
        conference.setLink("conferenceLink");
        conference.setDuration("1 hour");
        conference.setLecture(lecture);
        conferenceRepository.save(conference);

        lecture.setConference(conference);
        lectureRepository.save(lecture);




    }
}
