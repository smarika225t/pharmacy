package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medication")
public class MedicationController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Map<String, Object>> getAllMedications() {
        String sql = "SELECT m.*, s.suppliername FROM medication m LEFT JOIN supplier s ON m.supplierid = s.supplierid";
        return jdbcTemplate.queryForList(sql);
    }

    @DeleteMapping("/{id}")
    public String deleteMedication(@PathVariable("id") int id) {
        jdbcTemplate.update("DELETE FROM medication WHERE medicationid = ?", id);
        return "Medication deleted successfully";
    }

    @PostMapping
    public String createMedication(@RequestBody Map<String, Object> body) {

        Integer supplierid = (Integer) body.get("supplierid");
        String medicationame = (String) body.get("medicationame");
        String medicationcategory = (String) body.get("medicationcategory");

        BigDecimal medicationprice = new BigDecimal(body.get("medicationprice").toString());
        LocalDate medicationexpdate = LocalDate.parse(body.get("medicationexpdate").toString());
        Integer medicationquantity = (Integer) body.get("medicationquantity");

        String sql = "INSERT INTO medication " +
                "(supplierid, medicationame, medicationcategory, medicationprice, medicationexpdate, medicationquantity) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                supplierid,
                medicationame,
                medicationcategory,
                medicationprice,
                medicationexpdate,
                medicationquantity
        );

        return "Medication inserted successfully";
    }
}
