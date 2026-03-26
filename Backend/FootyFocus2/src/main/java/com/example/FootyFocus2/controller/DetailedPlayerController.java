package com.example.FootyFocus2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.example.FootyFocus2.entity.DetailedPlayer;
import com.example.FootyFocus2.service.DetailedPlayerService;

@RestController
@RequestMapping("/v4/persons")
public class DetailedPlayerController {

    @Autowired
    private DetailedPlayerService detailedPlayerService;

    @GetMapping("/{id}")
    public ResponseEntity<?> fetchDetailedPlayerInfo(@PathVariable Long id) {
        try {
            DetailedPlayer player = detailedPlayerService.fetchDetailedPlayerInfo(id);
            return ResponseEntity.ok(player);

        } catch (HttpClientErrorException e) {
            return switch (e.getStatusCode().value()) {
                case 400 -> ResponseEntity.badRequest()
                        .body("Bad request — invalid player ID: " + id);
                case 401 -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized — invalid or missing API token.");
                case 403 -> ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Forbidden — your plan does not include access to player details.");
                case 404 -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No player found with ID: " + id);
                case 429 -> ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body("Rate limit reached — too many requests to the football API.");
                default  -> ResponseEntity.status(e.getStatusCode())
                        .body("Client error: " + e.getMessage());
            };

        } catch (HttpServerErrorException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Football API is currently unavailable. Please try again later.");

        } catch (ResourceAccessException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Could not reach the football API — possible network or timeout issue.");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
