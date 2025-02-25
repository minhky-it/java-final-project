package com.finalpos.POSsystem.Entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetailEntity {
    @Id
    private String id;
    private String order_id;
    private String orderNumber;
    private ArrayList<ProductCartEntity> products;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public ArrayList<ProductCartEntity> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductCartEntity> products) {
        this.products = products;
    }
}
