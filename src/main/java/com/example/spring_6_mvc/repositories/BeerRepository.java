package com.example.spring_6_mvc.repositories;

import com.example.spring_6_mvc.entities.Beer;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID> {
    Page<Beer> findAllByNameIsLikeIgnoreCase(String name, Pageable pageable);

    Page<Beer> findAllByPrice(Double price, Pageable pageable);

    Page<Beer> findAllByNameIsLikeIgnoreCaseAndPrice(String name, Double price, Pageable pageable);

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT b FROM Beer b WHERE b.id = :id")
    Optional<Beer> findById(@Param("id") UUID id);
}