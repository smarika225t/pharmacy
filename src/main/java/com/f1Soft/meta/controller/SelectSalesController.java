package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
public class SelectSalesController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/find")
    public List<Map<String, Object>> getSalesByPatient(@RequestBody Map<String, Object> body) {

        String patientname = (String) body.get("patientname");

        String sql = "SELECT s.* FROM sales s " +
                     "JOIN patient pt ON s.patientid = pt.patientid " +
                     "WHERE pt.patientname = ?";

        return jdbcTemplate.queryForList(sql, patientname);
    }
}
