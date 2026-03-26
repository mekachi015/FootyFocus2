package com.example.FootyFocus2.entity;

import org.springframework.stereotype.Indexed;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

@Entity
@Transactional
@Setter
@Getter
@Table(name = "league_standings")
public class LeagueStandings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //for competition - Primier league, Ligue 1, la liga
    //under competition
    private String competitionName;
    private String competitionEmblem;

    //for season start and end date
    private String seasonStartDate;
    private String seasonEndDate;
    private int currentMatchDay;

    //For actual standings
    //from table
    private int position;

    //from team
    private String teamName;
    private String teamCrest;
    private String teamShortName;

    //from table
    private int matchesPlayed;
    private  String teamForm;
    private int gamesWon;
    private int gamesLost;
    private int gamesDrew;
    private int noOfPoints;
    private int goalDifference;


}
