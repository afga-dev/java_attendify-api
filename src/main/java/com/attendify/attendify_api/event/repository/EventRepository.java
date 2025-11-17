package com.attendify.attendify_api.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.attendify.attendify_api.event.model.Event;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCategories_Id(Long categoryId);
}
