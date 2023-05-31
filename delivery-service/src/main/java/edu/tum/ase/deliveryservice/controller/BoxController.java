package edu.tum.ase.deliveryservice.controller;

import edu.tum.ase.deliveryservice.model.Box;
import edu.tum.ase.deliveryservice.model.Delivery;
import edu.tum.ase.deliveryservice.service.BoxService;
import edu.tum.ase.deliveryservice.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/box")
public class BoxController {

    @Autowired
    BoxService boxService;
    @Autowired
    DeliveryService deliveryService;

    @PostMapping("")
    public Box createPickUpBox(@RequestBody Box box) {
        return boxService.createPickUpBox (box.getAddress (), box.getRaspberryPiId (), box.getBoxName ());
    }

    @PutMapping("")
    public Box updatePickUpBox(@RequestBody Box box) {
        Box boxToUpdate = boxService.getPickUpBoxById(box.getId ());
        deliveryService.updateTargetBox (boxToUpdate.getBoxName (), box.getBoxName ());
        return boxService.updatePickUpBox (box.getId (), box.getBoxName (), box.getAddress (), box.getDeliveries (), box.getRaspberryPiId (), box.getCustomerEmail ());
    }

    @DeleteMapping("/{id}")
    public void deletePickUpBox(@PathVariable String id) {
        boxService.deletePickUpBox (id);
    }

    @DeleteMapping("emptyDeliveries/{id}")
        public void deleteDeliveries (@PathVariable String id) {
            boxService.deleteDeliveries (id);
        }

    // return all 
    @GetMapping("")
    public List <Box> getAllPickUpBoxes() {
        return boxService.getAllPickUpBoxes ();
    }

    @GetMapping("/{id}")
    public Box getPickUpBoxesById(@PathVariable String id) {
        return boxService.getPickUpBoxById (id);
    }

    @GetMapping("/getPickUpBoxesByAddress/{streetAddress}")
    public List <Box> getPickUpBoxesByAddress(@PathVariable String streetAddress) {
        return boxService.getPickUpBoxesByAddress (streetAddress);
    }

    @GetMapping("/isForCustomer/{boxName}/{customerEmail}")
    public boolean isForCustomer(@PathVariable String customerEmail, @PathVariable String boxName) {
        return boxService.isForCustomer(boxName, customerEmail);
    }

    @PutMapping("/addDelivery")
    public Box addDelivery(@RequestBody Delivery delivery) {
        return boxService.addDelivery(delivery);
    }

    // ToDo add a function that checks whether there is already such boxName to avoid duplications when creating a new box
    // Dont write the function just call getPickUpBoxes by name and expect null
}
