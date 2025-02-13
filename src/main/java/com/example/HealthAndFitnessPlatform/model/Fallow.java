package com.example.HealthAndFitnessPlatform.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fallowTBL")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Fallow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


}
