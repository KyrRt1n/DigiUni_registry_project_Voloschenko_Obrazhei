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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ua.sopsany.Main.university;

public class FileStorageService {
    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class); // Логування
    private ObjectMapper mapper;

    private final Path backupDir = Paths.get("data/backups");
    private final Path uniFilePath = Paths.get("data/university_data.json");
    private final Path usersFilePath = Paths.get("data/users_data.json");
    private static final DateTimeFormatter TS_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    /** Скільки найсвіжіших бекапів тримаємо; решта автоматично видаляються. */
    private static final int MAX_BACKUPS = 20;

    public FileStorageService() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            Path dataDir = Paths.get("data");
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
            if (!Files.exists(backupDir)) {
                Files.createDirectories(backupDir);
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

    public Path createBackup(University uni, String reason) {
        try {
            if (!Files.exists(backupDir)) Files.createDirectories(backupDir);
            String safe = reason == null ? "manual"
                    : reason.replaceAll("[^a-zA-Z0-9_-]", "_");
            String fileName = LocalDateTime.now().format(TS_FMT) + "_" + safe + ".json";
            Path target = backupDir.resolve(fileName);
            String json = mapper.writeValueAsString(uni);
            Files.writeString(target, json);
            log.info("Backup created: {}", target.toAbsolutePath());
            pruneOldBackups();
            return target;
        } catch (Exception e) {
            log.error("Failed to create backup", e);
            return null;
        }
    }

    public List<Path> listBackups() {
        if (!Files.exists(backupDir)) return List.of();
        try (Stream<Path> stream = Files.list(backupDir)) {
            return stream
                    .filter(p -> p.getFileName().toString().endsWith(".json"))
                    .sorted(Comparator.comparing(Path::getFileName).reversed())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to list backups", e);
            return List.of();
        }
    }

    public University restoreFromBackup(Path backupFile) {
        try {
            String json = Files.readString(backupFile);
            University restored = mapper.readValue(json, University.class);
            log.info("Restored university from backup: {}", backupFile.getFileName());
            return restored;
        } catch (Exception e) {
            log.error("Failed to restore from backup {}", backupFile, e);
            return null;
        }
    }

    private void pruneOldBackups() {
        List<Path> all = listBackups();
        if (all.size() <= MAX_BACKUPS) return;
        for (Path p : all.subList(MAX_BACKUPS, all.size())) {
            try {
                Files.deleteIfExists(p);
                log.debug("Old backup deleted: {}", p.getFileName());
            } catch (Exception e) {
                log.warn("Could not delete old backup {}: {}", p, e.getMessage());
            }
        }
    }
}