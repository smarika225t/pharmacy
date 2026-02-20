package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/updatepatient")
public class UpdatePatientController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping
    public String updatePatient(@RequestBody Map<String, Object> body) {

        String patientname = (String) body.get("patientname");
        String patientphone = (String) body.get("patientphone");

        String sql = "UPDATE patient SET patientphone = ? WHERE patientname = ?";

        jdbcTemplate.update(sql,
                patientphone,
                patientname
        );

        return "Patient updated successfully";
    }
}
