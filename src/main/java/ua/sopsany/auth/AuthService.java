package ua.sopsany.auth;

import java.util.*;

import ua.sopsany.exceptions.UnauthorizedException;

public class AuthService {
    private Map<String, User> users = new HashMap<>();
    public void addUser(User user) {
        users.put(user.getLogin(), user);
    }
    public Optional<User> login(String login, String password) throws UnauthorizedException {
        User u = users.get(login);
        if(u.isBlocked())
            throw new UnauthorizedException("User is blocked");
        else if (u != null && u.getPassword() == password.hashCode()) {
            return Optional.of(u);
        }
        throw new UnauthorizedException("Invalid login or password");
    }
    public User findByLogin(String login) {
        return users.get(login);
    }
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public boolean removeUser(String login) {
        return users.remove(login) != null;
    }

    public void changePassword(User user, String newPassword) {
        user.setPassword(newPassword);
    }
}
