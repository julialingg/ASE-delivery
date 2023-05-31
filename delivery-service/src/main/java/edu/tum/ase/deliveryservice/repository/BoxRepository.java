package edu.tum.ase.deliveryservice.repository;

import edu.tum.ase.deliveryservice.model.Address;
import edu.tum.ase.deliveryservice.model.Box;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BoxRepository extends MongoRepository<Box, String> {
    List <Box> findBoxModelsByAddress (String name);
    Box findByBoxName(String boxName);
    Box findBoxModelById(String id);

}
