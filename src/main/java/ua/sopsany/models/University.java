package ua.sopsany.models;

import java.util.Arrays;

public class University {
    private String fullName;
    private String shortName;
    private String city;
    private String address;

    private Faculty[] faculties;
    private int facultyCount;

    public University(String fullName, String shortName, String city, String address) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.city = city;
        this.address = address;
        this.faculties = new Faculty[3];
        this.facultyCount = 0;
    }

    public University(String fullName, String shortName) {
        this.fullName = fullName;
        this.shortName = shortName;
    }

    public void addFaculty(Faculty faculty) {
        if(facultyCount == faculties.length) {
            faculties = Arrays.copyOf(faculties, faculties.length + 5);
        }

        faculties[facultyCount] = faculty;
        facultyCount++;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFaculties(Faculty[] faculties) {
        this.faculties = faculties;
    }

    public void setFacultyCount(int facultyCount) {
        this.facultyCount = facultyCount;
    }

    public Faculty[] getFaculties() {
        return Arrays.copyOf(faculties, facultyCount);
    }

    public int getFacultyCount() {
        return facultyCount;
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
