package com.finalpos.POSsystem.Service.Interface;

import com.finalpos.POSsystem.Model.DTO.UserDTO;

import java.util.Map;

public interface UserInterface {
    Map<String, Object> users(int page, int size);
    UserDTO register(String name, String email);
    String resendEmail(String email);
    UserDTO update(String userId, String email, String role, String status);
    UserDTO delete(String userId);
}
