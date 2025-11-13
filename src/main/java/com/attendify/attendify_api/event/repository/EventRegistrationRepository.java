package com.attendify.attendify_api.event.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.attendify.attendify_api.event.model.Event;
import com.attendify.attendify_api.event.model.EventRegistration;
import com.attendify.attendify_api.user.model.User;
import java.util.List;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    Optional<EventRegistration> findByUserAndEvent(User user, Event event);

    List<EventRegistration> findByUser(User user);

    List<EventRegistration> findByEvent(Event event);
}
