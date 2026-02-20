package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/updatesupplier")
public class UpdateSupplierController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping
    public String updateSupplier(@RequestBody Map<String, Object> body) {

        String suppliername = (String) body.get("suppliername");
        Long supplierphone = Long.valueOf(body.get("supplierphone").toString());

        String sql = "UPDATE supplier SET supplierphone = ? WHERE suppliername = ?";

        jdbcTemplate.update(sql,
                supplierphone,
                suppliername
        );

        return "Supplier updated successfully";
    }
}
