package com.attendify.attendify_api.event.repository;

import com.attendify.attendify_api.event.model.Category;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    @Query(value = "SELECT * FROM categories WHERE category_id = :id", nativeQuery = true)
    Optional<Category> findByIdIncludingDeleted(@Param("id") Long id);

    @Query(value = "SELECT * FROM categories WHERE deleted_at IS NOT NULL", nativeQuery = true)
    Page<Category> findAllDeleted(Pageable pageable);

    @Query(value = "SELECT * FROM categories", nativeQuery = true)
    Page<Category> findAllIncludingDeleted(Pageable pageable);
}
