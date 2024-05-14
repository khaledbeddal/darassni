package com.esi.mscours.API;

import com.esi.mscours.DTO.ModuleDTO;
import com.esi.mscours.DTO.SpecialityDTO;
import com.esi.mscours.entities.Module;
import com.esi.mscours.entities.Speciality;
import com.esi.mscours.repository.ModuleRepository;
import com.esi.mscours.repository.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class ModuleSpecialityController {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private SpecialityRepository specialityRepository;

    @GetMapping("/modules")
    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    };

    // Ajouter Module

    @PostMapping("/modules")
    public Module addModule(@RequestBody ModuleDTO moduleDTO) {
        Speciality speciality =specialityRepository.findById(moduleDTO.getSpecialityId()).get();
        Module module = new Module();
        module.setSpeciality(speciality);
        module.setName(moduleDTO.getName());
    return  moduleRepository.save(module);
    }

    @GetMapping("/specialities")
    public  List<Speciality> getAllSpecialities() {
        return specialityRepository.findAll();
    }


    @PostMapping("/specialities")
    public Speciality addSpecilality(@RequestBody SpecialityDTO specialityDTO) {

        Speciality speciality = new Speciality();
        speciality.setName(specialityDTO.getName());

        return  specialityRepository.save(speciality);
    }

}

