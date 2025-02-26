package com.finalpos.POSsystem.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;

public class CustomerDTO {
    @JsonInclude
    private String id;
    private String name;
    private String phone;
    private String address;
    private String image;
}
