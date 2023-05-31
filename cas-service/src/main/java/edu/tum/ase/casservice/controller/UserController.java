package edu.tum.ase.casservice.controller;

import edu.tum.ase.casservice.model.AseUser;
import edu.tum.ase.casservice.model.UserRole;
import edu.tum.ase.casservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/ase-user")

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*",allowCredentials = "true")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("")
    public List<AseUser> getAllUsers(){
        return userService.getAllUsers();
    }

    // test authority for dispatcher
//    @PostMapping("")
//    @PreAuthorize("hasAuthority('DISPATCHER')")     // OK !
//    public String createProject(@RequestBody String name) {   // postman  use raw json
//
////        System.out.println(name); // {name:"zl"}
//        return name;
//
//    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('DISPATCHER')")
    public AseUser createUser(@RequestBody AseUser user){
        return userService.createUser(user);
    }

    @PutMapping("")
    @PreAuthorize("hasAuthority('DISPATCHER')")
    public AseUser updateUser(@RequestBody AseUser user){
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id){
        userService.deleteUser(id);
    }
}
