package ua.sopsany.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ua.sopsany.auth.AuthService;
import ua.sopsany.auth.Role;
import ua.sopsany.auth.User;
import ua.sopsany.models.University;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static ua.sopsany.Main.university;

public class FileStorageService {
    private ObjectMapper mapper;
    private final Path uniFilePath = Paths.get("src/main/java/ua/sopsany/university_data.json");
    private final Path usersFilePath = Paths.get("src/main/java/ua/sopsany/users_data.json");

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
//            System.out.println("Data saved successfully to " + uniFilePath.toAbsolutePath());
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

    public void saveUsers(AuthService authService) {
        try {
            List<Map<String, Object>> data = new ArrayList<>();
            for (User u : authService.getAllUsers()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("login", u.getLogin());
                m.put("passwordHash", u.getPassword());
                m.put("role", u.getRole().toString());
                m.put("isBlocked", u.isBlocked());
                data.add(m);
            }
            String json = mapper.writeValueAsString(data);
            Files.writeString(usersFilePath, json);
//            System.out.println("Users saved successfully to " + usersFilePath.toAbsolutePath());
        } catch (Exception e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public void loadUsers(AuthService authService) {
        try {
            if (!Files.exists(usersFilePath)) return;

            String json = Files.readString(usersFilePath);
            List<Map<String, Object>> data = mapper.readValue(json,
                    mapper.getTypeFactory().constructCollectionType(List.class, Map.class));

            for (Map<String, Object> m : data) {
                String login = (String) m.get("login");
                int passwordHash = (Integer) m.get("passwordHash");
                Role role = Role.valueOf((String) m.get("role"));
                boolean isBlocked = (Boolean) m.get("isBlocked");

                User user = new User(login, passwordHash, role, isBlocked);
                authService.addUser(user);
            }
            System.out.println("Users loaded from file! (" + data.size() + " users)");
        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }
}