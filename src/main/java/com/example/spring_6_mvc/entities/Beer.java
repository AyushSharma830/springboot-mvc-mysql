package com.example.spring_6_mvc.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Beer {
    @Id //tells hibernate that this key is the primary key of this entity
    @UuidGenerator //generator named UUID
    @GeneratedValue(generator = "UUID") //this tells hibernate to automatically generate the value of this field
    // and do that you tell to use a generator named UUID and the UUID generator has name UUID
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false) //this is used to define
    // the column level specifications like length of value or its type or is it updatable etc
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID uuid;
    @NotNull
    @NotBlank // but if checks are present at entity level and not at dto and we use save and not saveAndFlush
    //then it is possible that the api may succeed as the data is flushed or committed by jpa in batches so
    // so flush ensure their instant commit to db though the data itself will not be saved as the hibernate
    // runs the validations at commit only or you can use @Valid which ensures the validations at time of
    //creation of object, use @Valid at controller level with dto
    @Size(max = 50) //this will be ensured by @Valid and will throw error before going to db will throw
    //ConstraintViolationException, this is a bean validation
    @Column(length = 50) //but if the hibernate validations are bypassed then db will ensure that this column
    // level validation is true for each data in db so it will throw TransactionSystemException and RollbackException
    //as the bean validation was bypassed so error is thrown at time of txn, though the reason of TxnSysException
    // is still constraint violation exception
    private String name;
    private Double price;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;
}
