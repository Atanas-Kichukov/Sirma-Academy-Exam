package com.example.Football.service;

import com.example.Football.model.entity.MatchesDTO;
import com.example.Football.model.entity.PlayerDTO;
import com.example.Football.model.entity.RecordsDto;
import com.example.Football.model.entity.TeamDTO;
import com.example.Football.model.view.PlayerPairDTO;
import com.example.Football.model.view.PlayerPlaytimeDTO;
import com.example.Football.repository.MatchesRepository;
import com.example.Football.repository.PlayerRepository;
import com.example.Football.repository.RecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerPairService {
    private static final Logger logger = LoggerFactory.getLogger(CsvParserService.class);
    @Autowired
    private RecordsRepository recordsRepository;
    @Autowired
    private MatchesRepository matchesRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public LinkedList<PlayerPlaytimeDTO> findPlayersWithLongestPlaytime() {
        List<RecordsDto> allRecords = recordsRepository.findAll();

        Map<PlayerDTO, Integer> playerPlaytimeMap = new HashMap<>();

        for (RecordsDto record : allRecords) {
            PlayerDTO player = record.getPlayerId();
            int playtime = calculatePlaytimeForSinglePLayer(record.getFromMinutes(), record.getToMinutes());

            playerPlaytimeMap.put(player, playerPlaytimeMap.getOrDefault(player, 0) + playtime);
        }

        // Find the players with the longest total playtime
        int maxPlaytime = playerPlaytimeMap.values().stream().max(Integer::compare).orElse(0);

        // Create a list of players with the maximum playtime
        LinkedList<PlayerPlaytimeDTO> longestPlayingPlayers = new LinkedList<>()
        for (Map.Entry<PlayerDTO, Integer> entry : playerPlaytimeMap.entrySet()) {
            if (entry.getValue() == maxPlaytime) {
                PlayerDTO player = entry.getKey();
                int totalPlaytime = entry.getValue();
                longestPlayingPlayers.add(new PlayerPlaytimeDTO(
                        player.getId(),
                        player.getFullName(),
                        player.getTeamId().getName(),
                        totalPlaytime
                ));
            }
        }

        return longestPlayingPlayers;
    }

    // Calculate the playtime for a single record
    private int calculatePlaytimeForSinglePLayer(Integer fromMinutes, Integer toMinutes) {
        if (toMinutes == null) toMinutes = 90; // Assume the player played until the end of the match
        return Math.max(0, toMinutes - fromMinutes); // Return the total playtime
    }

    public List<PlayerPairDTO> groupAllPlayersInPairsByMatchId() {
        LinkedList<PlayerPlaytimeDTO> longestPlayingPlayers = this.findPlayersWithLongestPlaytime();

        for (int i = 0; i < longestPlayingPlayers.size(); i++) {
            PlayerPlaytimeDTO firstPlayerPlaytimeDTO = longestPlayingPlayers.get(i);
            PlayerDTO firstPlayer = playerRepository.findById(firstPlayerPlaytimeDTO.getPlayerId()).orElse(null);

            for (int j = i + 1; j < longestPlayingPlayers.size() - 1; j++) {
                PlayerPlaytimeDTO secondPlayerPlaytimeDTO = longestPlayingPlayers.get(i);
                PlayerDTO secondPlayer = playerRepository.findById(firstPlayerPlaytimeDTO.getPlayerId()).orElse(null);

                if(firstPlayer.getTeamId().getName().equals(secondPlayer.getTeamId().getId())){

                }
            }

        }
        Map<Long, List<Long>> playerIdWithMatchId = new HashMap<>();

        for (PlayerPlaytimeDTO player : longestPlayingPlayers) {
            Long playerId = player.getPlayerId();
            PlayerDTO playerDTO = playerRepository.findById(playerId).orElse(null);
            List<RecordsDto> playersRecords = recordsRepository.findByPlayerId(playerDTO);


            for (RecordsDto record : playersRecords) {
                if(!playerIdWithMatchId.containsKey(playerId)){
                    playerIdWithMatchId.put(playerId,new ArrayList<>());
                }else{
                    List<Long> matchesIds = playerIdWithMatchId.get(playerId);
                    matchesIds.add(record.getMatchId().getId());
                    playerIdWithMatchId.put(playerId,matchesIds);
                }

                logger.info("Player id {}---Matches {}",playerId,


                        playerIdWithMatchId.get(playerId).toString());

            }

        }
        //-- change from here
        List<PlayerPairDTO> pairs = new ArrayList<>();
        for (Map.Entry<Long, List<Long>> entry : playerIdWithMatchId.entrySet()) {

        }

        return null;
    }



}
