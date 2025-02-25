package com.finalpos.POSsystem.Service.Interface;

import com.finalpos.POSsystem.Model.DTO.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

public interface AccountInterface {
    UserDTO createAdmin(String username, String email, String password, String name);
    Map<String, Object> login(String username, String password);
    UserDTO changePassword(String currentPassword, String newPassword, String confirmPassword, String token);
    UserDTO viewProfile(String token);
    UserDTO updateProfile(String name, Optional<MultipartFile> file, String token);
    Map<String, Object> loginDirectly(String token);
    UserDTO renewPassword(String password, String confirm, String token);
}
