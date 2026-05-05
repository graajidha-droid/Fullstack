package com.campus.eventmanagement.controller;

import com.campus.eventmanagement.model.Event;
import com.campus.eventmanagement.registration.Registration;
import com.campus.eventmanagement.registration.RegistrationRepository;
import com.campus.eventmanagement.service.EmailService;
import com.campus.eventmanagement.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class EventController {

    @Autowired
    private EventService service;

    @Autowired
    private RegistrationRepository regRepo;

    @Autowired
    private EmailService emailService;

    // =========================
    // STUDENT: View all events with Pagination & Search
    // =========================
    @GetMapping("/")
    public String viewEvents(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(required = false) String keyword) {
        Page<Event> eventPage = service.searchEvents(keyword, PageRequest.of(page, 6));
        model.addAttribute("events", eventPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", eventPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        return "index";
    }

    // =========================
    // STUDENT: Show Register Form
    // =========================
    @GetMapping("/register/{id}")
    public String showRegisterForm(@PathVariable Long id, Model model) {
        Event event = service.getEventById(id);
        if (event == null) {
            return "redirect:/";
        }
        model.addAttribute("event", event);
        
        Registration reg = new Registration();
        reg.setEventId(id);
        model.addAttribute("registration", reg);
        
        return "register";
    }

    // =========================
    // STUDENT: Submit Registration
    // =========================
    @PostMapping("/register")
    public String registerEvent(@Valid @ModelAttribute("registration") Registration reg, BindingResult bindingResult, Model model) {
        Event event = service.getEventById(reg.getEventId());
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("event", event);
            return "register";
        }

        boolean success = service.registerForEvent(reg.getEventId(), reg.getTickets());

        if (!success) {
            model.addAttribute("event", event);
            model.addAttribute("error", "Not enough seats available!");
            return "register";
        }

        regRepo.save(reg);

        // Send confirmation email asynchronously
        emailService.sendRegistrationConfirmation(
                reg.getEmail(), 
                reg.getName(), 
                event.getName(), 
                reg.getTickets(), 
                event.getVenue(), 
                event.getDate()
        );

        return "success";
    }
}