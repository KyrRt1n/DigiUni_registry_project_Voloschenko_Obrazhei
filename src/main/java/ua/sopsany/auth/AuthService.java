package ua.sopsany.auth;

import java.time.LocalDateTime;
import java.util.*;

import ua.sopsany.exceptions.UnauthorizedException;

public class AuthService {
    private Map<String, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getLogin(), user);
    }

    public Optional<User> login(String login, String password) throws UnauthorizedException {
        if (login == null || login.isBlank()) {
            throw new UnauthorizedException("Login must not be empty");
        }
        if (password == null) {
            throw new UnauthorizedException("Password must not be null");
        }

        User u = users.get(login);
        if (u == null) {
            throw new UnauthorizedException("User not found");
        }
        if (u.isBlocked()) {
            throw new UnauthorizedException("User is blocked");
        }
        if (u.getPassword() == password.hashCode()) {
            u.setLastLogin(LocalDateTime.now());
            return Optional.of(u);
        }
        throw new UnauthorizedException("Invalid login or password");
    }

    public User findByLogin(String login) {
        if (login == null) return null;
        return users.get(login);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public boolean removeUser(String login) {
        return users.remove(login) != null;
    }

    public void changePassword(User user, String newPassword) {
        if (user == null || newPassword == null) {
            throw new IllegalArgumentException("User and password must not be null");
        }
        user.setPassword(newPassword);
    }
}