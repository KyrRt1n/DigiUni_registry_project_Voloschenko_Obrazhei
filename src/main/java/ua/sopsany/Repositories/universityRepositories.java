package ua.sopsany.Repositories;

import ua.sopsany.Models.Student;
import ua.sopsany.Models.Teacher;

import java.util.Arrays;


public class universityRepositories {
    private Student[] students = new Student[6];
    private Teacher[] teachers = new Teacher[6];

    private int studentCounter = 0;
    private int teacherCounter = 0;

    public void addStudent(Student student) {
        if (studentCounter == students.length)
            students = Arrays.copyOf(students, students.length * 2);

    }
    public Student[] getAllStudents() {
        return Arrays.copyOf(students, studentCounter);
    }

    public void addTeacher(Teacher teacher) {
        if (teacherCounter == teachers.length)
            teachers = Arrays.copyOf(teachers, teachers.length * 2);
    }

    public Teacher[] getAllTeachers() {
        return Arrays.copyOf(teachers, teacherCounter);
    }

}


