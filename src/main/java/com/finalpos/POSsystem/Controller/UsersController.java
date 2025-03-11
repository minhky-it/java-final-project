package com.finalpos.POSsystem.Controller;

import com.finalpos.POSsystem.Exception.FailedException;
import com.finalpos.POSsystem.Exception.ResponseHandler;
import com.finalpos.POSsystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



@Controller
@RestController
@ResponseBody
@RequestMapping("/api/users")

public class UsersController {
    @Autowired
    UserService service;
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @GetMapping("/")
    //    Get list of users
    public ResponseEntity<?> index(@RequestParam Optional<String> page) {
        try {
            int pageNumber = Integer.parseInt(page.orElse("1"));
            return ResponseHandler.builder("Ok", service.users(pageNumber,10));
        }
        catch (Exception e) {
            throw new FailedException("Failed at user controller: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("name") String name,
                            @RequestParam("email") String email){
        try{
            if (!isValidEmail(email))
                return ResponseHandler.failed("Invalid email format", HttpStatus.BAD_REQUEST);

            return ResponseHandler.builder("OK", register(name, email));
        } catch (Exception e) {
            throw new FailedException("Failed at product controller: " + e.getMessage());
        }
    }


    @PostMapping("/resend")
    public ResponseEntity<?> resendEmail(@RequestParam("email") String email) {
        try {
            return ResponseHandler.builder("OK", service.resendEmail(email));
        } catch (Exception e) {
            throw new FailedException("Failed at user controller: " + e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> update(@PathVariable("userId") String userId,
                          @RequestParam String email,
                          @RequestParam String role,
                          @RequestParam String status){
        try {
            return ResponseHandler.builder("OK", service.update(userId, email, role, status));
        }
        catch (Exception e){
            throw new FailedException("Failed at user controller: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable("userId") String userId){
        try {
            return ResponseHandler.builder("OK", service.delete(userId));
        }
        catch (Exception e){
            throw new FailedException("Failed at user controller: " + e.getMessage());
        }
    }


    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}