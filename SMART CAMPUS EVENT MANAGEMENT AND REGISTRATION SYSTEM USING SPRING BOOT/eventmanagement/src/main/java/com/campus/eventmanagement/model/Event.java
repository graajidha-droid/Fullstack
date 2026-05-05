package com.campus.eventmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Event name is required")
    @Size(min = 3, max = 100, message = "Event name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Event type is required")
    private String type;

    @NotBlank(message = "Date is required")
    private String date;

    @NotBlank(message = "Venue is required")
    private String venue;

    @Min(value = 1, message = "Available seats must be at least 1")
    private int availableSeats;

    // Default constructor
    public Event() {}

    // Constructor for testing/seeding
    public Event(String name, String department, String type, String date, String venue, int availableSeats) {
        this.name = name;
        this.department = department;
        this.type = type;
        this.date = date;
        this.venue = venue;
        this.availableSeats = availableSeats;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
}