package ua.sopsany.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public sealed abstract class Person implements Identifiable<Integer> permits Teacher, Student {

    @Getter
    private String name;
    @Getter
    private String surname;
    @Getter
    private String lastname;
    @Getter
    @Setter
    private LocalDate birthday;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String phone;
    @Setter
    private int id;

    public Person(String surname, String name, String lastname, LocalDate birthday, String email, String phone, int id) {
        this.surname = surname;
        this.name = name;
        this.lastname = lastname;
        this.birthday = birthday;
        this.email = email;
        this.phone = phone;
        this.id = id;
    }

    public Person(String surname, String name, String lastname) {
        this.surname = surname;
        this.name = name;
        this.lastname = lastname;
    }

    public Person() {}

    @Override
    public Integer getId() {
        return id;
    }

    @JsonIgnore
    public int getAge() {
        if (birthday == null){
            return 0;
        }
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "№" + id + ": " + lastname + " " + name + " " + surname + " (" + phone + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}