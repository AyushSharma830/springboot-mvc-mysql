package com.example.spring_6_mvc.services;

import com.example.spring_6_mvc.model.BeerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
//@Primary
public class BeerServiceImpl implements BeerService {

    Map<UUID, BeerDTO> beerData = new HashMap<>();

    @Override
    public Page<BeerDTO> getBeers(String name, Double price, Integer limit, Integer offset) {

        List<BeerDTO> beers = new ArrayList<>(beerData.values());

        if(name!=null && !name.isBlank() && price==null) {
            beers = beers
                    .stream()
                    .filter(beer -> beer.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        } else if((name==null || name.isBlank()) && price!=null) {
            beers = beers
                    .stream()
                    .filter(beer -> price.equals(beer.getPrice()))
                    .collect(Collectors.toList());
        } else if(name!=null && !name.isBlank() && price!=null) {
            beers = beers
                    .stream()
                    .filter(beer -> beer.getName().toLowerCase().contains(name.toLowerCase()) && price.equals(beer.getPrice()))
                    .collect(Collectors.toList());
        }

        return new PageImpl<>(beers);
    }

    @Override
    public Optional<BeerDTO> getBeer(UUID id) {
        BeerDTO beerDTO = beerData.get(id);
        return Optional.ofNullable(beerDTO);
    }

    @Override
    public BeerDTO createBeer(BeerDTO beer) {
        beer.setUuid(UUID.randomUUID());
        beerData.put(beer.getUuid(), beer);
        return beer;
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer){
        BeerDTO foundBeer = beerData.get(id);
        if(foundBeer == null) return Optional.empty();
        return Optional.ofNullable(beerData.put(id, beer));
    }

    @Override
    public Optional<BeerDTO> deleteBeer(UUID id) {
        BeerDTO foundBeer = beerData.get(id);
        if(foundBeer == null) return Optional.empty();
        beerData.remove(id);
        return Optional.of(foundBeer);
    }
}
