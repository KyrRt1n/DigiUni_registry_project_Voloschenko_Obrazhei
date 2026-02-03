package ua.sopsany.Models;

public class Student extends Person {

    private int course;
    private String group;
    private int entryYear;
    private String formEducation;
    private String state;
    private String studentID;

    public Student(String name, String surname, String lastname, String birthday, String email, int phone, int id,
                   int course, String group, int entryYear, String formEducation, String state, String studentID) {
        super(name, surname, lastname, birthday, email, phone, id);
        this.course = course;
        this.group = group;
        this.entryYear = entryYear;
        this.formEducation = formEducation;
        this.state = state;
        this.studentID = studentID;
    }

    public int getCourse() {
        return course;
    }

    public String getGroup() {
        return group;
    }

    public int getEntryYear() {
        return entryYear;
    }

    public String getFormEducation() {
        return formEducation;
    }

    public String getState() {
        return state;
    }

    public String getStudentID() {
        return studentID;
    }

    @Override
    public String toString() {
        return super.toString() + "Student" + group + ", course " + course;
    }

}
