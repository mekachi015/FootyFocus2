package com.example.FootyFocus2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.FootyFocus2.entity.LeagueStandings;

@Service
public class LeagueStandingsService {

    @Value("${leagueStandings.api.url}")
    private String API_URL;

    @Value("${football.api.key}")
    private String API_KEY;

    public List<LeagueStandings> fetchStandings(String leagueCode, String season){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                API_URL, HttpMethod.GET, entity, Map.class, leagueCode, season);
        Map<String, Object> body = response.getBody();

        if (body == null) {
            throw new RuntimeException("Response body is null");
        }

        return mapToLeagueStandings(body);
    }

     private List<LeagueStandings> mapToLeagueStandings(Map<String, Object> body) {
        List<LeagueStandings> leagueStandingsList = new ArrayList<>();

        Map<String, Object> competition = (Map<String, Object>) body.get("competition");
        Map<String, Object> season = (Map<String, Object>) body.get("season");
        List<Map<String, Object>> standings = (List<Map<String, Object>>) body.get("standings");

        for (Map<String, Object> standing : standings) {
            List<Map<String, Object>> table = (List<Map<String, Object>>) standing.get("table");

            for (Map<String, Object> teamStanding : table) {
                LeagueStandings leagueStandings = new LeagueStandings();
                Map<String, Object> team = (Map<String, Object>) teamStanding.get("team");

                leagueStandings.setCompetitionName((String) competition.get("name"));
                leagueStandings.setCompetitionEmblem((String) competition.get("emblem"));
                leagueStandings.setSeasonStartDate((String) season.get("startDate"));
                leagueStandings.setSeasonEndDate((String) season.get("endDate"));
                leagueStandings.setCurrentMatchDay((int) season.get("currentMatchday"));

                leagueStandings.setPosition((int) teamStanding.get("position"));
                leagueStandings.setTeamName((String) team.get("name"));
                leagueStandings.setTeamCrest((String) team.get("crest"));
                leagueStandings.setTeamShortName((String) team.get("shortName"));
                leagueStandings.setMatchesPlayed((int) teamStanding.get("playedGames"));
                leagueStandings.setTeamForm((String) teamStanding.get("form"));
                leagueStandings.setGamesWon((int) teamStanding.get("won"));
                leagueStandings.setGamesLost((int) teamStanding.get("lost"));
                leagueStandings.setGamesDrew((int) teamStanding.get("draw"));
                leagueStandings.setNoOfPoints((int) teamStanding.get("points"));
                leagueStandings.setGoalDifference((int) teamStanding.get("goalDifference"));

                leagueStandingsList.add(leagueStandings);
            }
        }

        return leagueStandingsList;
    }
}

