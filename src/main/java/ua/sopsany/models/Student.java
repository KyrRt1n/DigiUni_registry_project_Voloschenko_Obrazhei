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
        super(surname, name, lastname, birthday, email, phone, id);
        this.course = course;
        this.group = group;
        this.entryYear = entryYear;
        this.educationForm = eduForm;
        this.studentState = state;
        this.studentID = studentID;
    }

    public Student() {}

    public void setCourse(int course) {
        this.course = course;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setEntryYear(int entryYear) {
        this.entryYear = entryYear;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void setEducationForm(FormEducation educationForm) {
        this.educationForm = educationForm;
    }

    public void setStudentState(StudentState studentState) {
        this.studentState = studentState;
    }

    public Student(String surname, String name, String lastname) {
        super(surname, name, lastname);
    }

    public int getCourse() { return course; }
    public String getGroup() { return group; }
    public int getEntryYear() { return entryYear; }
    public String getStudentID() { return studentID; }

    public FormEducation getEducationForm() { return educationForm; }
    public StudentState getStudentState() { return studentState; }

    @Override
    public String toString() {
        return super.toString() + " | Group: " + group + ", Course: " + course;
    }
}