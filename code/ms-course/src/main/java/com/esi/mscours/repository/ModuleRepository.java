package com.esi.mscours.repository;

import com.esi.mscours.DTO.ModuleDTO;
import com.esi.mscours.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource
public interface ModuleRepository extends JpaRepository<Module,Long> {
    @Query("SELECT new com.esi.mscours.DTO.ModuleDTO( m.idModule,m.name, m.speciality.idSpeciality) FROM Module m")
    List<ModuleDTO> findAllModulesData();

}
