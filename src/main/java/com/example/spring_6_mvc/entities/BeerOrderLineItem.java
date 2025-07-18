package com.example.spring_6_mvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
public class BeerOrderLineItem {

    public BeerOrderLineItem(UUID id, Long version, LocalDateTime createdAt, Timestamp lastUpdatedAt, Integer orderQuantity, Integer quantityAllocated, BeerOrder beerOrder, Beer beer) {
        this.id = id;
        this.version = version;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
        this.orderQuantity = orderQuantity;
        this.quantityAllocated = quantityAllocated;
        this.setBeerOrder(beerOrder);;
        this.setBeer(beer);
    }

    @Id
    @UuidGenerator
    @GeneratedValue(generator = "UUID")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false )
    private UUID id;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private Timestamp lastUpdatedAt;

    private Integer orderQuantity = 0;

    private Integer quantityAllocated = 0;

    @ManyToOne
    private BeerOrder beerOrder;

    @ManyToOne
    private Beer beer; //a single beer may be linked to multiple beer order line items so one to many from beer side for beer order line items

    public void setBeer(Beer beer) {
        this.beer = beer;
        beer.getBeerOrderLineItems().add(this);
    }

    public void setBeerOrder(BeerOrder beerOrder) {
        this.beerOrder = beerOrder;
        beerOrder.getBeerOrderLineItems().add(this);
    }
}
