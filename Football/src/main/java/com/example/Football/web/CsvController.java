package com.example.Football.web;

import com.example.Football.service.CsvParserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/csv")
public class CsvController {
    @Autowired
    private CsvParserService csvParserService;

    @PostMapping("/uploadTeams")
    public String uploadCsvTeams(@RequestParam("file") MultipartFile file) {
        try {
            csvParserService.loadTeamFromCsvFile(file.getInputStream());
            return "CSV file processed successfully!";
        } catch (Exception e) {
            return "Error processing CSV file: " + e.getMessage();
        }
    }
    @PostMapping("/uploadPlayers")
    public String uploadCsvPlayers(@RequestParam("file") MultipartFile file) {
        try {
            csvParserService.loadPlayersFromCsvFile(file.getInputStream());
            return "CSV file processed successfully!";
        } catch (Exception e) {
            return "Error processing CSV file: " + e.getMessage();
        }
    }

    @PostMapping("/uploadMatches")
    public String uploadCsvMatches(@RequestParam("file") MultipartFile file) {
        try {
            csvParserService.loadMatchesFromCsvFile(file.getInputStream());
            return "CSV file processed successfully!";
        } catch (Exception e) {
            return "Error processing CSV file: " + e.getMessage();
        }
    }

    @PostMapping("/uploadRecords")
    public String uploadCsvRecords(@RequestParam("file") MultipartFile file) {
        try {
            csvParserService.loadRecordsFromCsvFile(file.getInputStream());
            return "CSV file processed successfully!";
        } catch (IOException e) {
            return "Error processing CSV file: " + e.getMessage();
        }
    }
}
