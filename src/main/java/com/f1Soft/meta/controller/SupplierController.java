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
        String suppliercontact = (String) body.get("suppliercontact");
        String supplieremail = (String) body.get("supplieremail");

        String sql = "INSERT INTO supplier (suppliername, suppliercontact, supplieremail) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql,
                suppliername,
                suppliercontact,
                supplieremail
        );

        return "Supplier inserted successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteSupplier(@PathVariable int id) {
        jdbcTemplate.update("DELETE FROM supplier WHERE supplierid = ?", id);
        return "Supplier deleted successfully";
    }
}
