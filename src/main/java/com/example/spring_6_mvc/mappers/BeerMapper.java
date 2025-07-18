package com.example.spring_6_mvc.mappers;

import com.example.spring_6_mvc.entities.Beer;
import com.example.spring_6_mvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BeerMapper {

    BeerDTO BeerTOBeerDTO(Beer beer);

    Beer BeerDTOToBeer(BeerDTO beerDTO);
}
