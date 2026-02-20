package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/supplier")
public class SelectSupplierController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/find")
    public List<Map<String, Object>> getSupplierByName(@RequestBody Map<String, Object> body) {

        String suppliername = (String) body.get("suppliername");

        String sql = "SELECT * FROM supplier WHERE suppliername = ?";

        return jdbcTemplate.queryForList(sql, suppliername);
    }
}
