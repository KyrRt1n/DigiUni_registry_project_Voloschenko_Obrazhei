package ua.sopsany;

import ua.sopsany.models.*;
import ua.sopsany.utils.InputHandler;
import ua.sopsany.utils.Repository;
import ua.sopsany.utils.StudentFinder;

import java.time.LocalDate;

public class Main {

    public static University university = Repository.createUniversity();
    static InputHandler input = new InputHandler();
    static StudentFinder findStudent = new StudentFinder();

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n--- UNIVERSITY SYSTEM MENU ---");
            System.out.println("1. Print University Structure");
            System.out.println("2. Add Student");
            System.out.println("3. Find Student");
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
                    findStudent();
                    break;

                case 4:
                    printStudentList();
                    break;

                case 5:
                    studentRemoval();
                    break;

                case 6:
                    studentUpdate();
                    break;

                case 0:
                    System.out.println("Goodbye!");
                    return;
            }
        }
    }

    private static void studentUpdate() {
        System.out.println("=== Student info updater ===");
        String lastnameToUpd = input.readString("Lastname");
        Student foundStudent = null;
        for (Faculty f : university.getFaculties()) {
            for (Department d : f.getDepartments()) {
                for (Student s : d.getStudents()) {
                    if (s.getLastname().equalsIgnoreCase(lastnameToUpd)) {
                        System.out.println("FOUND: " + s);
                        foundStudent = s;
                        break;
                    }
                    if (foundStudent!=null) break;
                }
                if (foundStudent!=null) break;
            }
            if (foundStudent!=null) break;
        }
        if (foundStudent==null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("What do you wanna update?");
        System.out.println("1. Course");
        System.out.println("2. Group");
        System.out.println("0. Cancel");
        int choiceToUpd = input.readInt("Select option", 0, 2);
        switch (choiceToUpd) {
            case 1:
                foundStudent.setCourse(input.readInt("Course", 1, 6));
                break;
            case 2:
                foundStudent.setGroup(input.readString("Group"));
                break;
            case 0:
                System.out.println("Update cancelled");
                break;
        }

    }

    private static void printUniStructure() {
        System.out.println("============");
        System.out.println("University: " + university.getFullName());
        for (Faculty f : university.getFaculties()) {
            System.out.println("  └Faculty: " + f);
            for (Department d : f.getDepartments()) {
                System.out.println("   └-Department: " + d.getName());
            }
        }
        System.out.println("============");
    }

    private static void addStudent() {
        System.out.println("Select faculty you want add Student to:");
        int uniFacultyCount = university.getFacultyCount();
        for (int i = 0; i < uniFacultyCount; i++) {
            System.out.print(i +". " + university.getFaculties()[i] + " | ");
        }
        int chosenFaculty = input.readInt("Your choice:" );

        System.out.println("Now select department you want add Student to:");
        for (int i = 0; i < university.getFaculties()[chosenFaculty].getDepartmentCount(); i++) {
            System.out.println(i +". " + university.getFaculties()[chosenFaculty].getDepartments()[i]);
        }
        int chosenDepartment =  input.readInt("Your choice:" );

        System.out.println("--- Adding New Student ---");
        String nameToAdd = input.readString("Name");
        String surnameToAdd = input.readString("Surname");
        String lastnameToAdd = input.readString("Lastname");
        LocalDate birthDate = input.readDate("Birthday");
        String email = input.readString("Email");
        String phone = input.readString("Phone");
        int id = input.readInt("Personal ID");
        int course = input.readInt("Course", 1, 6);
        String group = input.readString("Group");
        int year = input.readInt("Entry Year", 1990, 2025);
        String studId = input.readString("Student Ticket ID");

        Student newStudent = new Student(nameToAdd, surnameToAdd, lastnameToAdd, birthDate, email, phone, id,
                course, group, year, Student.FormEducation.BUDGET, Student.StudentState.STUDYING, studId);

        university.getFaculties()[chosenFaculty].getDepartments()[chosenDepartment].addStudent(newStudent);
        System.out.println("Student added successfully!");
    }

    private static void findStudent() {
        boolean found = false;

        System.out.println("1. Find students in course of your choice");
        System.out.println("2. Find students in group of your choice");
        System.out.println("3. Find students by Name, Lastname and Surname");
        System.out.println("0. Go back");
        int findingOption = input.readInt("How to you wanna find student?", 0, 3);

        switch (findingOption) {
            case 1:
                int courseToFind = input.readInt("Select course:");
                findStudent.ByCourse(courseToFind);
                break;
            case 2:
                String groupToFind = input.readString("Select group:");
                findStudent.ByGroup(groupToFind);
                break;
            case 3:
                String lastnameToFind = input.readString("Enter lastname to search");
                String nameToFind = input.readString("Enter student name to search");
                String surnameToFind = input.readString("Enter surname to search");
                findStudent.ByFullName(nameToFind, lastnameToFind,  surnameToFind);
                break;
            case 0: return;
        }

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
        String nameToRemove = input.readString("Student name to remove: ");
        for (Faculty f : university.getFaculties()) {
            if(found) break;

            for (Department d : f.getDepartments()) {
                found = d.removeStudentByLastName(nameToRemove);
                if (found) System.out.println("Student removed successfully!");
            }
        }
        if (!found) System.out.println("Student not found!");
    }
}