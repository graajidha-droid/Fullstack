package com.campus.eventmanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.campus.eventmanagement.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByNameContainingIgnoreCaseOrDepartmentContainingIgnoreCaseOrTypeContainingIgnoreCase(
            String name, String department, String type, Pageable pageable);
}