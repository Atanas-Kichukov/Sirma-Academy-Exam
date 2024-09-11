package com.example.Football.web;

import com.example.Football.service.CsvParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/api/csv")
public class CsvController {

    @Autowired
    private CsvParserService csvParserService;

    @PostMapping("/upload")
    public String uploadCsvFiles(@RequestParam("file") MultipartFile[] files) {

        try {

            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                InputStream inputStream = file.getInputStream();
                String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf('.'));
                if (name.equalsIgnoreCase("matches")) {
                    csvParserService.loadMatchesFromCsvFile(inputStream);
                } else if (name.equalsIgnoreCase("players")) {
                    csvParserService.loadPlayersFromCsvFile(inputStream);
                } else if (name.equalsIgnoreCase("records")) {
                    csvParserService.loadRecordsFromCsvFile(inputStream);
                } else if (name.equalsIgnoreCase("teams")) {
                    csvParserService.loadTeamFromCsvFile(inputStream);
                }
            }
        } catch (Exception e) {
            return "Error processing CSV file: " + e.getMessage();
        }
        return "CSV file processed successfully!";
    }
}
