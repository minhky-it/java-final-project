package com.finalpos.POSsystem.Controller;

import com.fasterxml.jackson.databind.JsonNode;

import com.finalpos.POSsystem.Exception.FailedException;
import com.finalpos.POSsystem.Exception.ResponseHandler;
import com.finalpos.POSsystem.Model.DTO.UserDTO;
import com.finalpos.POSsystem.Service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RestController
@RequestMapping("/api/account")
@ResponseBody
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping("/register_admin")
    public ResponseEntity<?> register_admin(@RequestBody(required = false) JsonNode body) {
        try{
            String username = body.get("username").asText();
            String email = body.get("email").asText();
            String password = body.get("password").asText();
            String name = body.get("name").asText();

            UserDTO user = accountService
                    .createAdmin(username,email,password,name);
            return ResponseHandler.builder("Register successfully", user);
        }
        catch (Exception e){
            return ResponseHandler.failed(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody(required = false) JsonNode body) {
        try {
            String username = body.get("username").asText();
            String password = body.get("password").asText();

            return ResponseHandler.builder("OK", accountService.login(username, password));

        } catch (Exception e) {
            throw new FailedException("Failed at account controller: " + e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody JsonNode body,
            @RequestHeader(name = "Authorization") String token){
        try{
            String currentPassword = body.get("currentPassword").asText();
            String newPassword = body.get("newPassword").asText();
            String confirmPassword = body.get("confirmPassword").asText();
            return ResponseHandler
                    .builder("OK", accountService
                    .changePassword(currentPassword, newPassword, confirmPassword, token));
        }catch (Exception e){
            throw new FailedException("Failed to change password at Account controller: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> profile(@RequestHeader("Authorization") String token){
        try {
            return ResponseHandler.builder("OK", accountService.viewProfile(token));
        } catch (Exception e){
            throw new FailedException("Failed to get data: " + e.getMessage());
        }
    }

    @PatchMapping("/")
    public ResponseEntity<?> updateProfile(@RequestParam("name") String name,
                                 @RequestParam("file") Optional<MultipartFile> multipartFile,
                                 @RequestHeader("Authorization") String token) {
        try {
            return ResponseHandler
                    .builder("OK", accountService
                    .updateProfile(name, multipartFile, token));
        }catch (Exception e){
           throw new FailedException("Failed to update profile information" + e.getMessage(), null);
        }
    }

    @PostMapping("/direct")
    public ResponseEntity<?> direct(@RequestParam("token") String token){
        try{
            return ResponseHandler.builder("OK", accountService.loginDirectly(token));
        }catch (Exception e){
            throw new FailedException("Failed to login directly: " + e.getMessage());
        }
    }

    @PutMapping("/renew-password")
    public ResponseEntity<?> renewPassword(@RequestHeader("Authorization") String token,
                                 @RequestBody JsonNode body){
        try{
            String password = body.get("password").asText();
            String confirm = body.get("password").asText();
            return ResponseHandler
                    .builder("OK", accountService
                            .renewPassword(password, confirm, token));
        }catch (Exception e){
            throw new FailedException("Failed to renew password: " + e.getMessage());
        }
    }





}
