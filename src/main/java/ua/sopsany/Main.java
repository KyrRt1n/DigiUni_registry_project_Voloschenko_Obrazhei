package ua.sopsany;

import java.util.List;
import ua.sopsany.models.*;
import ua.sopsany.utils.InputHandler;
import ua.sopsany.utils.Repository;
import ua.sopsany.utils.SearchService;
import ua.sopsany.auth.AuthService;
import ua.sopsany.auth.User;
import ua.sopsany.auth.Role;
import ua.sopsany.exceptions.UnauthorizedExcpetion;

import java.time.LocalDate;

public class Main {

    public static University university = Repository.createUniversity();
    static InputHandler input = new InputHandler();
    static SearchService findStudent = new SearchService();
    static AuthService authService = Repository.createAuthService();
    static User currentUser = null;

    public static void main(String[] args) {

        while (currentUser == null) {
            System.out.println("\n=== LOGIN ===");
            String login = input.readString("Login");
            String password = input.readString("Password");
            try {
                currentUser = authService.login(login, password).get();
                System.out.println("Welcome, " + currentUser.getLogin() + " [" + currentUser.getRole() + "]");
            } catch (UnauthorizedExcpetion e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        while (true) {
            System.out.println("\n--- UNIVERSITY SYSTEM MENU ---");

            System.out.println("1. Print University Structure");
            System.out.println("2. Find Student");
            System.out.println("3. Show all students");

            if (currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.ADMIN) {
                System.out.println("4. Add Student");
                System.out.println("5. Remove Student");
                System.out.println("6. Update Student");
            }

            System.out.println("0. Exit");

            int choice = input.readInt("Select option", 0, 6);

            switch (choice) {
                case 1:
                    printUniStructure();
                    break;

                case 2:
                    findStudent();
                    break;

                case 3:
                    printStudentList();
                    break;

                case 4:
                    if (currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.ADMIN) {
                        addStudent();
                    } else {
                        System.out.println("Access denied.");
                    }
                    break;

                case 5:
                    if (currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.ADMIN) {
                        studentRemoval();
                    } else {
                        System.out.println("Access denied.");
                    }
                    break;

                case 6:
                    if (currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.ADMIN) {
                        studentUpdate();
                    } else {
                        System.out.println("Access denied.");
                    }
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
                    if (foundStudent != null) break;
                }
                if (foundStudent != null) break;
            }
            if (foundStudent != null) break;
        }
        if (foundStudent == null) {
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
        List<Faculty> faculties = university.getFaculties();
        for (int i = 0; i < faculties.size(); i++) {
            System.out.print(i + ". " + faculties.get(i) + " | ");
        }
        int chosenFaculty = input.readInt("Your choice:");

        System.out.println("Now select department you want add Student to:");
        Faculty chosenFac = university.getFaculties().get(chosenFaculty);
        for (int i = 0; i < chosenFac.getDepartments().size(); i++) {
            System.out.println(i + ". " + chosenFac.getDepartments().get(i));
        }
        int chosenDepartment = input.readInt("Your choice:");

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

        university.getFaculties().get(chosenFaculty).getDepartments().get(chosenDepartment).addStudent(newStudent);
        System.out.println("Student added successfully!");
    }

    private static void findStudent() {
        System.out.println("1. Find students by course");
        System.out.println("2. Find students by group");
        System.out.println("3. Find students by full name");
        System.out.println("4. Sort students by lastname");
        System.out.println("0. Go back");
        List<Student> allStudents = Repository.studentRepo.getAll();
        List<Student> result = List.of();
        int findingOption = input.readInt("Your choice:", 0, 4);

        switch (findingOption) {
            case 1:
                int courseToFind = input.readInt("Select course:");
                result = findStudent.findByCourse(allStudents, courseToFind);
                break;
            case 2:
                String groupToFind = input.readString("Select group:");
                result = findStudent.findByGroup(allStudents, groupToFind);
                break;
            case 3:
                String lastnameToFind = input.readString("Enter lastname");
                String nameToFind = input.readString("Enter name");
                String surnameToFind = input.readString("Enter surname");
                result = findStudent.findByFullName(allStudents, lastnameToFind, nameToFind, surnameToFind);
                break;
            case 4:
                result = findStudent.sortByLastname(allStudents);
                break;
            case 0:
                return;
        }

        if (!result.isEmpty())
            result.forEach(s -> System.out.println("FOUND: " + s));
        else
            System.out.println("Student not found.");


    }

    private static void printStudentList() {
        System.out.println("--- ALL STUDENTS LIST ---");
        for (Faculty f : university.getFaculties()) {
            System.out.println("Faculty: " + f.getShortName());
            for (Department d : f.getDepartments()) {
                System.out.println("    └-Department: " + d.getName());
                for (Student s : d.getStudents()) {
                    System.out.println("        └- " + s);
                }
            }
        }
    }

    private static void studentRemoval() {
        boolean found = false;
        System.out.println("--- Student removal ---");
        String nameToRemove = input.readString("Student lastname to remove:");
        for (Faculty f : university.getFaculties()) {
            if (found) break;
            for (Department d : f.getDepartments()) {
                found = d.removeStudentByLastName(nameToRemove);
                if (found) System.out.println("Student removed successfully!");
            }
        }
        if (!found) System.out.println("Student not found!");
    }
}