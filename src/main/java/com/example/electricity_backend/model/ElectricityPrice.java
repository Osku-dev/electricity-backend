package com.example.electricity_backend.model;

import lombok.Data;

@Data
public class ElectricityPrice {
    private String startDate;
    private String endDate;
    private double price;
}
