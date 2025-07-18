package com.example.spring_6_mvc.services;

import com.example.spring_6_mvc.mappers.BeerMapper;
import com.example.spring_6_mvc.model.Beer;
import com.example.spring_6_mvc.model.BeerDTO;
import com.example.spring_6_mvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceDTOImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    private static final int DEFAULT_PAGE_SIZE = 25;
    private static final int DEFAULT_PAGE_NUMBER = 1;

    @Override
    public Page<BeerDTO> getBeers(String name, Double price, Integer limit, Integer offset) {

        Page<Beer> beers;

        PageRequest pageRequest = buildPageRequest(limit, offset);

        if(name!=null && !name.isBlank() && price==null) {
            beers = beerRepository.findAllByNameIsLikeIgnoreCase("%" + name + "%", pageRequest);
        } else if((name==null || name.isBlank()) && price!=null) {
            beers = beerRepository.findAllByPrice(price, pageRequest);
        } else if(name!=null && !name.isBlank() && price!=null) {
            beers = beerRepository.findAllByNameIsLikeIgnoreCaseAndPrice("%" + name + "%", price, pageRequest);
        } else {
            beers = beerRepository.findAll(pageRequest);
        }

//        return beers
//                .stream()
//                .map(beerMapper::BeerTOBeerDTO)
//                // this is method reference, a part of stream api
//                .collect(Collectors.toList());

        return beers.map(beerMapper::BeerTOBeerDTO);
    }

    private PageRequest buildPageRequest(Integer limit, Integer offset) {
        int pageSize, pageNumber;

        if(limit!=null && limit>0) pageSize = limit;
        else pageSize = DEFAULT_PAGE_SIZE;
        if(offset!=null && offset>0) pageNumber = offset - 1;
        else pageNumber = DEFAULT_PAGE_NUMBER - 1;

        Sort sort = Sort.by(Sort.Order.asc("name"));

        return PageRequest.of(pageNumber, pageSize, sort);
    }

    @Override
    public Optional<BeerDTO> getBeer(UUID id) {
        return beerRepository.findById(id).map(beerMapper::BeerTOBeerDTO); //returning optional
    }

    @Override
    public BeerDTO createBeer(BeerDTO beer) {
        return beerMapper.BeerTOBeerDTO(beerRepository.save(beerMapper.BeerDTOToBeer(beer)));
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer) {
        //in java the inner lambda can't update the outside variables so to achieve that we use atomic
        //reference so we will set optional in it and at return we will get the value
        AtomicReference<Optional<BeerDTO>> result = new AtomicReference<>();
        beerRepository.findById(id).ifPresentOrElse(foundBeer-> {
                foundBeer.setName(beer.getName());
                foundBeer.setPrice(beer.getPrice());
                result.set(Optional.of(
                        beerMapper.BeerTOBeerDTO(beerRepository.save(foundBeer))
                ));
            }, () -> {
                    result.set(Optional.empty());
                }
        );
        return result.get();
    }

    @Override
    public Optional<BeerDTO> deleteBeer(UUID id) {
        AtomicReference<Optional<BeerDTO>> result = new AtomicReference<>();
        beerRepository.findById(id).ifPresentOrElse(foundBeer -> {
                beerRepository.deleteById(id);
                result.set(Optional.of(beerMapper.BeerTOBeerDTO(foundBeer)));
            }, () -> {
                    result.set(Optional.empty());
                }
        );
        return result.get();
    }
}
