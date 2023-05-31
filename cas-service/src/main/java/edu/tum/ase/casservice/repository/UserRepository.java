package edu.tum.ase.casservice.repository;

import edu.tum.ase.casservice.model.AseUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<AseUser, String>{
    // The MongoRepository is typed to the Document, and the type of the Document's ID
    // TODO: Create User Repository to retrieve the User from database
    AseUser getAseUserByUsername(String username);

}
