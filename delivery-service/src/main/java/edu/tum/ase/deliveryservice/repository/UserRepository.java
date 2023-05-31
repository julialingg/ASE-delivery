package edu.tum.ase.deliveryservice.repository;

import edu.tum.ase.deliveryservice.model.AseUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<AseUser, String>{
    AseUser findUserByEmail(String username);
    @Query(value = "{'role':'CUSTOMER'}", fields = "{ 'email' : 1}")
    List<AseUser> findAllCustomers();

    @Query(value = "{'role':'DELIVERER'}", fields = "{ 'email' : 1}")
    List<AseUser> findAllDeliverers();

}
