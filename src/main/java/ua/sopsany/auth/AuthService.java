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
            if (u != null && u.getPassword() == password.hashCode() && !u.isBlocked()) {
                return Optional.of(u);
            }
            throw new UnauthorizedException("Invalid login or password");
        }

        public User findByLogin(String login) {
            return users.get(login);
        }

    }
