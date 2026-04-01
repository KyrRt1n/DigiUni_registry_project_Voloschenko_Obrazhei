package ua.sopsany.models;

import java.time.LocalDate;
import java.time.Period;

public class Teacher extends Person {

    private String position;
    private String academicDegree;
    private String academicTitle;
    private LocalDate dateOfEmployment;
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

    public void setPosition(String position) {
        this.position = position;
    }

    public void setAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    public void setDateOfEmployment(LocalDate dateOfEmployment) {
        this.dateOfEmployment = dateOfEmployment;
    }

    public void setWorkLoad(int workLoad) {
        this.workLoad = workLoad;
    }

    public int getWorkLoad() {
        return workLoad;
    }

    public LocalDate getDateOfEmployment() {
        return dateOfEmployment;
    }

    public String getAcademicTitle() {
            return academicTitle;
    }

    public String getAcademicDegree() {
        return academicDegree;
    }

    public String getPosition() {
        return position;
    }

    public int getYearsOfWork() {
        return Period.between(this.dateOfEmployment, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return super.toString() + " [Teacher: " + position + ", Work since: " + dateOfEmployment + "]";
    }
    }
