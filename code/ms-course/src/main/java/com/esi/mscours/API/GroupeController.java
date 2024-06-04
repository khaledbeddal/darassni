package com.esi.mscours.API;

import com.esi.mscours.DTO.GroupeDTO;
import com.esi.mscours.entities.Groupe;
import com.esi.mscours.entities.Lecture;
import com.esi.mscours.entities.Module;
import com.esi.mscours.entities.StudentJoinGroupe;
import com.esi.mscours.repository.GroupeRepository;
import com.esi.mscours.repository.ModuleRepository;
import com.esi.mscours.repository.StudentJoinGroupeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("api/v1")
public class GroupeController {
    @Autowired
    private GroupeRepository groupeRepository;
    @Autowired
    private ModuleRepository moduleRepository ;
    @Autowired
    private StudentJoinGroupeRepository studentJoinGroupeRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/groupes")
    public List<Groupe> getAllGroupes() {
        return groupeRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/groupes/{id}")
    public Groupe getAllGroupe(@PathVariable(value="id") Long idGroupe) {
        return groupeRepository.findById(idGroupe).get();
    }

    // Teacher add Groupe

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping("/add-group")
    public Groupe addGroupe(@RequestBody GroupeDTO groupeDTO) {
        Module module = moduleRepository.findById(groupeDTO.getIdModule()).get();
        if( module != null ) {
        Groupe groupe = new Groupe();
        groupe.setModule(module);
        groupe.setMax(groupeDTO.getMax());
        groupe.setName(groupeDTO.getName());
        groupe.setLecturePrice(groupeDTO.getLecturePrice());
        groupe.setIdTeacher(groupeDTO.getIdUser());
        groupe.setImage(groupeDTO.getImage());
        return  groupeRepository.save(groupe); }
        else return null ;
    }

    // Etudiant Join Groupe
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PostMapping("/sign-up-to-group")
    public Groupe affectGroupes(@RequestBody Map<String,Object> payload) {


        if(payload.get("idGroupe") != null && payload.get("idStudent") !=null && payload.get("name")!=null) {

            Long idGroupe = Long.valueOf(payload.get("idGroupe").toString());
            Long idStudent = Long.valueOf(payload.get("idStudent").toString());

            if(groupeRepository.findById(idGroupe).isPresent()) {

                Groupe groupe = groupeRepository.findById(idGroupe).get();
                ArrayList <StudentJoinGroupe> students = new ArrayList<>(groupe.getStudents());
                if(studentJoinGroupeRepository.findStudentJoinGroupeByIdGroupeAndIdStudent(idGroupe,idStudent)!=null){
                    return null;
                }else if (groupe.getMax() > students.size()) {
                    students.add(studentJoinGroupeRepository.save(new StudentJoinGroupe(null,idGroupe,idStudent,payload.get("name").toString())));
                    groupe.setStudents(students);
                    return  groupeRepository.save(groupe);
                }
                else return  null;

            } else  return  null;

        } else
            return null;
    }



    // Get Student Groupes
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/student-groupes" )
    public  List<Groupe> getStudentGroupes( @RequestParam Long idStudent) {

        List<Groupe> groupes = new ArrayList<>();

        List<StudentJoinGroupe> studentJoinGroupes = studentJoinGroupeRepository.findStudentJoinGroupesByIdStudent(idStudent);
        studentJoinGroupes.forEach(e-> {
            groupes.add(groupeRepository.findById(e.getIdGroupe()).get());

        });


        return groupes;

    }


    // Get Teacher Groupes

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping("/teacher-groupes" )
    public  List<Groupe> getTeacherGroupes( @RequestParam Long idTeacher) {

        List<Groupe> groupes = groupeRepository.findGroupesByIdTeacher(idTeacher) ;
        return groupes;

    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping("/teacher-groupe/{id}" )
    public  Groupe getTeacherGroupe( @PathVariable(value="id") Long idGroupe,@RequestParam(value = "idTeacher") String idTeacher) {
        if(groupeRepository.findById(idGroupe).isPresent()
                && groupeRepository.findById(idGroupe).get().getIdTeacher().equals(Long.valueOf(idTeacher))){
            return groupeRepository.findById(idGroupe).get();
        }else return null;
    }




    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PatchMapping("/groupes/{id}")
    public Groupe updateGroupeByPatch(@PathVariable(value="id") Long idGroupe,
                                  @RequestBody Map<String, Object> payload){

        if( groupeRepository.findById(idGroupe).isPresent()){

            Groupe groupe = groupeRepository.findById(idGroupe).get();

            if(payload.get("name")!=null) groupe.setName(payload.get("name").toString());
            if(payload.get("lecturePrice")!=null) groupe.setLecturePrice(payload.get("lecturePrice").hashCode());
            if (payload.get("idModule")!=null) {
                Module module = moduleRepository.findById(Long.valueOf(payload.get("idModule").hashCode())).get();
                groupe.setModule(module);
            }
            return groupeRepository.save(groupe);
        }
        return null;
    }




    @PreAuthorize("hasRole('ROLE_TEACHER')")

    @DeleteMapping("/groupes/{id}")
    public ResponseEntity<String> deleteGroupe(@PathVariable("id") Long idGroupe) {
        if (groupeRepository.existsById(idGroupe)) {
            try {
                groupeRepository.deleteById(idGroupe);
                return ResponseEntity.ok("Le groupe a été supprimé.");
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Impossible de supprimer le groupe. Assurez-vous qu'il n'y a pas de dépendances.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}






