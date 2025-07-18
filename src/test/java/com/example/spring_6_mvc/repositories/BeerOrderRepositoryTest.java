package com.example.spring_6_mvc.repositories;

import com.example.spring_6_mvc.entities.Beer;
import com.example.spring_6_mvc.entities.BeerOrder;
import com.example.spring_6_mvc.entities.BeerOrderShipment;
import com.example.spring_6_mvc.entities.Customer;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerOrderRepositoryTest {
    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    private Customer testCustomer;
    private Beer testBeer;

    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.findAll().get(0);
        testBeer = beerRepository.findAll().get(0);
    }

    //bcz hibernate is set for lazy load of ManyToOne relationship so without @Transactional
    //annotation savedBeerOrder will throw LazyInitializationException for not being able to
    //lazy load customer in savedBeer and this is because savedBeer was out of Transaction or
    //persistence context so it was not able to lazy load customer as outside transaction or
    //persistence context we can't access entities managed by hibernate so to make this entire test
    //in a transaction context we added this annotation so customer gets lazy loaded
    @Transactional
    @Test
    void testBeerOrderRelationship() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customer(testCustomer)
                .beerOrderShipment( //bcz of cascade this will be created in db too
                        BeerOrderShipment.builder()
                                .trackingNumber("123")
                                .build()
                )
                .build();

        //here when you see now after @Transaction annotation is that customer is lazy loaded in savedBeerOrder but that customer
        //beerOrders key is still empty bcz in bi-directional relationship you have to do updates at both side so ensure that always
        //and to do that we made helper func which on setting beer.customer will add beerOrder in customer.beerOrders too se entity
        BeerOrder savedBeerOrder = beerOrderRepository.saveAndFlush(beerOrder);

        System.out.println(savedBeerOrder);
    }
}