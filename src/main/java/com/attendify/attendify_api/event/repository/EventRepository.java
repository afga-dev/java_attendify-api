package com.attendify.attendify_api.event.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.attendify.attendify_api.event.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Page<Event> findByCategories_Id(Long categoryId, Pageable pageable);

    @Query(value = "SELECT * FROM events WHERE event_id = :id", nativeQuery = true)
    Optional<Event> findByIdIncludingDeleted(@Param("id") Long id);

    @Query(value = "SELECT * FROM events WHERE deleted_at IS NOT NULL", nativeQuery = true)
    Page<Event> findAllDeleted(Pageable pageable);

    @Query(value = "SELECT * FROM events", nativeQuery = true)
    Page<Event> findAllIncludingDeleted(Pageable pageable);
}
