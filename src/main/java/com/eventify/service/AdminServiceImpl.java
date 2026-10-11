package com.eventify.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eventify.model.Admin;
import com.eventify.model.AdminRole;
import com.eventify.repository.AdminRepository;

@Service
public class AdminServiceImpl
        implements AdminService {

    private final AdminRepository adminRepository;


    public AdminServiceImpl(
            AdminRepository adminRepository) {

        this.adminRepository =
                adminRepository;
    }


    // =====================================================
    // GET ALL ADMINS
    // =====================================================

    @Override
    public List<Admin> getAllAdmins() {

        return adminRepository.findAll();
    }


    // =====================================================
    // GET ADMIN BY ID
    // =====================================================

    @Override
    public Optional<Admin> getAdminById(
            Integer id) {

        return adminRepository.findById(id);
    }


    // =====================================================
    // CREATE ADMIN
    // =====================================================

    @Override
    public Admin createAdmin(
            Admin admin) {

        // Set default role
        if (admin.getRole() == null) {

            admin.setRole(
                    AdminRole.moderator
            );
        }


        // Set creation date
        if (admin.getCreatedAt() == null) {

            admin.setCreatedAt(
                    LocalDateTime.now()
            );
        }


        return adminRepository.save(admin);
    }


    // =====================================================
    // UPDATE ADMIN
    // =====================================================

    @Override
    public Admin updateAdmin(
            Integer id,
            Admin updatedAdmin) {

        Admin admin =
                adminRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Admin not found with ID: "
                                                + id
                                )
                        );


        admin.setName(
                updatedAdmin.getName()
        );

        admin.setEmail(
                updatedAdmin.getEmail()
        );

        admin.setRole(
                updatedAdmin.getRole()
        );


        // Update password only if provided
        if (updatedAdmin.getPasswordHash() != null
                && !updatedAdmin
                        .getPasswordHash()
                        .isBlank()) {

            admin.setPasswordHash(
                    updatedAdmin.getPasswordHash()
            );
        }


        return adminRepository.save(admin);
    }


    // =====================================================
    // DELETE ADMIN
    // =====================================================

    @Override
    public void deleteAdmin(
            Integer id) {

        if (!adminRepository.existsById(id)) {

            throw new RuntimeException(
                    "Admin not found with ID: "
                            + id
            );
        }


        adminRepository.deleteById(id);
    }
}