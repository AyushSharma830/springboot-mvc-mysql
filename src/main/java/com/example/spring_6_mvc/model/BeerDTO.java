package com.example.spring_6_mvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BeerDTO {
    private UUID uuid;
    @NotNull
    @NotBlank
    private String name;
    private Double price;
}
