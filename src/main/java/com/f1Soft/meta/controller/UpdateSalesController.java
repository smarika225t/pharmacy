package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/updatesales")
public class UpdateSalesController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping
    public String updateSales(@RequestBody Map<String, Object> body) {

        Integer salesid = (Integer) body.get("salesid");
        String paymentmethod = (String) body.get("paymentmethod");

        String sql = "UPDATE sales SET paymentmethod = ? WHERE salesid = ?";

        jdbcTemplate.update(sql,
                paymentmethod,
                salesid
        );

        return "Sales updated successfully";
    }
}
