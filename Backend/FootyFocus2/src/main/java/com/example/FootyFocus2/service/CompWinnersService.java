package com.example.FootyFocus2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.FootyFocus2.entity.CompWinners;

import tools.jackson.databind.JsonNode;

@Service
public class CompWinnersService {

    @Value("${football.api.key}")
    private String API_KEY;

    @Value("${compWinners.api.url}")
    private String API_URL;

    private final RestTemplate restTemplate;

    public CompWinnersService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<CompWinners> fetchWinnersFromApi(String leagueCode) {
        var headers = new HttpHeaders();
        headers.set("X-Auth-Token", API_KEY);

        var entity = new HttpEntity<String>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                API_URL + leagueCode, HttpMethod.GET, entity, JsonNode.class);

        JsonNode body = response.getBody();

        if (body == null) {
            throw new RuntimeException("Response body is null");
        }

        return mapToCompWinners(leagueCode, body);
    }

    private List<CompWinners> mapToCompWinners(String leagueCode, JsonNode body) {
        List<CompWinners> compWinnersList = new ArrayList<>();

        JsonNode seasons = body.get("seasons");

        if (seasons == null || seasons.isEmpty()) {
            throw new RuntimeException("No seasons data found for league: " + leagueCode);
        }

        for (JsonNode season : seasons) {
            JsonNode winner = season.get("winner");

            if (winner != null && !winner.isNull()) {
                var compWinners = new CompWinners();
                compWinners.setLeagueCode(leagueCode);
                compWinners.setStartDate(season.has("startDate") ? season.get("startDate").asText() : null);
                compWinners.setEndDate(season.has("endDate")     ? season.get("endDate").asText()   : null);
                compWinners.setWinnerName(winner.has("name")      ? winner.get("name").asText()      : null);
                compWinners.setShortName(winner.has("shortName")  ? winner.get("shortName").asText() : null);
                compWinners.setWinnersCrest(winner.has("crest")   ? winner.get("crest").asText()     : null);
                compWinners.setWinnerVenue(winner.has("venue")    ? winner.get("venue").asText()      : null);

                compWinnersList.add(compWinners);
            }
        }

        return compWinnersList;
    }
}
