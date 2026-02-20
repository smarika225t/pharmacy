package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/medication")
public class MedicationController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping
    public String createMedication(@RequestBody Map<String, Object> body) {

        Integer supplierid = (Integer) body.get("supplierid");
        String medicationame = (String) body.get("medicationame");
        String medicationcategory = (String) body.get("medicationcategory");

        BigDecimal medicationprice = new BigDecimal(body.get("medicationprice").toString());
        LocalDate medicationexpirydate = LocalDate.parse(body.get("medicationexpirydate").toString());
        Integer medicationstockqty = (Integer) body.get("medicationstockqty");

        String sql = "INSERT INTO medication " +
                "(supplierid, medicationame, medicationcategory, medicationprice, medicationexpirydate, medicationstockqty) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                supplierid,
                medicationame,
                medicationcategory,
                medicationprice,
                medicationexpirydate,
                medicationstockqty
        );

        return "Medication inserted successfully";
    }
}
