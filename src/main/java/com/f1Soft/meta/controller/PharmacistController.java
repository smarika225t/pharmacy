package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pharmacist")
public class PharmacistController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Map<String, Object>> getAllPharmacists() {
        return jdbcTemplate.queryForList("SELECT * FROM pharmacist");
    }

    @PostMapping
    public String createPharmacist(@RequestBody Map<String, Object> body) {

        String pharmacistname = (String) body.get("pharmacistname");
        String pharmacistcontact = (String) body.get("pharmacistcontact");
        String pharmacistemail = (String) body.get("pharmacistemail");

        String sql = "INSERT INTO pharmacist (pharmacistname, pharmacistcontact, pharmacistemail) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql,
                pharmacistname,
                pharmacistcontact,
                pharmacistemail
        );

        return "Pharmacist inserted successfully";
    }

    @DeleteMapping("/{id}")
    public String deletePharmacist(@PathVariable int id) {
        jdbcTemplate.update("DELETE FROM pharmacist WHERE pharmacistid = ?", id);
        return "Pharmacist deleted successfully";
    }
}
