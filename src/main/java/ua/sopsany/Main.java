package ua.sopsany;

import java.util.List;
import ua.sopsany.models.*;
import ua.sopsany.utils.InputHandler;
import ua.sopsany.utils.Repository;
import ua.sopsany.utils.SearchService;
import ua.sopsany.auth.AuthService;
import ua.sopsany.auth.User;
import ua.sopsany.auth.Role;
import ua.sopsany.exceptions.UnauthorizedException;

import java.time.LocalDate;

public class Main {

    public static University university = Repository.createUniversity();
    static InputHandler input = new InputHandler();
    static SearchService searchService = new SearchService();
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
            } catch (UnauthorizedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        while (true) {
            System.out.println("\n--- UNIVERSITY SYSTEM MENU ---");

            System.out.println("1. Print University Structure");
            System.out.println("2. Find Student");
            System.out.println("3. Show all students");

            if (currentUser.getRole() == Role.ADMIN) {
                System.out.println("4. Add Student");
                System.out.println("5. Remove Student");
                System.out.println("6. Update Student");
                System.out.println("7. Admin Menu");
            }
            else if (currentUser.getRole() == Role.MANAGER ) {
                System.out.println("4. Add Student");
                System.out.println("5. Remove Student");
                System.out.println("6. Update Student");
            }

            System.out.println("0. Exit");

            int choice = input.readInt("Select option", 0, 7);

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
                    if (currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.ADMIN) addStudent();
                    else System.out.println("Access denied.");
                    break;
                case 5:
                    if (currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.ADMIN) studentRemoval();
                    else System.out.println("Access denied.");
                    break;
                case 6:
                    if (currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.ADMIN) studentUpdate();
                    else System.out.println("Access denied.");
                    break;
                case 7:
                    if(currentUser.getRole() == Role.ADMIN)
                        adminMenu();
                    else
                        System.out.println("Access denied.");
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    return;
            }
        }
    }

    private static void adminMenu() {
        System.out.println("--- ADMIN MENU ---");
        System.out.println("1. Block/Unblock user");
        System.out.println("2. Remove user");
        System.out.println("3. Edit user");
        System.out.println("4. Show all users");

        int choice = input.readInt("Select option", 0, 4);
        switch (choice) {
            case 1:
                System.out.println("Edit users");
                adminBlockUnblock();
                break;
            case 2:
                System.out.println("Edit departments");
                break;
        }

    }

    private static void adminBlockUnblock() {
        System.out.println("--- User block/unblock ---");
        boolean block;
        if(input.readInt("Do you want block user(1) or unblock(0)?", 0, 1) == 1) {
            block = true;
        } else {
            block = false;
        }

        String login = input.readString("Enter user's login");
        if(login == null) {
            System.out.println("User not found");
        }
        else if(login == "admin") {
            System.out.println("You can't block admin");
        }
        else{
            authService.findByLogin(login).setBlockedStatus(block);
        }


    }

    private static Student findStudentInteractively(String actionName) {
        String lastname = input.readString("Enter student's lastname to " + actionName);
        List<Student> allStudents = Repository.studentRepo.getAll();
        List<Student> matches = searchService.findByLastName(allStudents, lastname);

        if (matches.isEmpty()) {
            System.out.println("Student not found.");
            return null;
        } else if (matches.size() == 1) {
            return matches.get(0);
        }

        System.out.println("Found multiple students with lastname '" + lastname + "':");
        for (Student s : matches) {
            System.out.println(s + " [Ticket ID: " + s.getStudentID() + ", Faculty/Group: " + s.getGroup() + "]");
        }

        String studentID = input.readString("Please enter the precise Student ID to confirm");
        Student exactStudent = searchService.findByStudentId(matches, studentID);

        if (exactStudent == null) {
            System.out.println("Student with such Ticket ID not found among the matches.");
        }
        return exactStudent;
    }

    private static void studentUpdate() {
        System.out.println("=== Student info updater ===");
        Student foundStudent = findStudentInteractively("update");

        if (foundStudent == null) return;

        System.out.println("Found: " + foundStudent);
        System.out.println("What do you wanna update?");
        System.out.println("1. Course");
        System.out.println("2. Group");
        System.out.println("0. Cancel");
        int choiceToUpd = input.readInt("Select option", 0, 2);

        switch (choiceToUpd) {
            case 1:
                foundStudent.setCourse(input.readInt("Course", 1, 6));
                System.out.println("Course updated!");
                break;
            case 2:
                foundStudent.setGroup(input.readString("Group"));
                System.out.println("Group updated!");
                break;
            case 0:
                System.out.println("Update cancelled");
                break;
        }
    }

    private static void studentRemoval() {
        System.out.println("--- Student removal ---");
        Student foundStudent = findStudentInteractively("remove");

        if (foundStudent == null) return;

        Department foundDept = null;

        for (Faculty f : university.getFaculties()) {
            for (Department d : f.getDepartments()) {
                boolean isHere = d.getStudents().stream()
                        .anyMatch(s -> s.getStudentID().equals(foundStudent.getStudentID()));
                if (isHere) {
                    foundDept = d;
                    break;
                }
            }
            if (foundDept != null) break;
        }

        if (foundDept != null) {
            Repository.removeStudent(foundDept, foundStudent);
            System.out.println("Student " + foundStudent.getLastname() + " removed successfully!");
        } else {
            System.out.println("Error: Student found in global list but missing from departments.");
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
            System.out.print(i + ". " + faculties.get(i) + "\n");
        }
        int chosenFaculty = input.readInt("Your choice", 0 , faculties.size() - 1);

        System.out.println("Now select department you want add Student to:");
        Faculty chosenFac = university.getFaculties().get(chosenFaculty);
        if (chosenFac.getDepartments().isEmpty()) {
            System.out.println("There is no departments in this faculty. Please select another faculty.");
            return;
        }
        for (int i = 0; i < chosenFac.getDepartments().size(); i++) {
            System.out.println(i + ". " + chosenFac.getDepartments().get(i));
        }
        int chosenDepartment = input.readInt("Your choice", 0, chosenFac.getDepartments().size() - 1);

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

        Department targetDept = university.getFaculties().get(chosenFaculty).getDepartments().get(chosenDepartment);
        Student newStudent = new Student(nameToAdd, surnameToAdd, lastnameToAdd, birthDate, email, phone, id,
                course, group, year, Student.FormEducation.BUDGET, Student.StudentState.STUDYING, studId);

        Repository.addStudent(targetDept, newStudent);

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
                result = searchService.findByCourse(allStudents, courseToFind);
                break;
            case 2:
                String groupToFind = input.readString("Select group:");
                result = searchService.findByGroup(allStudents, groupToFind);
                break;
            case 3:
                String lastnameToFind = input.readString("Enter lastname");
                String nameToFind = input.readString("Enter name");
                String surnameToFind = input.readString("Enter surname");
                result = searchService.findByFullName(allStudents, lastnameToFind, nameToFind, surnameToFind);
                break;
            case 4:
                result = searchService.sortByLastname(allStudents);
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
}