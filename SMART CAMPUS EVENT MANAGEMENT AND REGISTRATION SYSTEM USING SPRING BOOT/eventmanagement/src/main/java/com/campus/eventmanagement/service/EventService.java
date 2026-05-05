package com.campus.eventmanagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.campus.eventmanagement.model.Event;
import com.campus.eventmanagement.repository.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository repo;

    @Transactional
    public boolean registerForEvent(Long eventId, int tickets) {
        Event event = repo.findById(eventId).orElse(null);

        if (event == null) return false;

        if (event.getAvailableSeats() < tickets) {
            return false;
        }

        event.setAvailableSeats(event.getAvailableSeats() - tickets);
        repo.save(event);

        return true;
    }

    public List<Event> getAllEvents() {
        return repo.findAll();
    }

    public Page<Event> getEventsPaginated(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Page<Event> searchEvents(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            return repo.findAll(pageable);
        }
        return repo.findByNameContainingIgnoreCaseOrDepartmentContainingIgnoreCaseOrTypeContainingIgnoreCase(
                keyword, keyword, keyword, pageable);
    }

    public Event getEventById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Event saveEvent(Event event) {
        return repo.save(event);
    }

    public void deleteEvent(Long id) {
        repo.deleteById(id);
    }

    public long getTotalEvents() {
        return repo.count();
    }
}