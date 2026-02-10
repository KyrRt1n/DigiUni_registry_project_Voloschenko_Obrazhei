package ua.sopsany.utils;

import ua.sopsany.models.*;
import java.time.LocalDate;

public class Repository {

    public static University createUniversity() {
        University university = new University("НаУКМА", "НаУКМА", "Киев", "Сковороды 2");

        Faculty fi = new Faculty(1, "Faculty of Infomatics", "FI");
        university.addFaculty(fi);

        Teacher Hlybovets = new Teacher("Andrii",
                "Mykolayovych",
                "Hlybovets",
                LocalDate.of(1970, 10, 1),
                "hlybaphp@gm",
                "0730737373",
                91238761,
                "FI faculty decan",
                "Tech science doctor",
                "Professor",
                LocalDate.of(2012, 9, 1),
                24);

        fi.setDecan(Hlybovets);

        Department se = new Department(1, "Software Engineering", "1-225");
        fi.addDepartment(se);

        Student Sanya = new Student("Sanya", "Valeriyovych", "Obrazhei", LocalDate.of(2007, 10, 11),
                "konodioda", "+380978620341", 1488228, 1, "IPZ", 2025, Student.FormEducation.BUDGET , Student.StudentState.STUDYING, "148228");
        System.out.println("Sanya: " + Sanya);
        se.addStudent(Sanya);

        return university;
    }
}