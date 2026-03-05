package ua.sopsany.utils;

import ua.sopsany.models.*;
import java.time.LocalDate;
import ua.sopsany.auth.AuthService;
import ua.sopsany.auth.User;
import ua.sopsany.auth.Role;


public class Repository {

    public static University createUniversity() {
        University university = new University("НаУКМА", "НаУКМА", "Київ", "Сковороди 2");

        Faculty fi = new Faculty(1, "Faculty of Informatics", "FI");
        university.addFaculty(fi);

        Faculty eco = new Faculty(2, "Faculty of Economics", "FOE");
        university.addFaculty(eco);

        Teacher Hlybovets = new Teacher("Andrii", "Mykolayovych", "Hlybovets",
                LocalDate.of(1970, 10, 1), "hlybaphp@gm", "0730737373", 91238761,
                "FI faculty decan", "Tech science doctor", "Professor",
                LocalDate.of(2012, 9, 1), 24);
        fi.setDecan(Hlybovets);

        Department se = new Department(1, "Software Engineering", "1-225");
        fi.addDepartment(se);

        se.addStudent(new Student("Sanya", "Valeriyovych", "Obrazhei",
                LocalDate.of(2007, 10, 11), "konodioda@gmail", "+380978620341",
                1488228, 1, "IPZ", 2025, Student.FormEducation.BUDGET, Student.StudentState.STUDYING, "148228"));

        se.addStudent(new Student("Artem", "Oleksiyovich", "Voloshchenko",
                LocalDate.of(2008, 1, 9), "aVoloshka666@gmail", "+380972417071",
                105105105, 1, "IPZ", 2025, Student.FormEducation.BUDGET, Student.StudentState.STUDYING, "105105105"));

        Department marketing = new Department(2, "Marketing", "6-406");
        eco.addDepartment(marketing);

        marketing.addStudent(new Student("Oleg", "Kyrylovych", "Kyrolov",
                LocalDate.of(2007, 7, 29), "OlegRagul@gmail", "+3809783493",
                1232343565, 2, "MARK", 2025, Student.FormEducation.BUDGET, Student.StudentState.STUDYING, "1232343565"));

        Department management = new Department(3, "Management", "6-407");
        eco.addDepartment(management);

        management.addStudent(new Student("Vlad", "Sergiyovich", "Hryn",
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