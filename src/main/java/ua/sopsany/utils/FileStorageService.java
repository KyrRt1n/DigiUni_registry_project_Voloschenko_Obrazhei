package ua.sopsany.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.sopsany.auth.AuthService;
import ua.sopsany.auth.Role;
import ua.sopsany.auth.User;
import ua.sopsany.models.University;

import java.nio.file.*;
import java.util.*;

import static ua.sopsany.Main.university;

public class FileStorageService {
    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class); // Логування
    private ObjectMapper mapper;

    private final Path dataDir = Paths.get("data");
    private final Path uniFilePath = Paths.get("data/university_data.json");
    private final Path usersFilePath = Paths.get("data/users_data.json");

    public FileStorageService() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
        } catch (Exception e) {
            log.error("Failed to create data directory", e);
        }
    }

    public void saveUni(University uni){
        try {
            String json = mapper.writeValueAsString(university);
            Files.writeString(uniFilePath, json);
            log.info("University data saved successfully to {}", uniFilePath.toAbsolutePath());
        } catch (Exception e) {
            log.error("Error saving university data", e);
        }
    }

    public University loadUniversity() {
        try {
            if (Files.exists(uniFilePath)) {
                String json = Files.readString(uniFilePath);
                log.info("University data loaded from {}", uniFilePath);
                return mapper.readValue(json, University.class);
            }
        } catch (Exception e) {
            log.error("Error loading university data", e);
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
            log.info("Users saved successfully to {}", usersFilePath.toAbsolutePath());
        } catch (Exception e) {
            log.error("Error saving users", e);
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
            log.info("Users loaded from file! ({} users)", data.size());
        } catch (Exception e) {
            log.error("Error loading users", e);
        }
    }
}