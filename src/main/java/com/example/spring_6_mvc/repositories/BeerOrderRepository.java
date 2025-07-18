package com.example.spring_6_mvc.repositories;

import com.example.spring_6_mvc.entities.BeerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID> {
}
