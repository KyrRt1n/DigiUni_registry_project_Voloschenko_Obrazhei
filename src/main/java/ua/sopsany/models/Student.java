package ua.sopsany.models;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import ua.sopsany.reflection.Validate;

public final class Student extends Person {

    public enum FormEducation {BUDGET, CONTRACT}
    public enum StudentState {STUDYING, ACADEMIC_LEAVE, DEDUCTED}


    @Getter
    @lombok.Setter
    @Validate(min = 1, max = 6)
    private int course;

    @Getter
    @Setter
    @Validate(notNull = true, minLength = 1, maxLength = 20)
    private String group;

    @Getter
    @Setter
    @Validate(min = 1990, max = 2025)
    private int entryYear;

    @Getter
    @Setter
    @Validate(notNull = true, minLength = 3, maxLength = 20)
    private String studentID;

    @Getter
    @Setter
    private FormEducation educationForm;
    @Getter
    @Setter
    private StudentState studentState;

    public Student(String name, String surname, String lastname, LocalDate birthday, String email, String phone, int id,
                   int course, String group, int entryYear, FormEducation eduForm, StudentState state, String studentID) {
        super(surname, name, lastname, birthday, email, phone, id);
        this.course = course;
        this.group = group;
        this.entryYear = entryYear;
        this.educationForm = eduForm;
        this.studentState = state;
        this.studentID = studentID;
    }

    public Student() {}

    public Student(String surname, String name, String lastname) {
        super(surname, name, lastname);
    }

    @Override
    public String toString() {
        return super.toString() + " | Group: " + group + ", Course: " + course;
    }
}