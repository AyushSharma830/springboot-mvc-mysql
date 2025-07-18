package com.example.spring_6_mvc.repositories;

import com.example.spring_6_mvc.model.Beer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID> {
    Page<Beer> findAllByNameIsLikeIgnoreCase(String name, Pageable pageable);

    Page<Beer> findAllByPrice(Double price, Pageable pageable);

    Page<Beer> findAllByNameIsLikeIgnoreCaseAndPrice(String name, Double price, Pageable pageable);
}