package com.finalpos.POSsystem.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;

public class OrderDTO {
    @JsonInclude
    private String id;
    private String orderNumber;
    private String customerId;
    private String staffId;
    private int taxrate;
    private int taxfee;
    private int sub_total;
    private int cash;
    private int change;
    private int total;
    private int quantity;
    private int paymentMethod;
    private String created_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public int getTaxrate() {
        return taxrate;
    }

    public void setTaxrate(int taxrate) {
        this.taxrate = taxrate;
    }

    public int getTaxfee() {
        return taxfee;
    }

    public void setTaxfee(int taxfee) {
        this.taxfee = taxfee;
    }

    public int getSub_total() {
        return sub_total;
    }

    public void setSub_total(int sub_total) {
        this.sub_total = sub_total;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
