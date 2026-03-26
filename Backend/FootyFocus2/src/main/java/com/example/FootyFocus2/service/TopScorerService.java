package com.example.FootyFocus2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.FootyFocus2.entity.TopGoalScorer;

import java.util.*;

@Service
public class TopScorerService {

    @Value("${topScorer.api.url}")
    private String API_URL;

    @Value("${football.api.key}")
    private String API_KEY;

    public List<TopGoalScorer> fetchTopScorersFromApi(String leagueCode, int season) {

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

        return mapToTopScorers(body);
    }

    private List<TopGoalScorer> mapToTopScorers(Map<String, Object> body) {
        List<TopGoalScorer> topScorersList = new ArrayList<>();

        Map<String, Object> competition = (Map<String, Object>) body.get("competition");
        Map<String, Object> season = (Map<String, Object>) body.get("season");
        List<Map<String, Object>> scorers = (List<Map<String, Object>>) body.get("scorers");

        for (Map<String, Object> scorerData : scorers) {
            TopGoalScorer topScorer = new TopGoalScorer();
            Map<String, Object> player = (Map<String, Object>) scorerData.get("player");
            Map<String, Object> team = (Map<String, Object>) scorerData.get("team");

            //This section handles getting competition information
            topScorer.setCompName((String) competition.get("name"));
            topScorer.setCompEmblem((String) competition.get("emblem"));
            topScorer.setSeasonStartDate((String) season.get("startDate"));
            topScorer.setSeasonEndDate((String) season.get("endDate"));
            topScorer.setCurrentMatchday((int) season.get("currentMatchday"));

            //This section gets player
            topScorer.setId((int) player.get("id"));
            topScorer.setPlayerName((String) player.get("name"));
            topScorer.setNationality((String) player.get("nationality"));
            topScorer.setSection((String) player.get("section"));
            topScorer.setTeamName((String) team.get("name"));
            topScorer.setTeamShortName((String) team.get("shortName"));
            topScorer.setTeamCrest((String) team.get("crest"));
//            topScorer.setMatchesPlayed((int) scorerData.get("playedMatches"));
            topScorer.setGoalsScored((int) scorerData.get("goals"));
            topScorer.setNoOfAssits((int) scorerData.get("assists"));
          //  topScorer.setPenalties((int) scorerData.get("penalties")); // error was cause by penaties being null

            topScorersList.add(topScorer);
        }

        return topScorersList;
    }
}
