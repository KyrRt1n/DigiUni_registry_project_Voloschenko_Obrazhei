package ua.sopsany.models;

import java.time.LocalDate;

public class Student extends Person {

    public enum FormEducation {BUDGET, CONTRACT}
    public enum StudentState {STUDYING, ACADEMIC_LEAVE, DEDUCTED}

    private int course;
    private String group;
    private int entryYear;
    private String studentID;

    private FormEducation educationForm;
    private StudentState studentState;

    public Student(String name, String surname, String lastname, LocalDate birthday, String email, String phone, int id,
                   int course, String group, int entryYear, FormEducation eduForm, StudentState state, String studentID) {
        super(name, surname, lastname, birthday, email, phone, id);
        this.course = course;
        this.group = group;
        this.entryYear = entryYear;
        this.educationForm = eduForm;
        this.studentState = state;
        this.studentID = studentID;
    }

    public int getCourse() { return course; }
    public String getGroup() { return group; }
    public int getEntryYear() { return entryYear; }
    public String getStudentID() { return studentID; }

    public FormEducation getEducationForm() { return educationForm; }
    public StudentState getStudentState() { return studentState; }

    @Override
    public String toString() {
        return super.toString() + "Student: "+getName() + getSurname() +", Group " + group + ", course " + course;
    }
}