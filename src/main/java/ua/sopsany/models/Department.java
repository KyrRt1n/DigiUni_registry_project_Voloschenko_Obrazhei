package ua.sopsany.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ua.sopsany.utils.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Department {

    private int ID;
    private String name;
    private String office;
    private Teacher head;

    @JsonIgnore
    private Faculty faculty;

    private List<Student> students = new ArrayList<>();
    private List<Teacher> teachers = new ArrayList<>();

    public Department(int ID, String Name, String office, Teacher head, Faculty faculty) {
        this.ID = ID;
        this.name = Name;
        this.office = office;
        this.head = head;
        this.faculty = faculty;
    }

    public Department(int ID, String Name, String office, Teacher head) {
        this.ID = ID;
        this.name = Name;
        this.office = office;
        this.head = head;
    }

    public Department(int ID, String Name, String office) {
        this.ID = ID;
        this.name = Name;
        this.office = office;
    }

    public Department(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department() {}

    public void addStudent(Student student) {
        students.add(student);
    }

    public boolean removeStudentByLastName(String lastname) {
        return students.removeIf(s -> s.getLastname().equals(lastname));
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public Teacher getHead() {
        return head;
    }

    public void setHead(Teacher head) {
        this.head = head;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getOffice() {
        return office;
    }

    public List<Student> getStudents() {
        return students;
    }

    @Override
    public String toString() {
        String headName = (head != null) ? head.getLastname() : "not assigned";
        return "Department " + name + ", Head: " + headName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department department = (Department) o;
        return ID == department.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

}
