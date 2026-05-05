package com.campus.eventmanagement.config;

import com.campus.eventmanagement.model.Admin;
import com.campus.eventmanagement.model.Event;
import com.campus.eventmanagement.repository.AdminRepository;
import com.campus.eventmanagement.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Super Admin");
            adminRepository.save(admin);
            System.out.println("Default Admin User created: admin / admin123");
        }

        if (eventRepository.count() == 0) {
            eventRepository.save(new Event("Tech Symposium 2026", "Computer Science", "Conference", "2026-06-15", "Main Auditorium", 500));
            eventRepository.save(new Event("Annual Sports Meet", "Physical Education", "Sports", "2026-07-20", "College Ground", 1000));
            eventRepository.save(new Event("Robotics Workshop", "Electrical Eng", "Workshop", "2026-05-10", "Lab 4B", 50));
            eventRepository.save(new Event("Cultural Fest - Utsav", "Arts", "Festival", "2026-08-05", "Open Air Theatre", 2000));
            eventRepository.save(new Event("AI/ML Bootcamp", "Computer Science", "Workshop", "2026-06-01", "Lab 1A", 40));
            System.out.println("Dummy events populated.");
        }
    }
}
