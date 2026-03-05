package ua.sopsany.models;

import java.util.ArrayList;
import java.util.List;

public class University {
    private String fullName;
    private String shortName;
    private String city;
    private String address;

    private List<Faculty> faculties = new ArrayList<>();

    public University(String fullName, String shortName, String city, String address) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.city = city;
        this.address = address;

    }

    public University(String fullName, String shortName) {
        this.fullName = fullName;
        this.shortName = shortName;
    }

    public void addFaculty(Faculty faculty) {
        faculties.add(faculty);
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public String getFullName() {
        return fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }
}
