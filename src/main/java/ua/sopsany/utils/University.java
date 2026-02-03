package ua.sopsany.utils;

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

    public Faculty[] getFaculties() {
        return Arrays.copyOf(faculties, facultyCount);
    }

    public int getFacultyCount() {
        return facultyCount;
    }

    public void addFaculty(Faculty faculty) {
        if(facultyCount == faculties.length) {
            faculties = Arrays.copyOf(faculties, faculties.length + 5);
        }

        faculties[facultyCount++] = faculty;
        facultyCount++;
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
