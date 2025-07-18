package com.example.spring_6_mvc.controllers;

import com.example.spring_6_mvc.exceptions.NotFoundException;
import com.example.spring_6_mvc.model.BeerDTO;
import com.example.spring_6_mvc.services.BeerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/beers")
public class BeerController {
    BeerService beerService;

    /**
     * Exceptional handler at controller layer helps handle mentioned exceptions at time of api calls
     * of this controller; only api calls not method calls of this controller layer
     */
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity handleNotFoundException(){
//        System.out.println("In exception handler");
//        return new ResponseEntity(HttpStatus.NOT_FOUND);
//    }

    @GetMapping
    public Page<BeerDTO> getBeers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer offset
    ){
        return beerService.getBeers(name, price, limit, offset);
    }

    @GetMapping("/{id}")
    public BeerDTO getBeer(@PathVariable("id") UUID id) {
        System.out.println("Getting beer by id");
        return beerService.getBeer(id).orElseThrow(NotFoundException::new);
    }

    /**
     * @ Validated annotation is used to tell spring to ensure the validations defined on the method
     * parameters like BeerDTO that is why we annotated BeerDTO and not Beer so that they are validated
     * at controller layers as method parameters and thrown if validations not passed from controller only
     * and service layer is not called in that case
     */
    @PostMapping
    public ResponseEntity<HttpHeaders> createBeer(@Validated @RequestBody BeerDTO beer){
        BeerDTO createdBeer = beerService.createBeer(beer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("uuid", createdBeer.getUuid().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpHeaders> updateBeer(@Validated @PathVariable("id") UUID id, @RequestBody BeerDTO beer) {
        BeerDTO updatedBeer =  beerService.updateBeer(id, beer).orElseThrow(NotFoundException::new);
        HttpHeaders headers = new HttpHeaders();
        headers.add("uuid", updatedBeer.getUuid().toString());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public BeerDTO deleteBeer(@PathVariable("id") UUID id) {
        return beerService.deleteBeer(id).orElseThrow(NotFoundException::new);
    }
}
