package ua.sopsany.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ua.sopsany.exceptions.UnauthorizedExcpetion;


    public class AuthService {
        private List<User> users = new ArrayList<>();

        public void addUser(User user) {
            users.add(user);
        }

        public Optional<User> login(String login, String password)
                throws UnauthorizedExcpetion {
            for (User u : users) {
                if (u.getLogin().equals(login)
                        && u.getPassword() == password.hashCode()
                        && !u.isBlocked()) {
                    return Optional.of(u);
                }
            }
            throw new UnauthorizedExcpetion("Invalid login or password");
        }
    }
