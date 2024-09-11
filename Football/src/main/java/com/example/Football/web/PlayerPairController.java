package com.example.Football.web;
import com.example.Football.service.PlayerPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/player-pairs")
public class PlayerPairController {

    @Autowired
    private PlayerPairService playerPairService;
    @GetMapping("/longest")
    public ResponseEntity<?> getLongestPlayingPlayers() {
        try {

            List<Map<String, Object>> longestPlayingPlayers = playerPairService.findLongestPlayingPairsPerMatch();

            if (!longestPlayingPlayers.isEmpty()) {

                return new ResponseEntity<>(longestPlayingPlayers, HttpStatus.OK);
            } else {

                return new ResponseEntity<>("No players found", HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {

            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
