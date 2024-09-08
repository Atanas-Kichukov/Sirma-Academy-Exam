package com.example.Football.web;

import com.example.Football.model.entity.PlayerDTO;
import com.example.Football.model.view.PlayerPlaytimeDTO;
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

//    @GetMapping("/longest-playing-pair")
//    public void getLongestPlayingPair(){
//        playerPairService.findLongestPlayingPairInTheSameTeam();
//    }
//    @GetMapping("/longest-playing-pair-for-match")
//    public ResponseEntity<?> getLongestPlayingPair2() {
//        try {
//            // Call the service to get the data
//            List<Map<String, Object>> results = playerPairService.findLongestPlayingPairsPerTeamAndMatch();
//
//            if (!results.isEmpty()) {
//                // Return the result in the response
//                return new ResponseEntity<>(results, HttpStatus.OK);
//            } else {
//                // If no pair found, return a no-content response
//                return new ResponseEntity<>("No player pair found", HttpStatus.NO_CONTENT);
//            }
//
//        } catch (Exception e) {
//            // In case of any error, return a bad request response
//            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping("/longest")
    public ResponseEntity<?> getLongestPlayingPlayers() {
        try {
            // Call the service to get the list of players with the longest playtime
            List<PlayerPlaytimeDTO> longestPlayingPlayers = playerPairService.findPlayersWithLongestPlaytime();
            playerPairService.groupAllPlayersInPairsByMatchId();
            if (!longestPlayingPlayers.isEmpty()) {
                // Return the list of players in the response with OK status
                return new ResponseEntity<>(longestPlayingPlayers, HttpStatus.OK);
            } else {
                // If no players found, return a no-content response
                return new ResponseEntity<>("No players found", HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            // In case of any error, return a bad request response
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
