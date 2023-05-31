package edu.tum.ase.casservice.model;
import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Document(collection = "users")

public class AseUser {
    @Id
    private String id;

    @Indexed(unique = true)
    @NonNull
    private String username; //UNIQUE INDEX

    private UserRole role;

    private String password;
    private User user;


    // private String rfidToken;

    public AseUser(String username, String password,UserRole role){
        this.username = username;
        this.role = role;
        this.password = password;
        //  this.rfidToken = rfidToken;
    }

    public String getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

//    public String getRfidToken(){
//        return rfidToken;
//    }

    public UserRole getRole(){return  role;}

    //security.core.GrantedAuthority
    //是封装了权限信息的Authentication对象（即对象的Collection<? extends GrantedAuthority>属性被赋值 ）
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority auth=new SimpleGrantedAuthority(role.toString());
        return List.of(auth);  //SimpleGrantedAuthority存储授予Authentication对象的权限的String表示形式
    }

    //userDetails   for MongoUserDetailsService
    public User getUser() {
        User user= new User(username,password,getAuthorities());  //authority that we retrieved according the role
        return user;

    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setRole(UserRole role){
        this.role = role;
    }

    //public void setRfidToken(String rfidToken){
        //this.rfidToken = rfidToken;
    //}

    public void setUser(User user) {
        this.user = user;
    }

}
