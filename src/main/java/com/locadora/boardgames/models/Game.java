package com.locadora.boardgames.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "games", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String image;

    @Column(nullable = false)
    private Integer stockTotal;

    @Column(nullable = false)
    private Integer pricePerDay;
}
