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

    /**
     * to test create a select request with for update(pessimistic lock) for this id resource
     * from workbench and first disable auto commit in workbench and then call this
     * findById api from postman or scratch file that api will get blocked as the pessimistic lock won't
     * be released with commit of that txn and when you commit the txn you will see the postman api resolve
     */
    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT b FROM Beer b WHERE b.id = :id")
    Optional<Beer> findById(@Param("id") UUID id);
}