package com.example.demo1.module.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class AddOrUpdateCoachDTO {
    private BigInteger id;
    private String pics;
    private String name;
    private String speciality;
    private String intro;
}
