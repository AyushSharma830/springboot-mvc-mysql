package com.example.spring_6_mvc;

import com.example.spring_6_mvc.controllers.BeerController;
import com.example.spring_6_mvc.model.Beer;
import com.example.spring_6_mvc.repositories.BeerRepository;
import org.hibernate.annotations.UuidGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;
import java.util.UUID;

import static org.springframework.web.client.RestClient.builder;

@DataJpaTest //this is used for testing jpa repo specifically for testing whole use @SpringBootTest this will create
	//the spring context
class Spring6MvcApplicationTests {

//	@Autowired
//	BeerController beerController;

	@Autowired
	BeerRepository beerRepository;

//	@Test
//	void contextLoads() {
//		beerController.getBeer(UUID.randomUUID());
//	}

	@Test
	void testJpaRepository() {
		Beer beer = Beer.builder().name("b4").price(15.0).build();
		beerRepository.save(beer);
		List<Beer> beers = beerRepository.findAll();
		System.out.println(beers);
	}

	@Test
	void testBeerNameLengthTransactionSystemException(){
		Beer beer = Beer.builder()
				.name("1234567899012345678990123456789901234567899012345678990123456789901234567899012345678990")
				.price(12.0)
				.build();

		//thus bypassing the bean validation so the db level validation will be applied so txnSystemExc is expected
		try{
			beerRepository.save(beer);
			System.out.println("Beer saved successfully");
		}catch (TransactionSystemException e){
			System.out.println(e.getMessage());
		}
	}

}
