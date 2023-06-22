package com.demospringfullstack.springbootexample.customer;


import com.github.javafaker.IdNumber;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
/*@Table(
    name = "customer",
    uniqueConstraints = {
            @UniqueConstraint(
                    name = "customer_email_unique",
                    columnNames = "email"
            ),
            @UniqueConstraint(
                    name = "profile_image_id_unique",
                    columnNames = "profileImageId"
            )
    }
)*/
public class Customer {

    @Id
    @SequenceGenerator(
            name = "customer_id_sequence",
            sequenceName = "customer_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_sequence"
    )
    private Integer id;
    @Column(
            nullable = false
    )
    private String name;
    @Column(
            nullable = false,
            unique = true
    )
    private String email;
    @Column(
            nullable = false
    )
    private Integer age;

    public Customer(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
}
