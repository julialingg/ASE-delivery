package edu.tum.ase.deliveryservice.repository;

import edu.tum.ase.deliveryservice.model.AseUser;
import edu.tum.ase.deliveryservice.model.Box;
import edu.tum.ase.deliveryservice.model.Delivery;
import edu.tum.ase.deliveryservice.model.DeliveryStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DeliveryRepository extends MongoRepository<Delivery, String> {
    List<Delivery> findAllByBoxName(String boxName);
    List<Delivery> findAllByDelivererEmail(String delivererEmail);
    List<Delivery> findAllByCustomerEmail(String customerEmail);

    @Query(value = "{'customerEmail' : ?0 , 'status' : {$ne : 'DELIVERED'} }", fields = "{ 'status':0}")
    List<Delivery> findCustomerActiveDeliveries(String customerEmail);

    @Query("{'customerEmail' : ?0 , 'status' : 'DELIVERED'}")
    List<Delivery> findCustomerPastDeliveries(String customerEmail);

    Delivery findDeliveryModelById(String id);
}

