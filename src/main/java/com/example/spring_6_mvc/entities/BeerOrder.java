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

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
public class BeerOrder {

    //this is all args constructor to use setter for customer instead of direct assign
    public BeerOrder(UUID id, Long version, LocalDateTime createdAt, LocalDateTime lastUpdatedAt, Customer customer, Set<BeerOrderLineItem> beerOrderLineItems) {
        this.id = id;
        this.version = version;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
        this.setCustomer(customer);
        this.beerOrderLineItems = beerOrderLineItems;
    }

    @Id
    @UuidGenerator
    @GeneratedValue(generator = "UUID")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;

    @ManyToOne
    private Customer customer;

    @Builder.Default
    @OneToMany(mappedBy = "beerOrder")
    private Set<BeerOrderLineItem> beerOrderLineItems = new HashSet<>(); // a single can consist of orders of multiple beers so one order to multiple line items

    //Helper func which auto sets beerOrders on setting of beer.customer but we use lombok's builder which uses
    //AllArgsConstructor which will do this.customer = customer and not use setter so make your our all args constructor
    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.getBeerOrders().add(this); //here make sure that beerOrders default is not null
    }
}
