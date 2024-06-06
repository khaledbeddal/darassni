package com.esi.mscours.DTO;


import com.esi.mscours.entities.Module;
import com.esi.mscours.entities.StudentJoinGroupe;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupeDTO {

    private String name;
    private Double lecturePrice;
    private int max ;
    private Long idModule;
    private Module module;
    private Long idUser;
    private String image;

}
