package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/updateprescription")
public class UpdatePrescriptionController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping
    public String updatePrescription(@RequestBody Map<String, Object> body) {

        Integer prescriptionid = (Integer) body.get("prescriptionid");
        String prescriptionduration = (String) body.get("prescriptionduration");

        String sql = "UPDATE prescription SET prescriptionduration = ? WHERE prescriptionid = ?";

        jdbcTemplate.update(sql,
                prescriptionduration,
                prescriptionid
        );

        return "Prescription updated successfully";
    }
}
