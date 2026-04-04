package ua.sopsany;

import java.util.List;

import ua.sopsany.dto.FacultyStatsRecord;
import ua.sopsany.models.*;
import ua.sopsany.utils.FileStorageService;
import ua.sopsany.utils.InputHandler;
import ua.sopsany.utils.Repository;
import ua.sopsany.utils.SearchService;
import ua.sopsany.auth.AuthService;
import ua.sopsany.auth.User;
import ua.sopsany.auth.Role;
import ua.sopsany.exceptions.UnauthorizedException;

import java.time.LocalDate;

public class Main {

    public static University university;
    static InputHandler input = new InputHandler();
    static SearchService searchService = new SearchService();
    static AuthService authService = Repository.createAuthService();
    static User currentUser = null;
    static FileStorageService storage = new FileStorageService();

    public static void main(String[] args) {

        university = storage.loadUniversity();
        if (university == null) {
            university = Repository.createUniversity();
        } else {
            System.out.println("University data loaded from file!");
            Repository.syncRepos(university);
        }

        AuthService loadedAuth = new AuthService();
        storage.loadUsers(loadedAuth);
        if (!loadedAuth.getAllUsers().isEmpty()) {
            authService = loadedAuth;
        }
        startAutoSaveThread();
        while (true) {

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

            while (currentUser != null) {
                System.out.println("\n--- UNIVERSITY SYSTEM MENU ---");
                System.out.println("1. Search & Reports");

                if (currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.ADMIN) {
                    System.out.println("2. Manage Students");
                    System.out.println("3. Manage Teachers");
                    System.out.println("4. Manage Departments");
                    System.out.println("5. Manage Faculties");
                    if (currentUser.getRole() == Role.ADMIN)
                        System.out.println("6. Manage Users & Roles");
                    System.out.println("8. Save University Structure to file");
                }

                System.out.println("9. Logout");
                System.out.println("0. Exit Application");

                int choice = input.readInt("Select option", 0, 10);

                switch (choice) {
                    case 1:
                        searchAndReportsMenu();
                        break;
                    case 2:
                        if (currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.ADMIN) manageStudentsMenu();
                        else System.out.println("Access denied.");
                        break;
                    case 3:
                        if (currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.ADMIN) manageTeachersMenu();
                        else System.out.println("Access denied.");
                        break;
                    case 4:
                        if (currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.ADMIN) manageDepartmentsMenu();
                        else System.out.println("Access denied.");
                        break;
                    case 5:
                        if (currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.ADMIN) manageFacultiesMenu();
                        else System.out.println("Access denied.");
                        break;
                    case 6:
                        if (currentUser.getRole() == Role.ADMIN) adminMenu();
                        else System.out.println("Access denied.");
                        break;
                    case 8:
                        if (currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.ADMIN) {
                            System.out.println("Saving...");
                            storage.saveUni(university);
                            storage.saveUsers(authService);
                            System.out.println("University + Users saved successfully!");
                        }
                        else System.out.println("Access denied.");
                        break;
                    case 9:
                        System.out.println("Logging out...");
                        currentUser = null;
                        break;
                    case 0:
                        System.out.println("Goodbye!");
                        return;

                    default:
                        System.out.println("Invalid option.");
                }
            }
        }
    }
    private static void startAutoSaveThread() {
        Thread autoSave = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60_000); // 60 секунд
                    System.out.println("\n[AutoSave] Saving university data...");
                    storage.saveUni(university);
                    storage.saveUsers(authService);
                    System.out.println("[AutoSave] Done.");
                } catch (InterruptedException e) {
                    System.out.println("[AutoSave] Thread interrupted.");
                    break;
                }
            }
        });
        autoSave.setDaemon(true);
        autoSave.setName("AutoSaveThread");
        autoSave.start();
        System.out.println("[AutoSave] Background thread started (saves every 60 sec)");
    }
    private static void searchAndReportsMenu() {
        while (true) {
            System.out.println("\n--- Search & Reports ---");
            System.out.println("1. Print University Structure");
            System.out.println("2. Find Student/Teacher");
            System.out.println("3. Sort Students/Teachers");
            System.out.println("4. Show all students");
            System.out.println("5. Faculty Statistics Report");
            System.out.println("6. Sorted Students by Course");
            System.out.println("0. Go back");

            int choice = input.readInt("Select option", 0, 5);
            switch (choice) {
                case 1: printUniStructure(); break;
                case 2: findStudent(); break;
                case 3: sortStudents(); break;
                case 4: printStudentList(); break;
                case 5:
                    System.out.println("\n--- Faculty Statistics ---");
                    List<FacultyStatsRecord> stats = searchService.generateFacultyStatistics(university);
                    if (stats.isEmpty())
                        System.out.println("No data available.");
                    else
                        for (FacultyStatsRecord record : stats)
                            System.out.println(record.toReportLine());
                    break;

                case 6:
                    System.out.println("\n--- Students by course ---");
                    int c = input.readInt("Enter course", 1, 6);
                    List<Student> sortedStudents = searchService.getStudentsByCourseSorted(Repository.studentRepo.getAll(), c);
                    if (sortedStudents.isEmpty()) {
                        System.out.println("No students found on course " + c);
                    } else {
                        for (Student s : sortedStudents) {
                            System.out.println(s);
                        }
                    }
                    break;

                case 0: return;
            }
        }
    }

    private static void manageStudentsMenu() {
        while (true) {
            System.out.println("\n--- Manage Students ---");
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. Update Student Info");
            System.out.println("0. Go back");

            int choice = input.readInt("Select option", 0, 3);
            switch (choice) {
                case 1: addStudent(); break;
                case 2: studentRemoval(); break;
                case 3: studentUpdate(); break;
                case 0: return;
            }
        }
    }

    private static void manageTeachersMenu() {
        while (true) {
            System.out.println("\n--- Manage Teachers ---");
            System.out.println("1. Add Teacher");
            System.out.println("2. Remove Teacher");
            System.out.println("3. Show all Teachers");
            System.out.println("0. Go back");

            int choice = input.readInt("Select option", 0, 3);
            switch (choice) {
                case 1:
                    System.out.println("--- Adding New Teacher ---");
                    String name = input.readString("Name");
                    String surname = input.readString("Surname");
                    String lastname = input.readString("Lastname");
                    LocalDate birthDate = input.readDate("Birthday");
                    String email = input.readString("Email");
                    String phone = input.readString("Phone");
                    int id = input.readInt("Personal ID");
                    String position = input.readString("Position (e.g. Professor, Assistant)");
                    String academicDegree = input.readString("Academic Degree (e.g. PhD, Doctor)");
                    String academicTitle = input.readString("Academic Title");
                    LocalDate empDate = input.readDate("Date of Employment");
                    int workLoad = input.readInt("Workload (hours)");

                    Teacher newTeacher = new Teacher(name, surname, lastname, birthDate, email, phone, id,
                            position, academicDegree, academicTitle, empDate, workLoad);

                    try {
                        Repository.teacherRepo.add(newTeacher);
                        System.out.println("Teacher added successfully!");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("--- Removing Teacher ---");
                    String lastNameToRemove = input.readString("Enter teacher's lastname to remove");
                    Teacher teacherToRemove = null;

                    for (Teacher t : Repository.teacherRepo.getAll()) {
                        if (t.getLastname().equalsIgnoreCase(lastNameToRemove)) {
                            teacherToRemove = t;
                            break;
                        }
                    }

                    if (teacherToRemove != null) {
                        try {
                            Repository.teacherRepo.remove(teacherToRemove);
                            System.out.println("Teacher " + teacherToRemove.getLastname() + " removed successfully!");
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Teacher not found.");
                    }
                    break;

                case 3:
                    System.out.println("--- All Teachers ---");
                    List<Teacher> teachers = Repository.teacherRepo.getAll();
                    if (teachers.isEmpty()) {
                        System.out.println("No teachers found.");
                    } else {
                        for (Teacher t : teachers) {
                            System.out.println(t);
                        }
                    }
                    break;

                case 0:
                    return;
            }
        }
    }

    private static void manageDepartmentsMenu() {
        while (true) {
            System.out.println("\n--- Manage Departments ---");
            System.out.println("1. Add Department");
            System.out.println("2. Remove Department");
            System.out.println("3. Show all Departments");
            System.out.println("0. Go back");

            int choice = input.readInt("Select option", 0, 3);
            switch (choice) {
                case 1:
                    List<Faculty> faculties = university.getFaculties();
                    if (faculties.isEmpty()) {
                        System.out.println("Error: No faculties exist. Please add a faculty first.");
                        break;
                    }

                    System.out.println("Select faculty to add department to:");
                    for (int i = 0; i < faculties.size(); i++) {
                        System.out.println(i + ". " + faculties.get(i).getShortName() + " (" + faculties.get(i).getFullName() + ")");
                    }
                    int facIndex = input.readInt("Your choice", 0, faculties.size() - 1);
                    Faculty selectedFac = faculties.get(facIndex);

                    System.out.println("--- Adding New Department ---");
                    int deptId = input.readInt("Department ID");
                    String deptName = input.readString("Name (e.g. Software Engineering)");
                    String office = input.readString("Office (e.g. 1-225)");

                    Department newDept = new Department(deptId, deptName, office);
                    newDept.setFaculty(selectedFac);
                    selectedFac.addDepartment(newDept);

                    try {
                        Repository.departmentRepo.add(newDept);
                        System.out.println("Department added successfully to " + selectedFac.getShortName() + "!");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("--- Removing Department ---");
                    faculties = university.getFaculties();
                    if (faculties.isEmpty()) {
                        System.out.println("No faculties found.");
                        break;
                    }
                    System.out.println("Select faculty:");
                    for (int i = 0; i < faculties.size(); i++) {
                        System.out.println(i + ". " + faculties.get(i).getShortName());
                    }
                    int fIndex = input.readInt("Your choice", 0, faculties.size() - 1);
                    Faculty f = faculties.get(fIndex);

                    if (f.getDepartments().isEmpty()) {
                        System.out.println("No departments in this faculty.");
                        break;
                    }

                    System.out.println("Select department to remove:");
                    for (int i = 0; i < f.getDepartments().size(); i++) {
                        System.out.println(i + ". " + f.getDepartments().get(i).getName());
                    }
                    int dIndex = input.readInt("Your choice", 0, f.getDepartments().size() - 1);

                    Department deptToRemove = f.getDepartments().get(dIndex);
                    f.getDepartments().remove(deptToRemove);

                    try {
                        Repository.departmentRepo.remove(deptToRemove);
                        System.out.println("Department removed successfully!");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("--- All Departments ---");
                    for (Faculty fac : university.getFaculties()) {
                        System.out.println("Faculty: " + fac.getShortName());
                        for (Department d : fac.getDepartments()) {
                            System.out.println("  └- " + d);
                        }
                    }
                    break;

                case 0:
                    return;
            }
        }
    }

    private static void manageFacultiesMenu() {
        while (true) {
            System.out.println("\n--- Manage Faculties ---");
            System.out.println("1. Add Faculty");
            System.out.println("2. Remove Faculty");
            System.out.println("3. Show all Faculties");
            System.out.println("0. Go back");

            int choice = input.readInt("Select option", 0, 3);
            switch (choice) {
                case 1:
                    System.out.println("--- Adding New Faculty ---");
                    int id = input.readInt("Faculty ID");
                    String fullName = input.readString("Full Name (e.g. Faculty of Informatics)");
                    String shortName = input.readString("Short Name (e.g. FI)");
                    String contacts = input.readString("Contacts (Phone/Email)");

                    Faculty newFac = new Faculty(id, fullName, shortName, null, contacts);
                    university.addFaculty(newFac);

                    try {
                        Repository.facultyRepo.add(newFac);
                        System.out.println("Faculty added successfully!");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("--- Removing Faculty ---");
                    List<Faculty> faculties = university.getFaculties();
                    if (faculties.isEmpty()) {
                        System.out.println("No faculties found.");
                        break;
                    }

                    System.out.println("Select faculty to remove:");
                    for (int i = 0; i < faculties.size(); i++) {
                        System.out.println(i + ". " + faculties.get(i).getFullName());
                    }
                    int facIndex = input.readInt("Your choice", 0, faculties.size() - 1);

                    Faculty facToRemove = faculties.get(facIndex);
                    university.getFaculties().remove(facToRemove);

                    try {
                        Repository.facultyRepo.remove(facToRemove);
                        System.out.println("Faculty " + facToRemove.getShortName() + " removed successfully!");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("--- All Faculties ---");
                    if (university.getFaculties().isEmpty()) {
                        System.out.println("No faculties found.");
                    } else {
                        for (Faculty f : university.getFaculties()) {
                            System.out.println(f);
                        }
                    }
                    break;

                case 0:
                    return;
            }
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n--- ADMIN MENU (User Management) ---");
            System.out.println("1. Block/Unblock user");
            System.out.println("2. Remove user");
            System.out.println("3. Edit user");
            System.out.println("4. Show all users");
            System.out.println("0. Go back");

            int choice = input.readInt("Select option", 0, 4);
            switch (choice) {
                case 1: adminBlockUnblock(); break;
                case 2: adminRemoveUser(); break;
                case 3: adminEditUser(); break;
                case 4:
                    for (User u : authService.getAllUsers()) System.out.println(u);
                    break;
                case 0: return;
            }
        }
    }

    private static void adminEditUser() {
        System.out.println("\n--- Edit user ---");
        for (User u : authService.getAllUsers())
            System.out.println(u);

        String login = input.readString("Enter user's login");
        User user = authService.findByLogin(login);
        if(user == null) {
            System.out.println("User not found");
            return;
        }

        System.out.println("\nWhat do you want to change for user '" + login + "'? \n 1. Password \n 2. Role \n 0. Cancel");
        int choice = input.readInt("Select option", 0, 2);

        if (choice == 1) {
            String newPassword = input.readString("Enter new password");
            authService.changePassword(user, newPassword);
            System.out.println("Password changed successfully");
        }
        else if (choice == 2) {
            if(login.equals("admin") || login.equals(currentUser.getLogin())) {
                System.out.println("You can't change role of admin/yourself");
                return;
            }
            System.out.println("Current role: " + user.getRole());
            int newRole = input.readInt("Roles: 1. USER \n2. MANAGER \n3. ADMIN \n0. Cancel", 0, 3);

            if (newRole == 1) user.setRole(Role.USER);
            else if (newRole == 2) user.setRole(Role.MANAGER);
            else if (newRole == 3) user.setRole(Role.ADMIN);
            else {
                System.out.println("Cancelled");
                adminEditUser();
            }
        }
        else {
            System.out.println("Cancelled");
            adminMenu();
        }
    }

    private static void adminRemoveUser() {
        System.out.println("\n--- Remove user ---");
        for (User u : authService.getAllUsers())
            System.out.println(u);

        String login = input.readString("Enter user's login to remove or \"_\" to go back");
        if(login.equals("_")) {
            System.out.println("Cancelled");
            adminMenu();
            return;
        }
        User user = authService.findByLogin(login);
        if(user == null) {
            System.out.println("User not found");
        }
        else if(login.equals("admin") || login.equals(currentUser.getLogin())) {
            System.out.println("You can't remove admin or yourself");
        }
        else {
            authService.removeUser(login);
            System.out.println("User " + login + " removed");
        }
        adminMenu();
    }

    private static void adminBlockUnblock() {
        System.out.println("\n--- User block/unblock ---");
        for (User u : authService.getAllUsers())
            System.out.println(u);

        String login = input.readString("Enter user's login");
        User user = authService.findByLogin(login);
        if(user == null) {
            System.out.println("User not found");
            return;
        }
        else if(login.equals("admin") || login.equals(currentUser.getLogin())) {
            System.out.println("You can't block admin or yourself");
            return;
        }

        boolean block = input.readInt("Do you want block user(1) or unblock(0)?", 0, 1) == 1;

        if (user.isBlocked() && block) {
            System.out.println("User is already blocked");
            return;
        }
        if (!user.isBlocked() && !block) {
            System.out.println("User is already unblocked");
            return;
        }
        user.setBlockedStatus(block);
        System.out.println("User status updated.");
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
        int formEd = input.readInt("Form of education (1. Budget | 2. Contract)", 1, 2);
        int studState = input.readInt("Student state (1. STUDYING, 2. ACADEMIC_LEAVE, 3. DEDUCTED)", 1, 3);
        String email = input.readString("Email");
        String phone = input.readString("Phone");
        int id = input.readInt("Personal ID");
        int course = input.readInt("Course", 1, 6);
        String group = input.readString("Group");
        int year = input.readInt("Entry Year", 1990, 2025);
        String studId = input.readString("Student Ticket ID");

        Student.FormEducation formEducationEnum;
        if(formEd == 1) {
            formEducationEnum = Student.FormEducation.BUDGET;
        }
        else {
            formEducationEnum = Student.FormEducation.CONTRACT;
        }

        Student.StudentState studStateEnum;
        if(studState == 1) {
            studStateEnum = Student.StudentState.STUDYING;
        }
        else if(studState == 2) {
            studStateEnum = Student.StudentState.ACADEMIC_LEAVE;
        }
        else {
            studStateEnum = Student.StudentState.DEDUCTED;
        }

        Department targetDept = university.getFaculties().get(chosenFaculty).getDepartments().get(chosenDepartment);
        Student newStudent = new Student(nameToAdd, surnameToAdd, lastnameToAdd, birthDate, email, phone, id,
                course, group, year, formEducationEnum, studStateEnum, studId);

        Repository.addStudent(targetDept, newStudent);

        System.out.println("Student added successfully!");
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

    private static void sortStudents() {
        System.out.println("1. Sort students by lastname");
        System.out.println("2. Sort teachers by lastname");
        System.out.println("3. Sort students by course");
        System.out.println("4. Sort by faculty");
        System.out.println("5. Sort by department");
        System.out.println("6. Sort by group");
        System.out.println("0. Go back");

        List<Student> allStudents = Repository.studentRepo.getAll();
        List<Student> resultStud = List.of();
        List<Teacher> allTeachers = Repository.teacherRepo.getAll();
        List<Teacher> resultTeach = List.of();
        int sortOption = input.readInt("Your choice", 0, 6);

        switch (sortOption) {

        }
    }

    private static void findStudent() {
        System.out.println("\n1. Find students by course");
        System.out.println("2. Find students by group");
        System.out.println("3. Find students by full name");
        System.out.println("4. Find student by ticket ID");
        System.out.println("5. Find teacher by lastname");
        System.out.println("0. Go back");

        List<Student> allStudents = Repository.studentRepo.getAll();
        List<Student> resultStud = List.of();
        List<Teacher> allTeachers = Repository.teacherRepo.getAll();
        List<Teacher> resultTeach = List.of();
        int findingOption = input.readInt("Your choice", 0, 5);

        switch (findingOption) {
            case 1:
                int courseToFind = input.readInt("Select course");
                resultStud = searchService.findByCourse(allStudents, courseToFind);
                break;
            case 2:
                String groupToFind = input.readString("Select group");
                resultStud = searchService.findByGroup(allStudents, groupToFind);
                break;
            case 3:
                String lastnameToFind = input.readString("Enter lastname");
                String nameToFind = input.readString("Enter name");
                String surnameToFind = input.readString("Enter surname");
                resultStud = searchService.findByFullName(allStudents, lastnameToFind, nameToFind, surnameToFind);
                break;
            case 4:
                String studIdToFind = input.readString("Enter Student ID");
                Student found = searchService.findByStudentId(allStudents, studIdToFind);
                if (found != null) {
                    resultStud = List.of(found);
                }
                break;
            case 5:
                String teachLastnameToFind = input.readString("Enter teacher's lastname");
                resultTeach = searchService.findTeacherByLastName(allTeachers, teachLastnameToFind);
                break;
            case 0:
                return;
        }

        if (!resultStud.isEmpty() && resultStud.size() >= 1)
            resultStud.forEach(s -> System.out.println("FOUND: " + s));
        else if(!resultTeach.isEmpty() && resultTeach.size() >= 1)
            resultTeach.forEach(t -> System.out.println("FOUND: " + t));
        else
            System.out.println("Person not found.");
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