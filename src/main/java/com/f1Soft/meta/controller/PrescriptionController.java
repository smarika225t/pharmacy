package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prescription")
public class PrescriptionController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Map<String, Object>> getAllPrescriptions() {
        String sql = "SELECT p.*, pt.patientname, m.medicationame, ph.pharmacistname " +
                     "FROM prescription p " +
                     "LEFT JOIN patient pt ON p.patientid = pt.patientid " +
                     "LEFT JOIN medication m ON p.medicationid = m.medicationid " +
                     "LEFT JOIN pharmacist ph ON p.pharmacistid = ph.pharmacistid";
        return jdbcTemplate.queryForList(sql);
    }

    @PostMapping
    public String createPrescription(@RequestBody Map<String, Object> body) {

        Integer patientid = (Integer) body.get("patientid");
        Integer medicationid = (Integer) body.get("medicationid");
        Integer pharmacistid = (Integer) body.get("pharmacistid");
        String prescriptionduration = (String) body.get("prescriptionduration");
        LocalDate prescriptiondate = LocalDate.parse(body.get("prescriptiondate").toString());

        String sql = "INSERT INTO prescription (patientid, medicationid, pharmacistid, prescriptionduration, prescriptiondate) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                patientid,
                medicationid,
                pharmacistid,
                prescriptionduration,
                prescriptiondate
        );

        return "Prescription inserted successfully";
    }
}
