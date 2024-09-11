package com.example.Football.service;
import com.example.Football.model.entity.MatchesDTO;
import com.example.Football.model.entity.PlayerDTO;
import com.example.Football.model.entity.RecordsDto;
import com.example.Football.model.entity.TeamDTO;
import com.example.Football.repository.PlayerRepository;
import com.example.Football.repository.RecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class PlayerPairService {

    @Autowired
    private RecordsRepository recordsRepository;

    @Autowired
    private PlayerRepository playerRepository;

    // method that gathers all the date from the other ones, like this I use the Single Responsibility Principle
    public List<Map<String, Object>> findLongestPlayingPairsPerMatch() {
        List<RecordsDto> allRecords = recordsRepository.findAll();

        Map<MatchesDTO, Map<TeamDTO, List<RecordsDto>>> groupedRecords = groupRecordsByMatchAndTeam(allRecords);

        return getLongestPlayingPairsForAllMatches(groupedRecords);
    }

    //First group the  records by match and by team
    private Map<MatchesDTO, Map<TeamDTO, List<RecordsDto>>> groupRecordsByMatchAndTeam(List<RecordsDto> records) {
        return records.stream()
                .collect(Collectors.groupingBy(RecordsDto::getMatchId,
                        Collectors.groupingBy(record -> record.getPlayerId().getTeamId())));
    }

    // Second find the longest playing pair
    private List<Map<String, Object>> getLongestPlayingPairsForAllMatches(Map<MatchesDTO, Map<TeamDTO, List<RecordsDto>>> groupedRecords) {
        List<Map<String, Object>> results = new ArrayList<>();

        for (Map.Entry<MatchesDTO, Map<TeamDTO, List<RecordsDto>>> matchEntry : groupedRecords.entrySet()) {
            MatchesDTO match = matchEntry.getKey();
            Map<TeamDTO, List<RecordsDto>> teams = matchEntry.getValue();

            for (Map.Entry<TeamDTO, List<RecordsDto>> teamEntry : teams.entrySet()) {
                TeamDTO team = teamEntry.getKey();
                List<RecordsDto> teamRecords = teamEntry.getValue();

                Map<String, Object> matchResult = findLongestPlayingPairInTeam(match, team, teamRecords);
                if (matchResult != null) {
                    results.add(matchResult);
                }
            }
        }
        return results;
    }

    // Third Find the longest-playing pair in a team for a match
    private Map<String, Object> findLongestPlayingPairInTeam(MatchesDTO match, TeamDTO team, List<RecordsDto> teamRecords) {
        PlayerDTO firstPlayer = null;
        PlayerDTO secondPlayer = null;
        int maxOverlapTime = 0;

        for (int i = 0; i < teamRecords.size(); i++) {
            for (int j = i + 1; j < teamRecords.size(); j++) {
                RecordsDto firstRecord = teamRecords.get(i);
                RecordsDto secondRecord = teamRecords.get(j);

                Integer overlapTime = calculateOverlapTime(
                        firstRecord.getFromMinutes(), firstRecord.getToMinutes(),
                        secondRecord.getFromMinutes(), secondRecord.getToMinutes());

                if (overlapTime > maxOverlapTime) {
                    maxOverlapTime = overlapTime;
                    firstPlayer = playerRepository.findById(firstRecord.getPlayerId().getId()).orElse(null);
                    secondPlayer = playerRepository.findById(secondRecord.getPlayerId().getId()).orElse(null);
                }
            }
        }



        if (firstPlayer != null && secondPlayer != null) {
            Map<String, Object> matchResult = new HashMap<>();
            matchResult.put("teamName", team.getName());
            matchResult.put("matchId", match.getId());
            matchResult.put("firstPlayer", firstPlayer.getFullName());
            matchResult.put("secondPlayer", secondPlayer.getFullName());
            matchResult.put("overlapTime", maxOverlapTime);
            return matchResult;
        }
        return null;
    }


    private int calculateOverlapTime(Integer from1, Integer to1, Integer from2, Integer to2) {
        int overlapStart = Math.max(from1, from2);
        if (to1 == null) to1 = 90;
        if (to2 == null) to2 = 90;
        int overlapEnd = Math.min(to1, to2);
        return Math.max(0, overlapEnd - overlapStart);
    }
}
