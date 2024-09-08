package com.example.Football.service;

import com.example.Football.model.entity.MatchesDTO;
import com.example.Football.model.entity.PlayerDTO;
import com.example.Football.model.entity.RecordsDto;
import com.example.Football.model.entity.TeamDTO;
import com.example.Football.repository.MatchesRepository;
import com.example.Football.repository.PlayerRepository;
import com.example.Football.repository.RecordsRepository;
import com.example.Football.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CsvParserService {
    private static final Logger logger = LoggerFactory.getLogger(CsvParserService.class);
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MatchesRepository matchesRepository;
    @Autowired
    private RecordsRepository recordsRepository;

    public void loadTeamFromCsvFile(InputStream inputStream) {
        List<TeamDTO> teams = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (isHeader) {
                    data[0] = data[0].replace("\uFEFF", "").trim();
                    if (!data[0].equalsIgnoreCase("id")
                            || !data[1].equalsIgnoreCase("name")
                            || !data[2].equalsIgnoreCase("managerFullName")
                            || !data[3].equalsIgnoreCase("group")) {
                        throw new IllegalArgumentException("Header is not in the right format! " +
                                "The right format is: ID,Name,ManagerFullName,Group! ");
                    }
                    isHeader = false;
                    continue;
                }
                if (data.length != 4) {
                    throw new IllegalArgumentException("Invalid number of fields in the CSV line");
                }

                String name = data[1];
                String managerFullName = data[2];
                String teamGroup = data[3];

                if (name.equalsIgnoreCase("null") || name.trim().isEmpty()) {
                    throw new IllegalArgumentException("Full name mustn't be null or empty!");
                }

                if (managerFullName.equalsIgnoreCase("null") || managerFullName.trim().isEmpty()) {
                    throw new IllegalArgumentException("Manager name mustn't be null or empty!");
                }

                if (teamGroup.equalsIgnoreCase("null") || teamGroup.trim().isEmpty()) {
                    throw new IllegalArgumentException("Team group mustn't be null or empty!");
                }
                TeamDTO teamDTO = new TeamDTO(name, managerFullName, teamGroup);
                teams.add(teamDTO);

            }

            teamRepository.saveAll(teams);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadPlayersFromCsvFile(InputStream inputStream) {
        List<PlayerDTO> players = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new BufferedReader(new InputStreamReader(inputStream)))) {

            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (isHeader) {
                    //cleaning the BOM marker
                    data[0] = data[0].replace("\uFEFF", "").trim();
                    if (!data[0].trim().equalsIgnoreCase("id")
                            || !data[1].trim().equalsIgnoreCase("teamNumber")
                            || !data[2].trim().equalsIgnoreCase("position")
                            || !data[3].trim().equalsIgnoreCase("fullName")
                            || !data[4].trim().equalsIgnoreCase("teamId")) {
                        logger.info(Arrays.toString(data));
                        throw new IllegalArgumentException("Header is not in the right format! " +
                                "The right format is: Id,TeamNumber,Position,FullName,TeamId! ");
                    }
                    isHeader = false;
                    continue;
                }
                if (data.length != 5) {
                    throw new IllegalArgumentException("Invalid number of fields in the CSV line");
                }

                PlayerDTO player;
                String teamNumberInput = data[1];
                String position = data[2];
                String fullName = data[3];
                Long teamId = Long.parseLong(data[4]);
                TeamDTO teamDTO = teamRepository.findById(teamId).orElse(null);

                if (teamNumberInput.equalsIgnoreCase("null") || teamNumberInput.trim().isEmpty() || isNotValidNumber(teamNumberInput)) {
                    throw new IllegalArgumentException("Team number must include only digits and mustn't be null or empty");
                }

                if (position.equalsIgnoreCase("null") || position.trim().isEmpty()) {
                    throw new IllegalArgumentException("Position  mustn't be null or empty");
                }
                if (fullName.equalsIgnoreCase("null") || fullName.trim().isEmpty()) {
                    throw new IllegalArgumentException("Position  mustn't be null or empty");
                }

                if (teamDTO != null) {
                    int teamNumber = Integer.parseInt(teamNumberInput);
                    player = new PlayerDTO(teamNumber, position, fullName, teamDTO);
                    players.add(player);
                } else {
                    throw new IllegalArgumentException("There is no team with that id in the database!");
                }


            }
            playerRepository.saveAll(players);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadMatchesFromCsvFile(InputStream inputStream) {
        List<MatchesDTO> matches = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (isHeader) {
                    data[0] = data[0].replace("\uFEFF", "").trim();
                    if (!data[0].equalsIgnoreCase("id")
                            || !data[1].equalsIgnoreCase("ATeamId")
                            || !data[2].equalsIgnoreCase("BTeamId")
                            || !data[3].equalsIgnoreCase("Date")
                            || !data[4].equalsIgnoreCase("Score")) {
                        throw new IllegalArgumentException("Header is not in the right format! " +
                                "The right format is: Id,TeamNumber,Position,FullName,TeamId! ");
                    }
                    isHeader = false;
                    continue;
                }
                if (data.length != 5) {
                    throw new IllegalArgumentException("Invalid number of fields in the CSV line");
                }

                if (data[1].trim().isEmpty()
                        || data[1].equalsIgnoreCase("null")
                        || data[2].trim().isEmpty() || data[2].equalsIgnoreCase("null")) {
                    throw new IllegalArgumentException("Id field mustn't be empty or null");
                }
                Long idATeam = Long.parseLong(data[1]);
                Long idBTeam = Long.parseLong(data[2]);
                LocalDate date = parseDate(data[3]);
                String score = data[4];
                TeamDTO teamA = teamRepository.findById(idATeam).orElse(null);
                TeamDTO teamB = teamRepository.findById(idBTeam).orElse(null);


                if (score.equalsIgnoreCase("null") || score.trim().isEmpty()) {
                    throw new IllegalArgumentException("Score mustn't be null or empty!");
                }

                if (teamA != null && teamB != null) {
                    MatchesDTO matchesDTO = new MatchesDTO(teamA, teamB, date, score);
                    matches.add(matchesDTO);
                } else {
                    throw new IllegalArgumentException("Invalid ids for the teams!");
                }
            }
            matchesRepository.saveAll(matches);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void loadRecordsFromCsvFile(InputStream inputStream) {
        List<RecordsDto> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (isHeader) {
                    data[0] = data[0].replace("\uFEFF", "").trim();
                    if (!data[0].equalsIgnoreCase("id")
                            || !data[1].equalsIgnoreCase("playerId")
                            || !data[2].equalsIgnoreCase("matchId")
                            || !data[3].equalsIgnoreCase("fromMinutes")
                            || !data[4].equalsIgnoreCase("toMinutes")) {
                        throw new IllegalArgumentException("Header is not in the right format! " +
                                "The right format is: Id,TeamNumber,Position,FullName,TeamId! ");
                    }
                    isHeader = false;
                    continue;
                }
                if (data.length != 5) {
                    throw new IllegalArgumentException("Invalid number of fields in the CSV line");
                }


                Long playerId = Long.parseLong(data[1]);
                Long matchId = Long.parseLong((data[2]));
                String fromMinutesInput = data[3];
                String toMinutesInput = data[4];
                if(isNotValidNumber(fromMinutesInput) || (!toMinutesInput.equalsIgnoreCase("null") && isNotValidNumber(toMinutesInput) )){
                    throw new IllegalArgumentException("Wrong format for numbers!");
                }

                PlayerDTO player = playerRepository.findById(playerId).orElse(null);
                MatchesDTO match = matchesRepository.findById(matchId).orElse(null);
                if (player != null && match != null) {
                    Integer toMinutes;
                    Integer fromMinutes = Integer.parseInt(fromMinutesInput);
                    if(!toMinutesInput.equalsIgnoreCase("null")) {
                         toMinutes = Integer.parseInt(toMinutesInput);
                    }else{
                        toMinutes = null;
                    }
                    RecordsDto recordsDto = new RecordsDto(player, match, fromMinutes, toMinutes);
                    records.add(recordsDto);
                } else {
                    throw new IllegalArgumentException("Invalid ids for the the match or player!");
                }
            }
            recordsRepository.saveAll(records);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isNotValidNumber(String value) {
        return value.chars().anyMatch(Character::isAlphabetic);
    }

    public LocalDate parseDate(String date) {
        //didn't cover all patterns so there could be a situation to throw an Exception
        List<DateTimeFormatter> formatters = Arrays.asList(
                DateTimeFormatter.ofPattern("MM/dd/yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("d/M/yyyy"),
                DateTimeFormatter.ofPattern("M/d/yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy-M-d"),
                DateTimeFormatter.ofPattern("d-M-yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
        );
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {

            }
        }
        throw new IllegalArgumentException("Unsupported date format : " + date);
    }
}
