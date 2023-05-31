package edu.tum.ase.deliveryservice.service;

import edu.tum.ase.deliveryservice.model.Box;
import edu.tum.ase.deliveryservice.model.Delivery;
import edu.tum.ase.deliveryservice.model.DeliveryStatus;
import edu.tum.ase.deliveryservice.repository.BoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class BoxService {
    @Autowired
    private BoxRepository boxRepository;

    public Box createPickUpBox(String streetAddress, String raspBerryPi, String boxName) {
        return boxRepository.save (new Box (streetAddress, raspBerryPi, boxName));
    }

    public Box updatePickUpBox(String id, String boxName, String streetAddress, List<Delivery> deliveries, String raspBerryPi, String customerEmail) {
        final Box pickUpBox = boxRepository.findById (id).get ();
        pickUpBox.setBoxName (boxName);
        pickUpBox.setAddress (streetAddress);
        pickUpBox.setDeliveries (deliveries);
        pickUpBox.setRaspberryPiId (raspBerryPi);
        pickUpBox.setCustomerEmail (customerEmail);
        return boxRepository.save (pickUpBox);
    }

    public void deleteDeliveries(String id) {
        Box toUpdateBox = boxRepository.findBoxModelById (id);
        toUpdateBox.setDeliveries (Collections.emptyList ());
    }
    public void deletePickUpBox(String id) {
        boxRepository.deleteById (id);
    }

    public List <Box> getAllPickUpBoxes() {
        return boxRepository.findAll ();
    }

    public Box getPickUpBoxById(String id) {
        return boxRepository.findBoxModelById (id);
    }

    public List <Box> getPickUpBoxesByAddress(String streetAddress) {
        return boxRepository.findBoxModelsByAddress (streetAddress);
    }

    public boolean isForCustomer (String boxName, String customerEmail) {
        return Objects.equals (boxRepository.findByBoxName (boxName).getCustomerEmail (), customerEmail);
    }

    public Box addDelivery(Delivery delivery) {
        Box box = boxRepository.findByBoxName (delivery.getBoxName ());
        box.setCustomerEmail (delivery.getCustomerEmail ());
        box.addDelivery (delivery);
        return boxRepository.save (box);
    }

    public void deleteCustomerEmail(String boxName) {
        Box box = boxRepository.findByBoxName (boxName);
        box.setCustomerEmail ("");
        boxRepository.save (box);
    }

    public void updateDeliveries(String id) {
        Box box = getPickUpBoxById(id);
        List<Delivery> deliveries = box.getDeliveries ();
        deliveries.removeIf (elem -> elem.getStatus () == DeliveryStatus.COLLECTED);
        if (deliveries.size () == 0) {
            box.setCustomerEmail ("");
        }
        boxRepository.save (box);
    }
}
