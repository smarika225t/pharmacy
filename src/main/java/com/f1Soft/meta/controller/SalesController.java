package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping
    public String createSales(@RequestBody Map<String, Object> body) {

        Integer pharmacistid = (Integer) body.get("pharmacistid");
        Integer patientid = (Integer) body.get("patientid");
        LocalDate salesdate = LocalDate.parse(body.get("salesdate").toString());
        BigDecimal totalamount = new BigDecimal(body.get("totalamount").toString());
        String paymentmethod = (String) body.get("paymentmethod");

        String sql = "INSERT INTO sales (pharmacistid, patientid, salesdate, totalamount, paymentmethod) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                pharmacistid,
                patientid,
                salesdate,
                totalamount,
                paymentmethod
        );

        return "Sales inserted successfully";
    }
}
