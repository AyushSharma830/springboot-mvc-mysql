package com.example.spring_6_mvc.entities;

import com.example.spring_6_mvc.repositories.BeerRepository;
import com.example.spring_6_mvc.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryTest {
    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Beer testBeer;

    @BeforeEach
    void setUp() {
        testBeer = beerRepository.findAll().get(0);
    }

    @Transactional
    @Test
    public void testCategoryRepo() {
        Category category = Category.builder()
                .description("hi there")
                .build();

        category.addBeer(testBeer); //since set beer will not be used so this will be our helper fun

        Category savedCategory = categoryRepository.saveAndFlush(category);

        System.out.println(savedCategory);
    }
}