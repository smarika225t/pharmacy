package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/updatepharmacist")
public class UpdatePharmacistController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping
    public String updatePharmacist(@RequestBody Map<String, Object> body) {

        String pharmacistname = (String) body.get("pharmacistname");
        String pharmacistphone = (String) body.get("pharmacistphone");

        String sql = "UPDATE pharmacist SET pharmacistphone = ? WHERE pharmacistname = ?";

        jdbcTemplate.update(sql,
                pharmacistphone,
                pharmacistname
        );

        return "Pharmacist updated successfully";
    }
}
