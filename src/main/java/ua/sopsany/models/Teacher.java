package ua.sopsany.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.Period;

public final class Teacher extends Person {

    @lombok.Getter
    @lombok.Setter
    private String position;
    @lombok.Getter
    @lombok.Setter
    private String academicDegree;
    @lombok.Getter
    @lombok.Setter
    private String academicTitle;
    @lombok.Getter
    @lombok.Setter
    private LocalDate dateOfEmployment;
    @lombok.Getter
    @lombok.Setter
    private int workLoad;

    public Teacher(String name, String surname, String lastname, LocalDate birthday, String email, String phone, int id,
                   String position, String academicDegree, String academicTitle, LocalDate dateOfEmployment, int workLoad) {
        super(surname, name, lastname, birthday, email, phone, id);
        this.position = position;
        this.academicDegree = academicDegree;
        this.academicTitle = academicTitle;
        this.dateOfEmployment = dateOfEmployment;
        this.workLoad = workLoad;
    }

    public Teacher(String surname, String name, String lastname) {
        super(surname, name, lastname);
    }

    public Teacher() {}

    @JsonIgnore
    public int getYearsOfWork() {
        return Period.between(this.dateOfEmployment, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return super.toString() + " [Teacher: " + position + ", Workload: " + workLoad + "h" + ", Work since: " + dateOfEmployment + "]";
    }
    }
