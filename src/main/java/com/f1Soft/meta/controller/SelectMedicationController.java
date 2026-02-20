package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medication")
public class SelectMedicationController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/find")
    public List<Map<String, Object>> getMedicationByName(@RequestBody Map<String, Object> body) {

        String medicationame = (String) body.get("medicationame");

        String sql = "SELECT * FROM medication WHERE medicationame = ?";

        return jdbcTemplate.queryForList(sql, medicationame);
    }
}
