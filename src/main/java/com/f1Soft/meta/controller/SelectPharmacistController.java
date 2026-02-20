package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pharmacist")
public class SelectPharmacistController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/find")
    public List<Map<String, Object>> getPharmacistByName(@RequestBody Map<String, Object> body) {

        String pharmacistname = (String) body.get("pharmacistname");

        String sql = "SELECT * FROM pharmacist WHERE pharmacistname = ?";

        return jdbcTemplate.queryForList(sql, pharmacistname);
    }
}
