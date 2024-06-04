package com.esi.mscours.repository;

import com.esi.mscours.DTO.GroupeDTO;
import com.esi.mscours.entities.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface GroupeRepository extends JpaRepository<Groupe,Long> {

    List<Groupe> findGroupesByIdTeacher(Long idTeacher);
}
