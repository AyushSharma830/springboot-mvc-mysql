package com.example.spring_6_mvc.services;

import com.example.spring_6_mvc.model.BeerDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Page<BeerDTO> getBeers(String name, Double price, Integer limit, Integer offset);

    Optional<BeerDTO> getBeer(UUID id);

    BeerDTO createBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer);

    Optional<BeerDTO> deleteBeer(UUID id);
}
