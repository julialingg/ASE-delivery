package edu.tum.ase.casservice.service;

import edu.tum.ase.casservice.model.AseUser;
import edu.tum.ase.casservice.model.UserRole;
import edu.tum.ase.casservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static edu.tum.ase.casservice.model.UserRole.DISPATCHER;

@Service
public class UserService {
    @Autowired
    public UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

    public AseUser createUser(AseUser user){
        String uName = user.getUsername();
        String uPwd = user.getPassword();
        String msg = "User " + uName
                    + " created , User password is " + uPwd + ".";
        emailService.sendEmail(uName, msg);

        String pwd = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(pwd);

        return userRepository.insert(user);
    }
    // test
    public AseUser createUserForTest(String username, String password, UserRole role) {
        return userRepository.save(new AseUser(username, password, role));
    }

    public AseUser updateUser(AseUser user){
        String pwd = user.getPassword();
        if(pwd == "") {
            final AseUser dbuser = userRepository.findById(user.getId()).get();
            pwd = dbuser.getPassword();
        }
        else {
            pwd = bCryptPasswordEncoder.encode(pwd);
        }
        user.setPassword(pwd);
        return userRepository.save(user);
        //user.setUsername(username);
        // user.setPassword(password);
        // user.setRole(role);
        // user.setRfidToken(rfidToken);
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }

    public List<AseUser> getAllUsers(){
        return userRepository.findAll();
    }

}
