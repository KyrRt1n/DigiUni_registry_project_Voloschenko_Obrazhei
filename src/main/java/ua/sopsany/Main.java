package ua.sopsany;

import ua.sopsany.models.*;
import ua.sopsany.utils.InputHandler;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Teacher Hlybovets = new Teacher("Andrii", "Mykolayovych", "Hlybovets",
                LocalDate.of(1970, 10, 1), "hlybaphp@gm", "0730737373", 91238761,
        "Decan","Professor","academicTitle",
                LocalDate.of(2012, 9, 1), 24);

        University university = new University("НаУКМА", "НаУКМА", "Киев", "Сковороды 2");
        Faculty fi = new Faculty(1, "Faculty of Infomatics", "FI", Hlybovets,"044-111-22-33");
        university.addFaculty(fi);

        Department se = new Department(1, "Software Engineering", "1-225");
        fi.addDepartment(se);

        InputHandler input = new InputHandler();

        Student Sanya = new Student("Sanya", "Valeriyovych", "Obrazhei", LocalDate.of(2007, 10, 11),
                "konodioda", "+380978620341", 1488228, 1, "IPZ", 2025, Student.FormEducation.BUDGET , Student.StudentState.STUDYING, "148228");
        System.out.println("Sanya: " + Sanya);
        se.addStudent(Sanya);

        while (true) {
            System.out.println("\n--- UNIVERSITY SYSTEM MENU ---");
            System.out.println("1. Print University Structure");
            System.out.println("2. Add Student to Software Engineering");
            System.out.println("3. Find Student by Lastname (in SE)");
            System.out.println("4. Show all students in SE");
            System.out.println("0. Exit");

            int choice = input.readInt("Select option", 0, 4);

            switch (choice) {
                case 1:
                    System.out.println("============");
                    System.out.println("University: " + university.getFullName());
                    for (Faculty f : university.getFaculties()) {
                        System.out.println("  Faculty: " + f);
                        for (Department d : f.getDepartments()) {
                            System.out.println("    Department: " + d.getName());
                        }
                    }
                    System.out.println("============");
                    break;

                case 2:
                    System.out.println("--- Adding New Student ---");
                    String name = input.readString("Name");
                    String surname = input.readString("Surname");
                    String lastname = input.readString("Lastname");
                    LocalDate birthDate = input.readDate("Birthday");
                    String email = input.readString("Email");
                    String phone = input.readString("Phone");
                    int id = input.readInt("Personal ID");
                    int course = input.readInt("Course", 1, 6);
                    String group = input.readString("Group");
                    int year = input.readInt("Entry Year", 1990, 2025);
                    String studId = input.readString("Student Ticket ID");

                    Student newStudent = new Student(name, surname, lastname, birthDate, email, phone, id,
                            course, group, year, Student.FormEducation.BUDGET, Student.StudentState.STUDYING, studId);

                    se.addStudent(newStudent);
                    System.out.println("Student added successfully!");
                    break;

                case 3:
                    String searchName = input.readString("Enter lastname to search");
                    boolean found = false;
                    for (Student s : se.getStudents()) {
                        if (s.getLastname().equalsIgnoreCase(searchName)) {
                            System.out.println("FOUND: " + s);
                            found = true;
                        }
                    }
                    if (!found) System.out.println("Student not found.");
                    break;

                case 4:
                    System.out.println("--- Students in " + se.getName() + " ---");
                    for (Student s : se.getStudents()) {
                        System.out.println(s);
                    }
                    break;

                case 0:
                    System.out.println("Goodbye!");
                    return;
            }
        }
    }
}