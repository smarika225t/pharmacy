package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
public class SelectPatientController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/find")
    public List<Map<String, Object>> getPatientByName(@RequestBody Map<String, Object> body) {

        String patientname = (String) body.get("patientname");

        String sql = "SELECT * FROM patient WHERE patientname = ?";

        return jdbcTemplate.queryForList(sql, patientname);
    }
}
