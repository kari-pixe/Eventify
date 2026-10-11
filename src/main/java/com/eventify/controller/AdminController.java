package com.eventify.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventify.model.Admin;
import com.eventify.service.AdminService;

@RestController
@RequestMapping("/eventify/admin")
public class AdminController {

    private final AdminService adminService;


    public AdminController(
            AdminService adminService) {

        this.adminService =
                adminService;
    }


    // =====================================================
    // GET ALL ADMINS
    // =====================================================

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Admin> getAllAdmins() {

        return adminService
                .getAllAdmins();
    }


    // =====================================================
    // GET ADMIN BY ID
    // =====================================================

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(
            @PathVariable Integer id) {

        return adminService
                .getAdminById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }


    // =====================================================
    // CREATE ADMIN
    // =====================================================

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Admin createAdmin(
            @RequestBody Admin admin) {

        return adminService
                .createAdmin(admin);
    }


    // =====================================================
    // UPDATE ADMIN
    // =====================================================

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Admin updateAdmin(
            @PathVariable Integer id,
            @RequestBody Admin admin) {

        return adminService
                .updateAdmin(
                        id,
                        admin
                );
    }


    // =====================================================
    // DELETE ADMIN
    // =====================================================

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(
            @PathVariable Integer id) {

        adminService
                .deleteAdmin(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}