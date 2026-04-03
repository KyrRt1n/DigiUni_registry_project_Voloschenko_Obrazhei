package ua.sopsany.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ua.sopsany.models.University;

import java.io.IOException;
import java.nio.file.*;

import static ua.sopsany.Main.university;

public class FileStorageService {
    private ObjectMapper mapper;
    private final Path uniFilePath = Paths.get("src/main/java/ua/sopsany/university_data.json");

    public FileStorageService() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void saveUni(University uni){
        try {
            String json = mapper.writeValueAsString(university);
            Files.writeString(uniFilePath, json);
            System.out.println("Data saved successfully to " + uniFilePath.toAbsolutePath());
        } catch (Exception e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public University loadUniversity() {
        try {
            if (Files.exists(uniFilePath)) {
                String json = Files.readString(uniFilePath);
                return mapper.readValue(json, University.class);
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
        return null;
    }

}
