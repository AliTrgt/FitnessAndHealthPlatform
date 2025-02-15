package com.example.HealthAndFitnessPlatform.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "fallowTBL")
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Fallow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "fallowersId")
    private User fallowersId;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "fallowingId")
    private User fallowingId;

}
