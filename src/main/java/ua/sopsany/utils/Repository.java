package ua.sopsany.utils;

import ua.sopsany.models.*;
import ua.sopsany.auth.*;
import ua.sopsany.exceptions.*;

import java.time.LocalDate;

public class Repository {

    public static GenericRepository<Faculty> facultyRepo = new GenericRepository<>();
    public static GenericRepository<Department> departmentRepo = new GenericRepository<>();
    public static GenericRepository<Teacher> teacherRepo = new GenericRepository<>();
    public static GenericRepository<Student> studentRepo = new GenericRepository<>();

    public static void addStudent(Department dept, Student student) {
        dept.addStudent(student);
        try {
            studentRepo.add(student);
        } catch (DuplicateIdException e) {
            System.out.println("[WARN] Student already in repo: " + e.getMessage());
        }
    }

    public static void removeStudent(Department dept, Student student) {
        dept.getStudents().remove(student);
        try {
            studentRepo.remove(student);
        } catch (EntityNotFoundException e) {
            System.out.println("[WARN] Student not found in repo: " + e.getMessage());
        }
    }

    public static University createUniversity() {

        University university = new University("НаУКМА", "НаУКМА", "Київ", "Сковороди 2");

        Faculty fi = new Faculty(1, "Faculty of Informatics", "FI");
        university.addFaculty(fi);
        try { facultyRepo.add(fi); } catch (DuplicateIdException e) { System.out.println(e.getMessage()); }

        Faculty eco = new Faculty(2, "Faculty of Economics", "FOE");
        eco.setContacts("+380973274136");
        university.addFaculty(eco);
        try { facultyRepo.add(eco); } catch (DuplicateIdException e) { System.out.println(e.getMessage()); }

        Teacher hlybovets = new Teacher("Andrii", "Mykolayovych", "Hlybovets",
                LocalDate.of(1970, 10, 1), "hlybaphp@gm", "0730737373", 91238761,
                "FI faculty decan", "Tech science doctor", "Professor",
                LocalDate.of(2012, 9, 1), 24);
        fi.setDecan(hlybovets);
        try { teacherRepo.add(hlybovets); } catch (DuplicateIdException e) { System.out.println(e.getMessage()); }

        Department se = new Department(1, "Software Engineering", "1-225", hlybovets, fi);
        fi.addDepartment(se);
        try { departmentRepo.add(se); } catch (DuplicateIdException e) { System.out.println(e.getMessage()); }

        addStudent(se, new Student("Sanya", "Valeriyovych", "Obrazhei",
                LocalDate.of(2007, 11, 10), "konodioda@gmail", "+380978620341",
                1488228, 1, "IPZ", 2025, Student.FormEducation.BUDGET, Student.StudentState.STUDYING, "148228"));

        addStudent(se, new Student("Artem", "Oleksiyovich", "Voloshchenko",
                LocalDate.of(2008, 1, 9), "aVoloshka666@gmail", "+380972417071",
                105105105, 1, "IPZ", 2025, Student.FormEducation.BUDGET, Student.StudentState.STUDYING, "105105105"));

        // --- Faculty of Economics ---
        Teacher economicsHead = new Teacher("Iryna", "Petrivna", "Kovalchuk",
                LocalDate.of(1975, 3, 15), "ikovalchuk@ukma.edu.ua", "+380671234567", 55567890,
                "Department head", "Candidate of Sciences", "Associate Professor",
                LocalDate.of(2005, 9, 1), 20);
        try { teacherRepo.add(economicsHead); } catch (DuplicateIdException e) { System.out.println(e.getMessage()); }

        Department marketing = new Department(2, "Marketing", "6-406");
        marketing.setFaculty(eco);
        marketing.setHead(economicsHead);
        eco.addDepartment(marketing);
        try { departmentRepo.add(marketing); } catch (DuplicateIdException e) { System.out.println(e.getMessage()); }

        addStudent(marketing, new Student("Oleg", "Kyrylovych", "Kyrolov",
                LocalDate.of(2007, 7, 29), "OlegRagul@gmail", "+3809783493",
                1232343565, 2, "MARK", 2025, Student.FormEducation.BUDGET, Student.StudentState.STUDYING, "1232343565"));

        Department management = new Department(3, "Management", "6-407");
        management.setFaculty(eco);
        eco.addDepartment(management);
        try { departmentRepo.add(management); } catch (DuplicateIdException e) { System.out.println(e.getMessage()); }

        addStudent(management, new Student("Vlad", "Sergiyovich", "Hryn",
                LocalDate.of(2007, 9, 25), "LuxonGames@gmail", "+380973274136",
                235346456, 1, "MANEG", 2025, Student.FormEducation.BUDGET, Student.StudentState.STUDYING, "235346456"));

        return university;
    }

    public static AuthService createAuthService() {
        AuthService auth = new AuthService();
        auth.addUser(new User("user", "user123", Role.USER));
        auth.addUser(new User("manager", "mgr123", Role.MANAGER));
        auth.addUser(new User("admin", "admin123", Role.ADMIN));
        return auth;
    }
}
