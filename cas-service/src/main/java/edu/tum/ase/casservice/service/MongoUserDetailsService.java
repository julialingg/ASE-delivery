package edu.tum.ase.casservice.service;

import edu.tum.ase.casservice.model.AseUser;
import edu.tum.ase.casservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

// call the UserRepository
// 省去了注解：注解的大致内容是通过username去数据库里查出完整的用户信息，那么完整的用户信息应该就是UserDetails了
@Component
public class MongoUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO: Call the repository to find the user from a given username
        System.out.println("LoadByUser"+username);
        AseUser user= userRepository.getAseUserByUsername(username);
        if (user == null) {   //InternalAuthenticationServiceException  异常
//            throw new InternalAuthenticationServiceException("Not found the user!");

            throw new UsernameNotFoundException("Not found the user!");
        }

//        System.out.println(user.getUser());
        // TODO: return a Spring User with the username, password and authority that we retrieved above
        return user.getUser();   // 得到的是此用户正确的密码


    }
}
