package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Map<String, Object>> getAllSuppliers() {
        return jdbcTemplate.queryForList("SELECT * FROM supplier");
    }

    @PostMapping
    public String createSupplier(@RequestBody Map<String, Object> body) {

        String suppliername = (String) body.get("suppliername");
        String supplieraddress = (String) body.get("supplieraddress");
        Long supplierphone = Long.valueOf(body.get("supplierphone").toString());
        String companyname = (String) body.get("companyname");

        String sql = "INSERT INTO supplier (suppliername, supplieraddress, supplierphone, companyname) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                suppliername,
                supplieraddress,
                supplierphone,
                companyname
        );

        return "Supplier inserted successfully";
    }
}
