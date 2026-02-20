package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pharmacist")
public class PharmacistController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping
    public String createPharmacist(@RequestBody Map<String, Object> body) {

        String pharmacistname = (String) body.get("pharmacistname");
        String pharmacistlicenseNo = (String) body.get("pharmacistlicenseNo");
        String pharmacistphone = (String) body.get("pharmacistphone");

        String sql = "INSERT INTO pharmacist (pharmacistname, pharmacistlicenseNo, pharmacistphone) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql,
                pharmacistname,
                pharmacistlicenseNo,
                pharmacistphone
        );

        return "Pharmacist inserted successfully";
    }
}
