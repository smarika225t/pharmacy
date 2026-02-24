package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Map<String, Object>> getAllPatients() {
        return jdbcTemplate.queryForList("SELECT * FROM patient");
    }

    // 1️⃣ Register New Patient
    @PostMapping("/register")
    public String registerPatient(@RequestBody Map<String, Object> body) {

        String patientname = (String) body.get("patientname");
        String patientcontact = (String) body.get("patientcontact");
        String patientemail = (String) body.get("patientemail");
        String patientaddress = (String) body.get("patientaddress");

        String sql = "INSERT INTO patient (patientname, patientcontact, patientemail, patientaddress) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                patientname,
                patientcontact,
                patientemail,
                patientaddress
        );

        return "Patient registered successfully";
    }

    // 2️⃣ View Patient History (all prescriptions)
    @PostMapping("/history")
    public List<Map<String, Object>> getPatientHistory(@RequestBody Map<String, Object> body) {

        Integer patientid = (Integer) body.get("patientid");

        String sql = "SELECT p.prescriptiondate, m.medicationame, ph.pharmacistname " +
                     "FROM prescription p " +
                     "JOIN medication m ON p.medicationid = m.medicationid " +
                     "JOIN pharmacist ph ON p.pharmacistid = ph.pharmacistid " +
                     "WHERE p.patientid = ?";

        return jdbcTemplate.queryForList(sql, patientid);
    }

    // 3️⃣ Issue New Prescription
    @PostMapping("/prescribe")
    public String issuePrescription(@RequestBody Map<String, Object> body) {

        Integer patientid = (Integer) body.get("patientid");
        Integer medicationid = (Integer) body.get("medicationid");
        Integer pharmacistid = (Integer) body.get("pharmacistid");
        String prescriptionduration = (String) body.get("prescriptionduration");

        String sql = "INSERT INTO prescription (patientid, medicationid, pharmacistid, prescriptionduration, prescriptiondate) VALUES (?, ?, ?, ?, CURDATE())";

        jdbcTemplate.update(sql,
                patientid,
                medicationid,
                pharmacistid,
                prescriptionduration
        );

        return "Prescription issued successfully";
    }

    // 4️⃣ Find Patient by Contact
    @PostMapping("/find-by-phone")
    public List<Map<String, Object>> findPatientByPhone(@RequestBody Map<String, Object> body) {

        String patientcontact = (String) body.get("patientcontact");

        String sql = "SELECT * FROM patient WHERE patientcontact = ?";

        return jdbcTemplate.queryForList(sql, patientcontact);
    }

    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable int id) {
        jdbcTemplate.update("DELETE FROM patient WHERE patientid = ?", id);
        return "Patient deleted successfully";
    }
}
