package com.f1Soft.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/inventory")
    public Map<String, Object> getInventoryStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            stats.put("totalItems", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM medication", Integer.class));
            stats.put("lowStock", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM medication WHERE medicationquantity < 20", Integer.class));
            stats.put("categories", jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT medicationcategory) FROM medication", Integer.class));
            stats.put("expiringSoon", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM medication WHERE medicationexpdate BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 90 DAY)", Integer.class));
        } catch (Exception e) {
            stats.put("totalItems", 0);
            stats.put("lowStock", 0);
            stats.put("categories", 0);
            stats.put("expiringSoon", 0);
        }
        return stats;
    }

    @GetMapping("/orders")
    public Map<String, Object> getOrderStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            stats.put("totalOrders", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sales", Integer.class));
            stats.put("totalRevenue", jdbcTemplate.queryForObject("SELECT COALESCE(SUM(salestotal), 0) FROM sales", java.math.BigDecimal.class));
        } catch (Exception e) {
            stats.put("totalOrders", 0);
            stats.put("totalRevenue", 0);
        }
        return stats;
    }

    @GetMapping("/prescriptions")
    public Map<String, Object> getPrescriptionStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            stats.put("totalPrescriptions", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM prescription", Integer.class));
        } catch (Exception e) {
            stats.put("totalPrescriptions", 0);
        }
        return stats;
    }

    @GetMapping("/reports")
    public Map<String, Object> getReportStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            stats.put("totalRevenue", jdbcTemplate.queryForObject("SELECT COALESCE(SUM(salestotal), 0) FROM sales", java.math.BigDecimal.class));
            stats.put("totalOrders", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sales", Integer.class));
            stats.put("totalCustomers", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM patient", Integer.class));
            stats.put("totalPrescriptions", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM prescription", Integer.class));
        } catch (Exception e) {
            stats.put("totalRevenue", 0);
            stats.put("totalOrders", 0);
            stats.put("totalCustomers", 0);
            stats.put("totalPrescriptions", 0);
        }

        // Top selling - medication with most prescriptions
        try {
            List<Map<String, Object>> topProducts = jdbcTemplate.queryForList(
                "SELECT m.medicationame, m.medicationcategory, COUNT(*) as total_sold " +
                "FROM prescription p JOIN medication m ON p.medicationid = m.medicationid " +
                "GROUP BY m.medicationid, m.medicationame, m.medicationcategory " +
                "ORDER BY total_sold DESC LIMIT 5"
            );
            stats.put("topProducts", topProducts);
        } catch (Exception e) {
            stats.put("topProducts", new java.util.ArrayList<>());
        }

        return stats;
    }
}
