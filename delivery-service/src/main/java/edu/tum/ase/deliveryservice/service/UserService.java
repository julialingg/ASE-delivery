package edu.tum.ase.deliveryservice.service;

import edu.tum.ase.deliveryservice.model.AseUser;
import edu.tum.ase.deliveryservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    public UserRepository userRepository;

    public AseUser createUser(AseUser user) {
        return userRepository.save(user);
    }

    public AseUser updateUser(AseUser user) {
        return userRepository.save(user);
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }

    public List<AseUser> getAllUsers(){
        return userRepository.findAll();
    }

    public AseUser getUser(String userEmail) {
        return userRepository.findUserByEmail (userEmail);
    }

    //set rfidtoken

    public List<AseUser> getAllCustomers(){
        return userRepository.findAllCustomers();
    }
    public List<AseUser> getAllDeliverers(){
        return userRepository.findAllDeliverers();
    }
}
