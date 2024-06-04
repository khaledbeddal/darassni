package com.esi.mscours.DTO;

import com.esi.mscours.entities.ModuleName;
import com.esi.mscours.entities.SpecialityName;

public class ModuleResDTO {
    private Long idModule;
    private ModuleName name;
    private SpecialityName specialityName;

    // Constructors, getters, and setters

    public ModuleResDTO(Long idModule, ModuleName name, SpecialityName specialityName) {
        this.idModule = idModule;
        this.name = name;
        this.specialityName = specialityName;
    }

    public Long getIdModule() {
        return idModule;
    }

    public void setIdModule(Long idModule) {
        this.idModule = idModule;
    }

    public ModuleName getName() {
        return name;
    }

    public void setName(ModuleName name) {
        this.name = name;
    }

    public SpecialityName getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(SpecialityName specialityName) {
        this.specialityName = specialityName;
    }
}
