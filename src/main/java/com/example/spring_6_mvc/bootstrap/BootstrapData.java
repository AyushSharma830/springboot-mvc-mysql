package com.example.spring_6_mvc.bootstrap;

import com.example.spring_6_mvc.mappers.BeerMapper;
import com.example.spring_6_mvc.entities.Beer;
import com.example.spring_6_mvc.model.BeerCSVRecord;
import com.example.spring_6_mvc.repositories.BeerRepository;
import com.example.spring_6_mvc.services.BeerCSVParser;
import com.example.spring_6_mvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final BeerRepository beerRepository;
    private final BeerService beerService;
    private final BeerMapper beerMapper;
    private final BeerCSVParser beerCSVParser;

    @Override
    public void run(String... args) throws Exception {
        beerRepository.deleteAll();

        Beer b1 = Beer.builder()
                .name("B1")
                .price(12.0)
                .build();

        Beer b2 = Beer.builder()
                .name("B2")
                .price(13.0)
                .build();

        Beer b3 = Beer.builder()
                .name("B3")
                .price(14.0)
                .build();

        beerRepository.save(b1);
        beerRepository.save(b2);
        beerRepository.save(b3);

//        beerService.createBeer(beerMapper.BeerTOBeerDTO(b1));
//        beerService.createBeer(beerMapper.BeerTOBeerDTO(b2));
//        beerService.createBeer(beerMapper.BeerTOBeerDTO(b3));

        loadCSVData();
    }

    private void loadCSVData() {
        try{
            File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

            List<BeerCSVRecord> beers = beerCSVParser.parseCsv(file);

            List<Beer> beersList = beers.stream()
                    .map(beer-> Beer.builder()
                        .name(beer.getBeer())
                        .price(10.0)
                        .build()
                    ).toList();

            beerRepository.saveAll(beersList);
        } catch (FileNotFoundException e) {
            System.out.println("No file found under resources");
        }
    }
}
