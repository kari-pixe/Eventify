package com.eventify.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eventify.model.AccountStatus;
import com.eventify.model.Admin;
import com.eventify.model.Attendee;
import com.eventify.model.Organizer;
import com.eventify.repository.AdminRepository;
import com.eventify.repository.AttendeeRepository;
import com.eventify.repository.OrganizerRepository;

/**
 * Authenticates real users straight from the Attendee/Organizer/Admin tables,
 * replacing the old hardcoded demo accounts. The "username" Spring Security
 * asks for here is the user's email.
 *
 * NOTE: passwords in the DB are currently stored in plain text (matches
 * what's actually in the `password_hash` columns today). We wrap them with
 * the "{noop}" prefix so Spring Security's delegating PasswordEncoder compares
 * them as plain text instead of expecting a bcrypt hash. When real hashing is
 * added later, this wrapping can just be removed.
 */
@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final AttendeeRepository attendeeRepository;
    private final OrganizerRepository organizerRepository;
    private final AdminRepository adminRepository;

    public DatabaseUserDetailsService(
            AttendeeRepository attendeeRepository,
            OrganizerRepository organizerRepository,
            AdminRepository adminRepository) {
        this.attendeeRepository = attendeeRepository;
        this.organizerRepository = organizerRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        var attendee = attendeeRepository.findByEmail(email);
        if (attendee.isPresent()) {
            Attendee a = attendee.get();
            return User.withUsername(a.getEmail())
                    .password("{noop}" + a.getPasswordHash())
                    .roles("USER")
                    .disabled(a.getAccountStatus() == AccountStatus.SUSPENDED)
                    .build();
        }

        var organizer = organizerRepository.findByEmail(email);
        if (organizer.isPresent()) {
            Organizer o = organizer.get();
            return User.withUsername(o.getEmail())
                    .password("{noop}" + o.getPasswordHash())
                    .roles("ORGANIZER")
                    .disabled(o.getAccountStatus() == AccountStatus.SUSPENDED)
                    .build();
        }

        var admin = adminRepository.findByEmail(email);
        if (admin.isPresent()) {
            Admin ad = admin.get();
            return User.withUsername(ad.getEmail())
                    .password("{noop}" + ad.getPasswordHash())
                    .roles("ADMIN")
                    .build();
        }

        throw new UsernameNotFoundException("No account found for " + email);
    }
}
