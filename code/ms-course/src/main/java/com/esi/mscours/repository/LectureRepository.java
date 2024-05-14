package com.esi.mscours.repository;

import com.esi.mscours.entities.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
    public interface LectureRepository extends JpaRepository<Lecture,Long> {
    }
