package edu.tum.ase.deliveryservice.service;

import edu.tum.ase.deliveryservice.model.Box;
import edu.tum.ase.deliveryservice.model.Delivery;
import edu.tum.ase.deliveryservice.model.DeliveryStatus;
import edu.tum.ase.deliveryservice.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private BoxService boxService;

    @Autowired
    private EmailService emailService;


    public Delivery createDelivery(Delivery delivery){
       delivery.setStatus(DeliveryStatus.CREATED);

       Delivery newDelivery = deliveryRepository.insert(delivery);
       String uName = newDelivery.getCustomerEmail();
       String uId = newDelivery.getId();
       String uBoxName = newDelivery.getBoxName();
       String Subject = "New delivery created";
       String msg = "Notification: You have a new delivery, it will be placed in box: " + uBoxName
               + ". Check with your delivery ID:" + uId + ".";
       emailService.sendEmail(uName, Subject, msg);

       return  newDelivery;
    }

    public Delivery updateDeliveryStatus(String id, DeliveryStatus status) {
        final Delivery delivery = deliveryRepository.findById(id).get();
        delivery.setStatus(status);
        return deliveryRepository.save(delivery);
    }
    public Delivery updateDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    public void deleteDelivery(String id) {
        String boxName = deliveryRepository.findById (id).get ().getBoxName ();
        if (deliveryRepository.findAllByCustomerEmail (boxName).size () <= 1) {
            boxService.deleteCustomerEmail (boxName);
        }
        deliveryRepository.deleteById(id);
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public void updateTargetBox(String oldBoxName, String boxName) {
        deliveryRepository.findAllByBoxName (oldBoxName).forEach (delivery -> {
            delivery.setBoxName (boxName);
            deliveryRepository.save (delivery);
        });
    }

    public Delivery getDeliveryById(String id) {
        return deliveryRepository.findDeliveryModelById (id);
    }


    public List<Delivery> getDelivererDeliveries(String delivererEmail) {
        return deliveryRepository.findAllByDelivererEmail(delivererEmail);
    }

    public List<Delivery> getCustomerDeliveries(String customerEmail, String status) {
        if (status.equals("active")) return deliveryRepository.findCustomerActiveDeliveries(customerEmail);
        else return deliveryRepository.findCustomerPastDeliveries(customerEmail);
    }

    public Delivery updateDeliveryStatusByDeliverer(String id) {
        final Delivery delivery = deliveryRepository.findById(id).get();
        String uName = delivery.getCustomerEmail();
        String uBoxName = delivery.getBoxName();
        DeliveryStatus currentStatus = delivery.getStatus();
        DeliveryStatus nextStatus = currentStatus;
        if (currentStatus.equals(DeliveryStatus.CREATED)) {
            nextStatus = DeliveryStatus.COLLECTED;
        } else if (currentStatus.equals(DeliveryStatus.COLLECTED)){
            nextStatus = DeliveryStatus.PLACED;
            String Subject = "New delivery placed";
            String msg = "Notification: A new delivery has been placed in your box: " + uBoxName + ".";
            emailService.sendEmail(uName, Subject, msg);
        }
        delivery.setStatus(nextStatus);
        return deliveryRepository.save(delivery);
    }
}
