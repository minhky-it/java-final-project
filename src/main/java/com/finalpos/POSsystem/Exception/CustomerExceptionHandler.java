package com.finalpos.POSsystem.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomerExceptionHandler {
    @ExceptionHandler(FailedException.class)
    public ResponseEntity<Object> handleDateTimeParseException(Exception e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(),HttpStatus.BAD_REQUEST), HttpStatus.OK);
    }

    // Custom response class
    static class ErrorResponse {
        private String message;
        private HttpStatus http_status;
        private int code;
        private boolean error;

        public ErrorResponse(String message, HttpStatus http_status) {
            this.message = message;
            this.error = true;
            this.http_status = http_status;
            this.code = this.http_status.value();
        }

        public String getMessage(){
            return message;
        }
        public HttpStatus getHttp_status(){
            return http_status;
        }
        public int getCode(){
            return code;
        }

        public boolean getError(){
            return error;
        }
    }
}
