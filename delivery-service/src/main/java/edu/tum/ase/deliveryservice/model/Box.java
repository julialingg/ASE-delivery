package edu.tum.ase.deliveryservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.List;

@Document(collection = "boxes")
public class Box {
    @Id
    private String id;
    private String address;
    @DBRef
    private List<Delivery> deliveries;
    // the one that is hardcoded into raspberryPi hardware itself
    private String raspberryPiId;
    private String boxName;

    private String customerEmail;


    public Box(String address, String raspberryPiId, String boxName) {
        this.address = address;
        this.deliveries= Collections.emptyList ();
        this.raspberryPiId=raspberryPiId;
        this.boxName= boxName;
        this.customerEmail = "";
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public List <Delivery> getDeliveries() {
        return deliveries;
    }

    public String getRaspberryPiId() {
        return raspberryPiId;
    }

    public String getBoxName() {
        return boxName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDeliveries(List <Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public void setRaspberryPiId(String raspberryPiId) {
        this.raspberryPiId = raspberryPiId;
    }

    public void addDelivery(Delivery delivery) {
        this.deliveries.add (delivery);
    }


}
