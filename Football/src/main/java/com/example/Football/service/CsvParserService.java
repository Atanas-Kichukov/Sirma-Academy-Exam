package com.example.Football.service;
import com.example.Football.model.entity.MatchesDTO;
import com.example.Football.model.entity.PlayerDTO;
import com.example.Football.model.entity.RecordsDto;
import com.example.Football.model.entity.TeamDTO;
import com.example.Football.repository.MatchesRepository;
import com.example.Football.repository.PlayerRepository;
import com.example.Football.repository.RecordsRepository;
import com.example.Football.repository.TeamRepository;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MatchesRepository matchesRepository;
    @Autowired
    private RecordsRepository recordsRepository;

    private static final String[] TEAM_HEADERS = {"id", "name", "managerFullName", "group"};
    private static final String[] PLAYER_HEADERS = {"id", "teamNumber", "position", "fullName", "teamId"};
    private static final String[] MATCH_HEADERS = {"id", "ATeamId", "BTeamId", "Date", "Score"};
    private static final String[] RECORD_HEADERS = {"id", "playerId", "matchId", "fromMinutes", "toMinutes"};
    private static final Logger logger = LoggerFactory.getLogger(CsvParserService.class);
    private static final List<DateTimeFormatter> dateFormats =  Arrays.asList(DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("M/d/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy-M-d"),
            DateTimeFormatter.ofPattern("d-M-yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    private boolean doesHeaderMatch(String[] givenElements, String[] expectedValues) {

        if (givenElements.length != expectedValues.length) {
            return false;
        }

        for (int i = 0; i < expectedValues.length; i++) {
            //cleaning the BOM marker
            givenElements[0] = givenElements[0].replace("\uFEFF", "").trim();
            if (!givenElements[i].trim().equalsIgnoreCase(expectedValues[i].trim())) {
                logger.info((givenElements[i]) + " this is data");
                logger.info((expectedValues[i]) + " this is static");
                return false;
            }
        }

        return true;
    }

    public void loadTeamFromCsvFile(InputStream inputStream) {
        List<TeamDTO> teams = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (isHeader) {

                    if (!doesHeaderMatch(data, TEAM_HEADERS)) {

                        throw new IllegalArgumentException("Header is not in the right format! The right format is: ID,Name,ManagerFullName,Group! ");
                    }
                    isHeader = false;
                    continue;
                }
                teams.add(parseToTeamDto(data));
            }

            teamRepository.saveAll(teams);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void loadPlayersFromCsvFile(InputStream inputStream) {
        List<PlayerDTO> players = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (isHeader) {
                    if (!doesHeaderMatch(data, PLAYER_HEADERS)) {
                        throw new IllegalArgumentException("Header is not in the right format! The right format is: Id,TeamNumber,Position,FullName,TeamId! ");
                    }
                    isHeader = false;
                    continue;
                }
                //checking the length because if the last element is empty the arrays length is -1
                if (data.length != 5) {
                    throw new IllegalArgumentException("Invalid number of fields in the CSV line");
                }
                players.add(parserToPlayerDto(data));
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

                    if (!doesHeaderMatch(data, MATCH_HEADERS)) {
                        throw new IllegalArgumentException("Header is not in the right format! The right format is: Id,TeamNumber,Position,FullName,TeamId!");
                    }
                    isHeader = false;
                    continue;
                }
                matches.add(parseToMatchesDto(data));
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

                    if (!doesHeaderMatch(data, RECORD_HEADERS)) {
                        throw new IllegalArgumentException("Header is not in the right format! The right format is: Id,TeamNumber,Position,FullName,TeamId! ");
                    }
                    isHeader = false;
                    continue;
                }
                records.add(parseToRecordsDTO(data));
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

        for (DateTimeFormatter formatter : dateFormats) {
            try {
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {

            }
        }
        throw new IllegalArgumentException("Unsupported date format : " + date);
    }

    private TeamDTO parseToTeamDto(String[] data) {
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
        return new TeamDTO(name, managerFullName, teamGroup);
    }

    private PlayerDTO parserToPlayerDto(String[] data) {
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
            return new PlayerDTO(teamNumber, position, fullName, teamDTO);
        } else {
            throw new IllegalArgumentException("There is no team with that id in the database!");
        }

    }

    private RecordsDto parseToRecordsDTO(String[] data) {
        if (data.length != 5) {
            throw new IllegalArgumentException("Invalid number of fields in the CSV line");
        }


        Long playerId = Long.parseLong(data[1]);
        Long matchId = Long.parseLong((data[2]));
        String fromMinutesInput = data[3];
        String toMinutesInput = data[4];
        if (isNotValidNumber(fromMinutesInput) || (!toMinutesInput.equalsIgnoreCase("null") && isNotValidNumber(toMinutesInput))) {
            throw new IllegalArgumentException("Wrong format for numbers!");
        }

        PlayerDTO player = playerRepository.findById(playerId).orElse(null);
        MatchesDTO match = matchesRepository.findById(matchId).orElse(null);
        if (player != null && match != null) {
            Integer toMinutes;
            Integer fromMinutes = Integer.parseInt(fromMinutesInput);
            if (!toMinutesInput.equalsIgnoreCase("null")) {
                toMinutes = Integer.parseInt(toMinutesInput);
            } else {
                toMinutes = null;
            }
            return new RecordsDto(player, match, fromMinutes, toMinutes);

        } else {
            throw new IllegalArgumentException("Invalid ids for the the match or player!");
        }
    }

    private MatchesDTO parseToMatchesDto(String[] data) {
        if (data.length != 5) {
            throw new IllegalArgumentException("Invalid number of fields in the CSV line");
        }

        if (data[1].trim().isEmpty() || data[1].equalsIgnoreCase("null") || data[2].trim().isEmpty() || data[2].equalsIgnoreCase("null")) {
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
            return new MatchesDTO(teamA, teamB, date, score);

        } else {
            throw new IllegalArgumentException("Invalid ids for the teams!");
        }
    }

}
