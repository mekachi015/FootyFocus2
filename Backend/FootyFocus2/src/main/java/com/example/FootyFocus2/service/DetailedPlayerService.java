package com.example.FootyFocus2.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.FootyFocus2.entity.DetailedPlayer;

import org.springframework.http.*;

@Service
public class DetailedPlayerService {

    @Value("${detailedPlayer.api.url}")
    private String API_URL;

    @Value("${football.api.key}")
    private String API_KEY;

    public DetailedPlayer fetchDetailedPlayerInfo(Long id) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(API_URL + id, HttpMethod.GET, entity, Map.class);

        Map<String, Object> body = response.getBody();

        if (body == null) {
            throw new RuntimeException("Response body is null");
        }

        DetailedPlayer playerInfo = new DetailedPlayer();
        playerInfo.setId(id);
        playerInfo.setPlayerName((String) body.get("name"));
        playerInfo.setDateOfBirth((String) body.get("dateOfBirth"));
        playerInfo.setNationality((String) body.get("nationality"));
        playerInfo.setSection((String) body.get("section"));
        playerInfo.setShirtNumber((int) body.get("shirtNumber"));

        Map<String, Object> currentTeam = (Map<String, Object>) body.get("currentTeam");
        if (currentTeam != null) {
            playerInfo.setTeamName((String) currentTeam.get("name"));
            playerInfo.setTeamCrest((String) currentTeam.get("crest"));
            playerInfo.setTeamVenue((String) currentTeam.get("venue"));
        }
        return playerInfo;
    }

}
