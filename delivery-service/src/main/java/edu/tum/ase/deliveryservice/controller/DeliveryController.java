package edu.tum.ase.deliveryservice.controller;

import edu.tum.ase.deliveryservice.model.Delivery;
import edu.tum.ase.deliveryservice.model.DeliveryStatus;
import edu.tum.ase.deliveryservice.service.BoxService;
import edu.tum.ase.deliveryservice.service.DeliveryService;
import edu.tum.ase.deliveryservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;
    @Autowired
    BoxController boxController;
    @Autowired
    BoxService boxService;
    @Autowired
    EmailService emailService;

    @GetMapping("")
    public List<Delivery> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }
    
    @PostMapping("")
//    @PreAuthorize("hasAuthority('DISPATCHER')")
    public Delivery createDelivery(@RequestBody Delivery delivery) {
         // if box.getCurrentCustomer() === myCustomer || null
// Delivery delivery = new Delivery(boxName,delivererEmail,customerEmail);
        // boxService.addNewDelivery(Delivery delivery)
        // send E-mail
        // generate QR code
        Delivery newDelivery = deliveryService.createDelivery (delivery);
        boxController.addDelivery (newDelivery);
        return newDelivery;
    }
    // ToDo add box ref to "clean" when all COLLECTED + add send email on customer + delete customer email
    @PutMapping("/status")
    public Delivery updateDeliveryStatus(@RequestParam String id, @RequestParam DeliveryStatus status, @RequestParam String boxId) {
        Delivery toUpdate = deliveryService.updateDeliveryStatus(id, status);
        boxService.updateDeliveries (boxId);
        String customerEmail = boxService.getPickUpBoxById (boxId).getCustomerEmail ();
        String subject = "You've collected Your delivery! Have fun:)";
        String msg = "Notification: Thank you for picking up your delivery!";
        emailService.sendEmail (customerEmail,subject,msg);
        return toUpdate;
    }

    @PutMapping("")
    public Delivery updateDelivery(@RequestBody Delivery delivery) {
        return deliveryService.updateDelivery(delivery);
    }

    @DeleteMapping("/{id}")
    public void deleteDelivery(@PathVariable String id) {
        deliveryService.deleteDelivery(id);
    }

    @GetMapping("/{id}")
    public Delivery getDeliveryById(@PathVariable String id) {
        return deliveryService.getDeliveryById (id);
    }

    @GetMapping("/deliverer/{email}")
    public List<Delivery> getDelivererDeliveries(@PathVariable String email) {
        return deliveryService.getDelivererDeliveries(email);
    }

    @GetMapping("/customer/{email}/{status}")
    public List<Delivery> getCustomerDeliveries(@PathVariable String email, @PathVariable String status) {
        return deliveryService.getCustomerDeliveries(email, status);
    }

    @PutMapping("/status/{id}")
    public Delivery updateDeliveryStatusByDeliverer(@PathVariable String id) {
        return deliveryService.updateDeliveryStatusByDeliverer(id);
    }

}
