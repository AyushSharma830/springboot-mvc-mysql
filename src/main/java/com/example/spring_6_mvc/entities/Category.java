package com.example.spring_6_mvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Category {
    @Id
    @UuidGenerator
    @GeneratedValue(generator = "UUID")
    @JdbcTypeCode(value = SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;

    private String description;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "beer_category",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "beer_id")
    ) //this is used to create entry in join table used for many to many relation entries where PK key will be beerId_categoryId
    private Set<Beer> beers = new HashSet<>(); //consider case where each beer can have multiple categories and then a single category would also be related to multiple beers

    //since setter would require a set<beer> so we made this func addBeer so that
    //we will make a category with default beers as empty set and then add beer with this helper
    //so then we won't be needing to override this class constructor as builder won't be required to
    //call this helper method unlike those setter ones
    //You can make this helper on any side since its many to many it is just with which obj i will work
    public void addBeer(Beer beer) {
        this.beers.add(beer);
        beer.getCategories().add(this);
    }
}
