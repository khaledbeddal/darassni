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
        Document document1 = new Document();
        document1.setLink("http://example.com/doc1");
        document1.setName("doc 1");
        document1.setIdTeacher(1L);
        document1 = documentRepository.save(document1);

        Document document2 = new Document();
        document2.setLink("http://example.com/doc2");
        document2.setName("doc 2");
        document2.setIdTeacher(1L);
        document2 = documentRepository.save(document2);
        for (SpecialityName specialityName : SpecialityName.values()) {
            Speciality speciality = new Speciality(null, specialityName, null);
            speciality = specialityRepository.save(speciality);
            for (ModuleName moduleName : ModuleName.values()) {
                Module module = new Module(null, moduleName, null, speciality);
                module = moduleRepository.save(module);

                // Create groups for each module
                for (int i = 1; i <= 3; i++) {
                    Groupe groupe = new Groupe(null, "Group " + i, 50, 30,"https://placehold.co/600x400", null,module, 1L, null, null);
                    groupe = groupeRepository.save(groupe);

                    // Create lectures for each group
                    Lecture lecture = new Lecture();
                    lecture.setTitle("Lecture for " + groupe.getName());
                    lecture.setDate(new Date());
                    lecture.setGroupe(groupe);

                    lecture.setDocumentList(Arrays.asList(document1, document2));
                    lecture = lectureRepository.save(lecture);

                    Conference conference = new Conference();
                    conference.setLink("http://example.com/conference");
                    conference.setDuration("1 hour");
                    conference.setLecture(lecture);
                    conference = conferenceRepository.save(conference);

                    lecture.setConference(conference);
                    lectureRepository.save(lecture);
                }
            }

    }
}
}
