package com.campus.eventmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendRegistrationConfirmation(String toEmail, String studentName, String eventName, int tickets, String venue, String date) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Registration Confirmed: " + eventName);
        
        String body = "Dear " + studentName + ",\n\n"
                    + "Your registration for the upcoming event is confirmed!\n\n"
                    + "Event Details:\n"
                    + "Event Name: " + eventName + "\n"
                    + "Tickets Booked: " + tickets + "\n"
                    + "Date: " + date + "\n"
                    + "Venue: " + venue + "\n\n"
                    + "Thank you for registering. We look forward to seeing you there!\n\n"
                    + "Best regards,\n"
                    + "VELTECH Event Management Team";
        
        message.setText(body);
        
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + toEmail + ": " + e.getMessage());
        }
    }
}
