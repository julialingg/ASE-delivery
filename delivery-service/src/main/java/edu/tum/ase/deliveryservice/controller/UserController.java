package edu.tum.ase.deliveryservice.controller;

import edu.tum.ase.deliveryservice.model.AseUser;
import edu.tum.ase.deliveryservice.model.UserRole;
import edu.tum.ase.deliveryservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("")
    public List<AseUser> getAllUsers(@RequestParam(required = false) UserRole role) {
        if (role != null) {
            if (role.equals(UserRole.CUSTOMER))
                return userService.getAllCustomers();
            else if (role.equals(UserRole.DELIVERER))
                return userService.getAllDeliverers();
        }
        return userService.getAllUsers();
    }

    @GetMapping("getUser/{userEmail}")
    public AseUser getUserByEmail (@PathVariable String userEmail) {
        return userService.getUser (userEmail);
    }

    @PostMapping("")
    public AseUser createUser(@RequestBody AseUser user) {
        return userService.createUser(user);
    }

    @PutMapping("")
    public AseUser updateUser(@RequestBody AseUser user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

    @GetMapping("/csrf")
    public String csrf() throws Exception{
        return "client call this function to get your csrf token";

    }

}
