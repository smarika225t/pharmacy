package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Map<String, Object>> getAllSales() {
        String sql = "SELECT s.*, pt.patientname, ph.pharmacistname " +
                     "FROM sales s " +
                     "LEFT JOIN patient pt ON s.patientid = pt.patientid " +
                     "LEFT JOIN pharmacist ph ON s.pharmacistid = ph.pharmacistid";
        return jdbcTemplate.queryForList(sql);
    }

    @PostMapping
    public String createSales(@RequestBody Map<String, Object> body) {

        Integer patientid = (Integer) body.get("patientid");
        Integer pharmacistid = (Integer) body.get("pharmacistid");
        LocalDate salesdate = LocalDate.parse(body.get("salesdate").toString());
        Integer salesquantity = Integer.valueOf(body.get("salesquantity").toString());
        BigDecimal salestotal = new BigDecimal(body.get("salestotal").toString());

        String sql = "INSERT INTO sales (patientid, pharmacistid, salesdate, salesquantity, salestotal) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                patientid,
                pharmacistid,
                salesdate,
                salesquantity,
                salestotal
        );

        return "Sales inserted successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteSales(@PathVariable int id) {
        jdbcTemplate.update("DELETE FROM sales WHERE salesid = ?", id);
        return "Sale deleted successfully";
    }
}
