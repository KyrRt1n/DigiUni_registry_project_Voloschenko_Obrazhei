package ua.sopsany.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
public class User {

    private String login;
    private int password;

    @Setter
    private Role role;

    private boolean isBlocked;

    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password.hashCode();
        this.role = role;
        this.isBlocked = false;
    }

    public User(String login, int passwordHash, Role role, boolean isBlocked) {
        this.login = login;
        this.password = passwordHash;
        this.role = role;
        this.isBlocked = isBlocked;
    }

    public void setPassword(String password) {
        this.password = password.hashCode();
    }

    public void setBlockedStatus(boolean blocked) {
        this.isBlocked = blocked;
    }

    @Override
    public String toString() {
        return isBlocked
                ? "User: " + login + " | [" + role + "] | (blocked)"
                : "User: " + login + " | [" + role + "]";
    }
}