package com.example.FootyFocus2.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

@Transactional
@Setter
@Getter
@Entity
public class DetailedPlayer {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerName;

    private String dateOfBirth;

    private String nationality;

    private String section;

    private int shirtNumber;

    //from current team
    private String teamName;
    private String teamCrest;
    private String teamVenue;
}
