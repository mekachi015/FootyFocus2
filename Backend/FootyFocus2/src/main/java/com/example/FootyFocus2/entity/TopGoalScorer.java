package com.example.FootyFocus2.entity;

import lombok.*;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

@Data
@Transactional
@Setter
@Getter
@Entity
public class TopGoalScorer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    //for the competition
    private String compName;
    private String compCode;
    private String compEmblem;

    //for the season
    private String seasonStartDate;
    private String seasonEndDate;
    private int currentMatchday;

    //For the player information

    private String playerName;
    private String dateOfBirth;
    private String nationality;
    private String section; // position played on the pitch

    //for the team they play for
    private String teamName;
    private String teamShortName;
    private String teamCrest;

    //Scorers information
//  private String playedMatches;
    private int goalsScored;
    private int noOfAssits;
}
