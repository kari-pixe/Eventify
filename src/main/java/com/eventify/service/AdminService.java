package com.eventify.service;

import java.util.List;
import java.util.Optional;

import com.eventify.model.Admin;

public interface AdminService {

    List<Admin> getAllAdmins();

    Optional<Admin> getAdminById(Integer id);

    Admin createAdmin(Admin admin);

    Admin updateAdmin(
            Integer id,
            Admin updatedAdmin
    );

    void deleteAdmin(Integer id);
}