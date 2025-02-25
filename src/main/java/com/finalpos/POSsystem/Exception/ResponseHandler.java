package com.finalpos.POSsystem.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> builder(String message, Object resObject){
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("code",200);
        response.put("data", resObject);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static ResponseEntity<Object> failed(String message, HttpStatus status){
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("http_status", status);
        response.put("error", true);
        response.put("code", status.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
