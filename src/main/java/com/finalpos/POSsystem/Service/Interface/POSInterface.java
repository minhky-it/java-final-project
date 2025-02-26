package com.finalpos.POSsystem.Service.Interface;

import com.finalpos.POSsystem.Controller.POSController;
import com.finalpos.POSsystem.Model.DTO.CustomerDTO;
import com.finalpos.POSsystem.Model.DTO.OrderDTO;
import com.finalpos.POSsystem.Model.DTO.ProductDTO;

import java.util.List;

public interface POSInterface {
    CustomerDTO customer(String phone);
    CustomerDTO createCustomer(String phone, String name, String address);
    OrderDTO createBill(POSController.PaymentModel payment);
    ProductDTO searchProduct(String barcode);
    List<ProductDTO> products(String terms);
}
