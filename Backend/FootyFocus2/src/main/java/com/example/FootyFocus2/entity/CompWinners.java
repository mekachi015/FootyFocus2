package com.example.FootyFocus2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CompWinners {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String leagueCode; //stores the leagues code either pl, sa, pd ect

    private String startDate; // the begining of the league

    private String endDate; // the last day of the league

    private String winnerName; //the name of the winnig team

    private String shortName; // the winning team shortened name

    private String winnersCrest; //stores the cres for the winning team

    private String winnerVenue; // the home stadium of the winning team

}
