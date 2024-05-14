package com.esi.mscours.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupeDTO {


    private String name;
    private int lecturePrice;
    private int max ;
    private Long idModule;
    private Long idUser;


}
