package com.finalpos.POSsystem.Model.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finalpos.POSsystem.Entity.ProductCartEntity;

import java.util.ArrayList;

public class OrderDetailDTO {
    @JsonIgnore
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
