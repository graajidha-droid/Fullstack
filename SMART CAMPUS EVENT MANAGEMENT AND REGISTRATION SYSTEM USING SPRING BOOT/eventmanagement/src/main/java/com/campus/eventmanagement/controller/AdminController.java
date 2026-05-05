package com.campus.eventmanagement.controller;

import com.campus.eventmanagement.model.Admin;
import com.campus.eventmanagement.model.Event;
import com.campus.eventmanagement.registration.RegistrationRepository;
import com.campus.eventmanagement.service.AdminService;
import com.campus.eventmanagement.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EventService eventService;

    @Autowired
    private RegistrationRepository regRepo;

    @Autowired
    private AdminService adminService;

    // =========================
    // ADMIN: Dashboard
    // =========================
    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        model.addAttribute("event", new Event());
        model.addAttribute("totalEvents", eventService.getTotalEvents());
        model.addAttribute("totalRegistrations", regRepo.count());
        return "admin";
    }

    // =========================
    // ADMIN: Save Event
    // =========================
    @PostMapping("/save")
    public String saveEvent(@Valid @ModelAttribute("event") Event event, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("events", eventService.getAllEvents());
            model.addAttribute("totalEvents", eventService.getTotalEvents());
            model.addAttribute("totalRegistrations", regRepo.count());
            model.addAttribute("error", "Please fix the validation errors.");
            return "admin";
        }
        eventService.saveEvent(event);
        return "redirect:/admin?success=Event Saved";
    }

    // =========================
    // ADMIN: Delete Event
    // =========================
    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/admin?success=Event Deleted";
    }

    // =========================
    // ADMIN: View Registrations
    // =========================
    @GetMapping("/registrations")
    public String viewRegistrations(Model model) {
        model.addAttribute("regs", regRepo.findAll());
        return "registrations";
    }

    // =========================
    // ADMIN: Clear Registrations
    // =========================
    @GetMapping("/registrations/clear")
    public String clearRegistrations() {
        regRepo.deleteAll();
        return "redirect:/admin/registrations?success=All Registrations Cleared";
    }

    // =========================
    // ADMIN: Manage Admins
    // =========================
    @GetMapping("/manage-admins")
    public String manageAdmins(Model model) {
        model.addAttribute("admins", adminService.getAllAdmins());
        model.addAttribute("newAdmin", new Admin());
        return "admins";
    }

    @PostMapping("/add-admin")
    public String addAdmin(@Valid @ModelAttribute("newAdmin") Admin admin, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors() || adminService.adminExists(admin.getUsername())) {
            model.addAttribute("admins", adminService.getAllAdmins());
            model.addAttribute("error", "Username already exists or invalid data.");
            return "admins";
        }
        adminService.saveAdmin(admin);
        return "redirect:/admin/manage-admins?success=Admin Added";
    }

    @GetMapping("/delete-admin/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return "redirect:/admin/manage-admins?success=Admin Deleted";
    }
}
