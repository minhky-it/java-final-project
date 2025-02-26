package com.finalpos.POSsystem.Service.Interface;

import com.finalpos.POSsystem.Model.DTO.CustomerDTO;

import java.util.List;
import java.util.Map;

public interface CustomerInterface {
    Map<String, Object> customers(int page, int size);
    CustomerDTO detail(String id);
    Map<String, Object> transactions(String id, int page, int size);
}
