package ua.sopsany.models;

import java.time.LocalDate;

public abstract class Person {

    private String name;
    private String surname;
    private String lastname;
    private LocalDate birthday;
    private String email;
    private String phone;
    private int id;



    public Person(String name, String surname, String lastname, LocalDate birthday, String email, String phone, int id) {
        this.name = name;
        this.surname = surname;
        this.lastname = lastname;
        this.birthday = birthday;
        this.email = email;
        this.phone = phone;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLastname() {
        return lastname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "№" +id + ": " + name + ", " + surname + " (" + phone + ")";
    }

}
