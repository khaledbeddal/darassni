package com.esi.mscours.API;

import com.esi.mscours.DTO.GroupeDTO;
import com.esi.mscours.entities.Groupe;
import com.esi.mscours.entities.Lecture;

import com.esi.mscours.entities.Module;
import com.esi.mscours.entities.StudentJoinGroupe;
import com.esi.mscours.model.User;
import com.esi.mscours.proxy.UserProxy;
import com.esi.mscours.repository.GroupeRepository;
import com.esi.mscours.repository.ModuleRepository;
import com.esi.mscours.repository.StudentJoinGroupeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/v1")
public class GroupeController {
    @Autowired
    private GroupeRepository groupeRepository;
    @Autowired
    private ModuleRepository moduleRepository ;
    @Autowired
    private StudentJoinGroupeRepository studentJoinGroupeRepository;
    @Autowired
    private UserProxy userProxy;


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
    public ResponseEntity<Groupe> updateGroupeByPatch(
            @PathVariable(value = "id") Long idGroupe,
            @RequestBody Map<String, Object> payload) {

        Optional<Groupe> optionalGroupe = groupeRepository.findById(idGroupe);
        if (optionalGroupe.isPresent()) {
            Groupe groupe = optionalGroupe.get();
            Long idTeacher = Long.valueOf(payload.get("idTeacher").toString());
            if (groupe.getIdTeacher().equals(idTeacher)) {
                if (payload.containsKey("name")) {
                    groupe.setName(payload.get("name").toString());
                }
                if (payload.containsKey("lecturePrice")) {
                    groupe.setLecturePrice(Double.valueOf(payload.get("lecturePrice").toString()));
                }
                if (payload.containsKey("max")) {
                    groupe.setMax(Integer.valueOf(payload.get("max").toString()));
                }
                return ResponseEntity.ok(groupeRepository.save(groupe));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER') or hasRole('ROLE_STUDENT')")
    @GetMapping("/search-groupes")
    public ResponseEntity<List<Groupe>> searchGroupes(@RequestParam(required = false) String name,
                                                      @RequestParam(required = false) String firstName,
                                                      @RequestParam(required = false) String lastName,
                                                      @RequestParam(required = false) String moduleName,@RequestHeader("Authorization") String authorizationHeader) {

        List<Groupe> groupes = groupeRepository.findAll();

        List<Groupe> filteredGroupes = groupes.stream()
                .filter(groupe -> {
                    User teacher = userProxy.getTeacher(groupe.getIdTeacher(),"tocours",authorizationHeader);
                    groupe.setTeacher(teacher);

                    boolean matchesName = (name == null || groupe.getName().contains(name));
                    boolean matchesFirstName = (firstName == null || (teacher != null && teacher.getFirstName().contains(firstName)));
                    boolean matchesLastName = (lastName == null || (teacher != null && teacher.getLastName().contains(lastName)));
                    boolean matchesModuleName = (moduleName == null || (groupe.getModule() != null && groupe.getModule().getName().toString().contains(moduleName)));

                    return matchesName && matchesFirstName && matchesLastName && matchesModuleName;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(filteredGroupes);
    }



}







