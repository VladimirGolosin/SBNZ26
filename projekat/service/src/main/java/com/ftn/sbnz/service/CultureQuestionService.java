package com.ftn.sbnz.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import com.ftn.sbnz.dto.CultureAnswerDTO;
import com.ftn.sbnz.dto.CultureQuestionRequestDTO;
import com.ftn.sbnz.model.models.PlantingTime;
import com.ftn.sbnz.repo.PlantingTimeRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class CultureQuestionService {

    @Autowired
    private PlantingTimeRepository plantingTimeRepository;

    public CultureAnswerDTO processCultureQuestion(CultureQuestionRequestDTO requestDTO) {
        String culture = requestDTO.getCulture();
        String plantingInstructions = getPlantingInstructionsFromKnowledgeSource(culture);
        LocalDate bestPlantingDateStart = getBestPlantingStartDate(culture);
        LocalDate bestPlantingDateEnd = getBestPlantingEndDate(culture);
        LocalDate currentDate = LocalDate.now();
        boolean isRightTime = isItRightTimeToPlant(currentDate, bestPlantingDateStart, bestPlantingDateEnd);
        return new CultureAnswerDTO(plantingInstructions, isRightTime);
    }

    private String getPlantingInstructionsFromKnowledgeSource(String culture) {
        //projekat/service/src/main/java/com/ftn/sbnz/service
        try (CSVReader reader = new CSVReader(new FileReader("../../../../../../../../../resursi/CultureInfo.csv"))) {

            String[] nextRecord;
            // skip header
            try {
                reader.readNext();
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }

            try {
                while ((nextRecord = reader.readNext()) != null) {
                    String cultureName = nextRecord[0];
                    String instructions = nextRecord[1];
                    if (cultureName.equalsIgnoreCase(culture)) {
                        return instructions;
                    }
                }
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //default
        return "Instructions not found for culture: " + culture;
    }

     public LocalDate getBestPlantingStartDate(String culture) {
        PlantingTime plantingTime = plantingTimeRepository.findByCulture(culture);
        if (plantingTime != null) {
            return plantingTime.getStartDate();
        }
        return null;
    }

    public LocalDate getBestPlantingEndDate(String culture) {
        PlantingTime plantingTime = plantingTimeRepository.findByCulture(culture);
        if (plantingTime != null) {
            return plantingTime.getEndDate();
        }
        return null;
    }

    private boolean isItRightTimeToPlant(LocalDate currentDate, LocalDate bestPlantingDateStart, LocalDate bestPlantingDateEnd) {
        if (bestPlantingDateStart != null && bestPlantingDateEnd != null) {
            return !currentDate.isBefore(bestPlantingDateStart) && !currentDate.isAfter(bestPlantingDateEnd);
        }
        return false;
    }
    
}
