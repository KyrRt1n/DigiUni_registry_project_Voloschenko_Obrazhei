package ua.sopsany;

import ua.sopsany.models.*;
import ua.sopsany.utils.InputHandler;
import ua.sopsany.utils.Repository;

import java.time.LocalDate;

public class Main {

    static int uniFacultyCount;
    static int chosenFaculty;
    static int chosenDepartment;
    static String name, surname, lastname;
    static University university = Repository.createUniversity();
    static InputHandler input = new InputHandler();

    public static void main(String[] args) {
        Faculty fi = university.getFaculties()[0];
        Department se = fi.getDepartments()[0];

        while (true) {
            System.out.println("\n--- UNIVERSITY SYSTEM MENU ---");
            System.out.println("1. Print University Structure");
            System.out.println("2. Add Student");
            System.out.println("3. Find Student by Lastname (in SE)");
            System.out.println("4. Show all students");
            System.out.println("5. Remove Student");
            System.out.println("0. Exit");

            int choice = input.readInt("Select option", 0, 5);

            switch (choice) {
                case 1:
                    printUniStructure();
                    break;

                case 2:
                    addStudent();
                    break;

                case 3:
                    findStudentByLastname();
                    break;

                case 4:
                    printStudentList();
                    break;

                case 5:
                    studentRemoval();
                    break;

                case 0:
                    System.out.println("Goodbye!");
                    return;
            }
        }
    }

    private static void printUniStructure() {
        System.out.println("============");
        System.out.println("University: " + university.getFullName());
        for (Faculty f : university.getFaculties()) {
            System.out.println("  └Faculty: " + f);
            for (Department d : f.getDepartments()) {
                System.out.println("  └-Department: " + d.getName());
            }
        }
        System.out.println("============");
    }

    private static void addStudent() {
        System.out.println("Select faculty you want add Student to:");
        uniFacultyCount = university.getFacultyCount();
        for (int i = 0; i < uniFacultyCount; i++) {
            System.out.print(i +". " + university.getFaculties()[i] + " | ");
        }
        chosenFaculty = input.readInt("Your choice:" );

        System.out.println("Now select department you want add Student to:");
        for (int i = 0; i < university.getFaculties()[chosenFaculty].getDepartmentCount(); i++) {
            System.out.println(i +". " + university.getFaculties()[chosenFaculty].getDepartments()[i]);
        }
        chosenDepartment =  input.readInt("Your choice:" );

        System.out.println("--- Adding New Student ---");
        name = input.readString("Name");
        surname = input.readString("Surname");
        lastname = input.readString("Lastname");
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

        university.getFaculties()[chosenFaculty].getDepartments()[chosenDepartment].addStudent(newStudent);
        System.out.println("Student added successfully!");
    }

    private static void findStudentByLastname() {
        boolean found = false;
        name = input.readString("Enter lastname to search");
        for (Faculty f : university.getFaculties()) {
            for (Department d : f.getDepartments()) {
                for (Student s : d.getStudents()) {
                    if (s.getLastname().equalsIgnoreCase(name)) {
                        System.out.println("FOUND: " + s);
                        found = true;
                    }
                }
            }
        }
        if (!found) System.out.println("Student not found.");
    }

    private static void printStudentList() {
        System.out.println("--- ALL STUDENTS LIST ---");
        for (Faculty f : university.getFaculties()) {
            System.out.println("Faculty: " + f.getShortName());
            for (Department d : f.getDepartments()) {
                System.out.println("  └-Department: " + d.getName());
                for (Student s : d.getStudents()) {
                    System.out.println("    └- " + s);
                }
            }
        }
    }

    private static void studentRemoval() {
        boolean found = false;
        System.out.println("--- Student removal ---");
        name = input.readString("Student name to remove: ");
        for (Faculty f : university.getFaculties()) {
            if(found) break;

            for (Department d : f.getDepartments()) {
                found = d.removeStudentByLastName(name);
                if (found) System.out.println("Student removed successfully!");
            }
        }
        if (!found) System.out.println("Student not found!");
    }
}