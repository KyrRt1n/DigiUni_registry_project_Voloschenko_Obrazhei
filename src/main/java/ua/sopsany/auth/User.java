package ua.sopsany.auth;

import ua.sopsany.auth.Role;

public class User {
    private String login;
    private int password;
    private Role role;
    private boolean isBlocked;


    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password.hashCode();
        this.role = role;
        this.isBlocked = false;
    }

    public String getLogin() {
        return login;
    }

    public int getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlockedStatus(boolean blocked) {
        isBlocked = blocked;
    }

    @Override
    public String toString() {
        if(isBlocked)
            return login + " | " + role + " | (blocked)";
        return login + " | " + role;
    }

}
