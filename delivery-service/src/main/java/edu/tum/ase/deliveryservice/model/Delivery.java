package edu.tum.ase.deliveryservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "deliveries")
public class Delivery {
    @Id
    private String id;
    private String boxName;
    private String delivererEmail;
    private String customerEmail;

    private DeliveryStatus status;


    public Delivery(String boxName, String delivererEmail, String customerEmail, DeliveryStatus status) {
        this.boxName = boxName;
        this.delivererEmail = delivererEmail;
        this.customerEmail = customerEmail;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getBoxName() {
        return boxName;
    }

    public String getDelivererEmail() {
        return delivererEmail;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }


    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public void setDelivererEmail(String delivererEmail) {
        this.delivererEmail = delivererEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public DeliveryStatus getStatus() {
       return status;
    }
}



