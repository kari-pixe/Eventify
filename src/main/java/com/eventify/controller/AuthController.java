package com.eventify.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventify.model.AccountStatus;
import com.eventify.model.Admin;
import com.eventify.model.Attendee;
import com.eventify.model.AuthResponseDTO;
import com.eventify.model.LoginRequestDTO;
import com.eventify.model.Organizer;
import com.eventify.model.RegisterRequestDTO;
import com.eventify.repository.AdminRepository;
import com.eventify.repository.AttendeeRepository;
import com.eventify.repository.OrganizerRepository;

@RestController
@RequestMapping("/eventify/auth")
public class AuthController {

    private final AttendeeRepository attendeeRepository;
    private final OrganizerRepository organizerRepository;
    private final AdminRepository adminRepository;

    public AuthController(
            AttendeeRepository attendeeRepository,
            OrganizerRepository organizerRepository,
            AdminRepository adminRepository) {
        this.attendeeRepository = attendeeRepository;
        this.organizerRepository = organizerRepository;
        this.adminRepository = adminRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {

        if (request.getEmail() == null || request.getPassword() == null || request.getRole() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "email, password and role are all required"));
        }

        String role = request.getRole().toUpperCase();

        switch (role) {

            case "ATTENDEE": {
                Optional<Attendee> found = attendeeRepository.findByEmail(request.getEmail());
                if (found.isEmpty() || !found.get().getPasswordHash().equals(request.getPassword())) {
                    return unauthorized();
                }
                Attendee a = found.get();
                if (a.getAccountStatus() == AccountStatus.SUSPENDED) {
                    return suspended();
                }
                return ResponseEntity.ok(toDto(a));
            }

            case "ORGANIZER": {
                Optional<Organizer> found = organizerRepository.findByEmail(request.getEmail());
                if (found.isEmpty() || !found.get().getPasswordHash().equals(request.getPassword())) {
                    return unauthorized();
                }
                Organizer o = found.get();
                if (o.getAccountStatus() == AccountStatus.SUSPENDED) {
                    return suspended();
                }
                return ResponseEntity.ok(toDto(o));
            }

            case "ADMIN": {
                Optional<Admin> found = adminRepository.findByEmail(request.getEmail());
                if (found.isEmpty() || !found.get().getPasswordHash().equals(request.getPassword())) {
                    return unauthorized();
                }
                return ResponseEntity.ok(toDto(found.get()));
            }

            default:
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "role must be ATTENDEE, ORGANIZER, or ADMIN"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {

        if (request.getName() == null || request.getEmail() == null
                || request.getPassword() == null || request.getRole() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "name, email, password and role are all required"));
        }

        String role = request.getRole().toUpperCase();

        switch (role) {

            case "ATTENDEE": {
                if (attendeeRepository.findByEmail(request.getEmail()).isPresent()) {
                    return emailTaken();
                }
                Attendee a = new Attendee();
                a.setName(request.getName());
                a.setEmail(request.getEmail());
                a.setPhone(request.getPhone());
                a.setPasswordHash(request.getPassword());
                a.setAccountStatus(AccountStatus.ACTIVE);
                a.setCreatedAt(LocalDateTime.now());
                attendeeRepository.save(a);
                return ResponseEntity.status(HttpStatus.CREATED).body(toDto(a));
            }

            case "ORGANIZER": {
                if (organizerRepository.findByEmail(request.getEmail()).isPresent()) {
                    return emailTaken();
                }
                Organizer o = new Organizer();
                o.setName(request.getName());
                o.setEmail(request.getEmail());
                o.setPhone(request.getPhone());
                o.setOrganizationName(request.getOrganizationName());
                o.setPasswordHash(request.getPassword());
                o.setAccountStatus(AccountStatus.ACTIVE);
                o.setCreatedAt(LocalDateTime.now());
                organizerRepository.save(o);
                return ResponseEntity.status(HttpStatus.CREATED).body(toDto(o));
            }

            default:
                // Admin accounts are provisioned manually, not through public self-registration.
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "role must be ATTENDEE or ORGANIZER to register"));
        }
    }

    private ResponseEntity<?> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid email or password"));
    }

    private ResponseEntity<?> suspended() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "This account has been suspended"));
    }

    private ResponseEntity<?> emailTaken() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "An account with this email already exists"));
    }

    private AuthResponseDTO toDto(Attendee a) {
        AuthResponseDTO dto = new AuthResponseDTO();
        dto.setId("attendee-" + a.getAttendeeId());
        dto.setName(a.getName());
        dto.setEmail(a.getEmail());
        dto.setPhone(a.getPhone());
        dto.setRole("ATTENDEE");
        dto.setStatus(a.getAccountStatus() != null ? a.getAccountStatus().name() : "ACTIVE");
        dto.setCreatedAt(a.getCreatedAt() != null ? a.getCreatedAt().toString() : null);
        return dto;
    }

    private AuthResponseDTO toDto(Organizer o) {
        AuthResponseDTO dto = new AuthResponseDTO();
        dto.setId("organizer-" + o.getOrganizerId());
        dto.setName(o.getName());
        dto.setEmail(o.getEmail());
        dto.setPhone(o.getPhone());
        dto.setRole("ORGANIZER");
        dto.setOrganization(o.getOrganizationName());
        dto.setStatus(o.getAccountStatus() != null ? o.getAccountStatus().name() : "ACTIVE");
        dto.setCreatedAt(o.getCreatedAt() != null ? o.getCreatedAt().toString() : null);
        return dto;
    }

    private AuthResponseDTO toDto(Admin a) {
        AuthResponseDTO dto = new AuthResponseDTO();
        dto.setId("admin-" + a.getAdminId());
        dto.setName(a.getName());
        dto.setEmail(a.getEmail());
        dto.setRole("ADMIN");
        dto.setStatus("ACTIVE");
        dto.setCreatedAt(a.getCreatedAt() != null ? a.getCreatedAt().toString() : null);
        return dto;
    }
}
