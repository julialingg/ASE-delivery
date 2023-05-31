package edu.tum.ase.deliveryservice.model;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")

public class AseUser {
    @Id
    private String id;

    @Indexed(unique = true)
    @NonNull
    private String email; //UNIQUE INDEX

    private UserRole role;

    //private String password;

    private String rfidToken;

    public AseUser(String email, UserRole role, String rfidToken){
        this.email = email;
        this.role = role;
        //this.password = password;
        this.rfidToken = rfidToken;
    }

    public String getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

//    public String getPassword(){
//        return password;
//    }

    public String getRfidToken(){
        return rfidToken;
    }

    public UserRole getRole(){return  role;}

    public void setEmail(String email){
        this.email = email;
    }

    //public void setPassword(String password){
        //this.password = password;
    //}

    public void setRole(UserRole role){
        this.role = role;
    }

    public void setRfidToken(String rfidToken){
        this.rfidToken = rfidToken;
    }
}
