package com.esi.mscours.msauth.controller;


import com.esi.mscours.msauth.DTO.*;
import com.esi.mscours.msauth.config.TokenProvider;
import com.esi.mscours.msauth.dao.*;
import com.esi.mscours.msauth.entities.*;

import com.esi.mscours.msauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleDao roleDao;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private AdminDao adminDao;
  /*

    @Autowired
    private CitizenDao citizenDao;
    @Autowired
    AgentDao agentDao;

    */
    @Resource(name = "userService")
    private UserDetailsService userDetailsService;


    //Reset the password

    @RequestMapping(value = "/reset/{email}" ,method = RequestMethod.PUT)
    public  ResponseEntity<?> resetPassword(@PathVariable String email,@RequestBody Reset reset){
        Auth auth =  userDao.findByEmail(email);
        if(auth != null){
            auth.setPassword(passwordEncoder.encode((reset.getPassword())));
            userDao.save(auth);
            return  new ResponseEntity<>("Le mot de passe est bien modifié",HttpStatus.OK);
        }
        return  new ResponseEntity<>("Utilisateur n'existe pas",HttpStatus.BAD_REQUEST);
    }







    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/student/{email}",method = RequestMethod.GET)
    public  ResponseEntity<?> getStudent (@PathVariable String email, @RequestHeader("Authorization") String authorizationHeader){

        Student student =studentDao.findStudentByEmail(email);
        return   ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION,authorizationHeader).body(student);



    }



    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/teacher/{email}",method = RequestMethod.GET)
    public  ResponseEntity<?> getTeacher (@PathVariable String email, @RequestHeader("Authorization") String authorizationHeader){

        Teacher teacher = teacherDao.findTeacherByEmail(email);
        return   ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION,authorizationHeader).body(email);



    }





    /// KHASNNIII NSAGAD AUTHENTIFICATION TA3 TEACHER

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody LoginUser loginUser) throws AuthenticationException {



        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getEmail(),
                        loginUser.getPassword()
                )
        );

        Auth  user = userDao.findAuthByEmail(loginUser.getEmail());
        if(user.isStatus()==true) {

            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                final String token = jwtTokenUtil.generateToken(authentication);

                return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).body(token);
            } else {

                throw new RuntimeException("invalid access");
            }
        }
        else return new ResponseEntity<>("le compte est desctiver",HttpStatus.BAD_REQUEST);

    }




    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/profile-student/{email}")
    public Student getProfileStudent(@PathVariable("email") String email){
        Student result= studentDao.findStudentByEmail(email);
        Student student=new Student();
        student.setEmail(result.getEmail());
        student.setId(result.getId());
        student.setSpeciality(result.getSpeciality());
        student.setFirstName(result.getFirstName());
        student.setLastName(result.getLastName());
        student.setBirthdate(result.getBirthdate());
        return student;

    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/profile-teacher/{email}")
    public Teacher getProfileTeacher(@PathVariable("email") String email){
        Teacher result= teacherDao.findTeacherByEmail(email);
        Teacher teacher=new Teacher();
        teacher.setEmail(result.getEmail());
        teacher.setId(result.getId());
        teacher.setModuleName(result.getModuleName());
        teacher.setFirstName(result.getFirstName());
        teacher.setLastName(result.getLastName());
        teacher.setBirthdate(result.getBirthdate());
        return teacher;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/profile-admin/{email}")
    public Admin getProfileAdmin(@PathVariable("email") String email){
        Admin result= adminDao.findAdminByEmail(email);
        Admin admin=new Admin();
        admin.setEmail(result.getEmail());
        admin.setId(result.getId());
        admin.setFirstName(result.getFirstName());
        admin.setLastName(result.getLastName());
        return admin;
    }


    @RequestMapping(value = "/register-student", method = RequestMethod.POST)
    public ResponseEntity<String> register(@RequestBody StudentDTO studentDTO) {
        if (userDao.existsByEmail(studentDTO.getEmail())) {
            return new ResponseEntity<>("L'email n'est pas disponible!", HttpStatus.BAD_REQUEST);
        }

        Auth user = new Auth();
        Student student = new Student();


        student.setEmail(studentDTO.getEmail());

        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());

        student.setGender(studentDTO.getGender());
        student.setStatus(true);
        student.setBirthdate(studentDTO.getBirthdate());
        student.setSpeciality(studentDTO.getSpeciality());

        user.setEmail(studentDTO.getEmail());
        user.setPassword(passwordEncoder.encode((studentDTO.getPassword())));

        List<Role> roles = new ArrayList<>();
        roles.add(roleDao.findByName("STUDENT"));
        user.setRoles(roles);
        user.setStatus(true);
        studentDao.save(student);
        userDao.save(user);


        return new ResponseEntity<>("Student est bien enregistrer!", HttpStatus.OK);

    }



    // register-teacher
    @RequestMapping(value = "/register-teacher", method = RequestMethod.POST)
    public ResponseEntity<String> registerTeacher(@RequestBody TeacherDTO teacherDTO) {
        if (userDao.existsByEmail(teacherDTO.getEmail())) {
            return new ResponseEntity<>("L'email n'est pas disponible!", HttpStatus.BAD_REQUEST);
        }

        Auth user = new Auth();
        Teacher teacher = new Teacher();

        teacher.setEmail(teacherDTO.getEmail());
        teacher.setFirstName(teacherDTO.getFirstName());
        teacher.setLastName(teacherDTO.getLastName());
        teacher.setGender(teacherDTO.getGender());
        teacher.setStatus(false);
        teacher.setBirthdate(teacherDTO.getBirthdate());
        teacher.setModuleName(teacherDTO.getModuleName());

        teacher.setCv(teacherDTO.getCv()); // Set the CV link

        user.setEmail(teacherDTO.getEmail());
        user.setPassword(passwordEncoder.encode((teacherDTO.getPassword())));

        List<Role> roles = new ArrayList<>();
        roles.add(roleDao.findByName("TEACHER"));
        user.setRoles(roles);
        teacherDao.save(teacher);
        userDao.save(user);

        return new ResponseEntity<>("Teacher est bien enregistrer! mais n'est pas encore activé", HttpStatus.OK);
    }


    // get teachers
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/teachers")
    public List<Teacher> getProfileTeacher(){

        List<Teacher> teachers=teacherDao.findAll();
        return  teachers;
    }

    // activate teacher acount

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/activate-teacher/{email}", method = RequestMethod.PATCH)
    public ResponseEntity<?> activateTeacher(@PathVariable String email) {
        if (userDao.existsByEmail(email)) {
            Auth auth = userDao.findAuthByEmail(email);
            auth.setStatus(true);
            userDao.save(auth);
            Teacher teacher=teacherDao.findTeacherByEmail(email);
            teacher.setStatus(true);
            teacherDao.save(teacher);
            return new ResponseEntity<>("Teacher account activated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Teacher does not exist", HttpStatus.BAD_REQUEST);
        }
    }





    /*

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/update/{nin}", method = RequestMethod.PATCH)
    public ResponseEntity<?> update(@RequestBody CitizenDto payload, @PathVariable String nin){
        Citizen citizen=citizenDao.findCitizensByNin(nin);
        System.out.println(citizen);
        System.out.println("hiiiiiiiiiiiiiiiiiiii"+payload.getFullNameAr());
        if (citizenDao.findCitizensByNin(nin)!=null){

            if(payload.getFullNameAr() == null){
                citizen.setFullNameAr(citizen.getFullNameAr());
            }
            else {
                citizen.setFullNameAr(payload.getFullNameAr());
            }
            if(payload.getFullNameLat()== null){
                citizen.setFullNameLat(citizen.getFullNameLat());
            }
            else {
                citizen.setFullNameLat(payload.getFullNameLat());
            }
            if(payload.getFather()== null){
                citizen.setFather(citizen.getFather());
            }
            else {
                citizen.setFather(payload.getFather());
            }
            if(payload.getMother()== null){
                citizen.setMother(citizen.getMother());
            }
            else {
                citizen.setMother(payload.getMother());
            }
            if(payload.getPartner()== null){
                citizen.setPartner(citizen.getPartner());
            }
            else {
                citizen.setPartner(payload.getPartner());
            }
            if(payload.getBirthdate() == null){
                citizen.setBirthdate(citizen.getBirthdate());
            }
            else {
                citizen.setBirthdate(payload.getBirthdate());
            }
            if(payload.getCommune()== null){
                citizen.setCommune(citizen.getCommune());
            }
            else {
                citizen.setCommune(payload.getCommune());
            }
            if(payload.getDaira()== null){
                citizen.setDaira(citizen.getDaira());
            }
            else {
                citizen.setDaira(payload.getDaira());
            }
            if(payload.getWilaya()== null){
                citizen.setWilaya(citizen.getWilaya());
            }
            else {
                citizen.setWilaya(payload.getWilaya());
            }
            if(payload.getNationality()== null){
                citizen.setNationality(citizen.getNationality());
            }
            else {
                citizen.setNationality(payload.getNationality());
            }
            citizenDao.save(citizen);
            return  new ResponseEntity<>("Les informations de citoyen est bien modifier!", HttpStatus.OK);}
        return new ResponseEntity<>("Citoyen n'existe pas ", HttpStatus.BAD_REQUEST);
    }

    */

/*
    @PreAuthorize("hasRole('STUDENT')")
    @RequestMapping(value="/update/{email}", method = RequestMethod.PATCH)
    public ResponseEntity<?> update(@RequestBody StudentDto payload, @PathVariable String email){
        Student student =studentDao.findStudentByEmail(email);
        System.out.println(student);
        System.out.println("hiiiiiiiiiiiiiiiiiiii"+payload.getEmail());
        if (studentDao.findStudentByEmail(email)!=null){

            if(payload.getFirstName() == null){
                student.setFirstName(student.getFirstName());
            }
            else {
                student.setLastName(payload.getLastName());
            }
            if(payload.getLastName() == null){
                student.setLastName(student.getLastName());
            }
            else {
                student.setGender(payload.getGender());
            } if(payload.getGender() == null){
                student.setGender(student.getGender());
            }
            else {
                student.setBirthdate(payload.getBirthdate());
            } if(payload.getBirthdate() == null){
                student.setBirthdate(student.getBirthdate());
            }
            else {
                student.setStatus(payload.getStatus());
            } if(payload.getStatus() == null){
                student.setStatus(student.getStatus());
            }
            else {
                student.setStatus(payload.getStatus());
            }
            studentDao.save(student);
            return  new ResponseEntity<>("Les informations  est bien modifier!", HttpStatus.OK);}
        return new ResponseEntity<>("Student n'existe pas ", HttpStatus.BAD_REQUEST);
    }




    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/delete/{nin}", method = RequestMethod.DELETE)
    public String delete(@PathVariable String nin){
        if(citizenDao.findCitizensByNin(nin)!=null){
            Auth auth=userDao.findByUsername(nin);
            Agent agent =agentDao.findAgentByNin(nin);
            if(agent != null){
                agentDao.delete(agent);
            }
            Citizen citizen =citizenDao.findCitizensByNin(nin);
            citizenDao.delete(citizen);
            userDao.delete(auth);
            return  citizen.getFullNameLat() +" est supprimer ";}
        return "Citizen n'existe pas ";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/deleteAgent/{nin}", method = RequestMethod.DELETE)
    public String deleteAgent(@PathVariable String nin) {


        Agent agent = agentDao.findAgentByNin(nin);
        Auth auth =userDao.findAuthByUsername(nin);


        if (agent != null) {


            agentDao.delete(agent);
            auth.getRoles().remove(new Role(2L, "AGENT"));
            System.out.println(auth.getRoles());
            return agent.getFullName()+" est supprimer";
        }else {
            return "Agent n'existe pas ";
        }


    }
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/updateAgent/{nin}", method = RequestMethod.PATCH)
    public String updateAgent(@RequestBody AgentDto payload, @PathVariable String nin){

        Agent agent= agentDao.findAgentByNin(nin);
        if (agent !=null) {
            if(payload.getCommune() == null) {
                agent.setCommune(agent.getCommune());
            }else {
                agent.setCommune(payload.getCommune());
            }
            if (payload.getWilaya() == null){
                agent.setWilaya(agent.getWilaya());
            }else {
                agent.setWilaya(payload.getWilaya());
            }

            agent.setNin(agent.getNin());
            agent.setFullName(agent.getFullName());
            agent.setId(agent.getId());
            agentDao.save(agent);
            return "Agent est bien modifier";
        }

        return "Agent n'existe pas ";
    }


   */




}
