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
        String patientcontact = (String) body.get("patientcontact");

        String sql = "UPDATE patient SET patientcontact = ? WHERE patientname = ?";

        jdbcTemplate.update(sql,
                patientcontact,
                patientname
        );

        return "Patient updated successfully";
    }
}
