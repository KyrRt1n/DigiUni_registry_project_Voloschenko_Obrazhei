package ua.sopsany.reflection;

import ua.sopsany.Main;
import ua.sopsany.auth.Role;
import ua.sopsany.models.Faculty;
import ua.sopsany.models.Student;
import ua.sopsany.models.Teacher;
import ua.sopsany.utils.Repository;

import java.util.List;
public class ReflectionDemo {

    @MenuItem(label = "Show total counts (all roles)", role = Role.USER, order = 1)
    public static void showCounts() {
        System.out.println("\n=== System Statistics ===");
        System.out.println("Faculties:   " + Repository.facultyRepo.getAll().size());
        System.out.println("Departments: " + Repository.departmentRepo.getAll().size());
        System.out.println("Teachers:    " + Repository.teacherRepo.getAll().size());
        System.out.println("Students:    " + Repository.studentRepo.getAll().size());
    }

    @MenuItem(label = "Show all students (USER+)", role = Role.USER, order = 2)
    public static void showAllStudents() {
        List<Student> students = Repository.studentRepo.getAll();
        if (students.isEmpty()) {
            System.out.println("No students in the system.");
            return;
        }
        System.out.println("\n--- All Students ---");
        students.forEach(s -> System.out.println("  " + s));
    }

    @MenuItem(label = "Show all teachers (MANAGER+)", role = Role.MANAGER, order = 3)
    public static void showAllTeachers() {
        List<Teacher> teachers = Repository.teacherRepo.getAll();
        if (teachers.isEmpty()) {
            System.out.println("No teachers in the system.");
            return;
        }
        System.out.println("\n--- All Teachers ---");
        teachers.forEach(t -> System.out.println("  " + t));
    }

    @MenuItem(label = "Validate first student (MANAGER+)", role = Role.MANAGER, order = 4)
    public static void validateFirstStudent() {
        List<Student> students = Repository.studentRepo.getAll();
        if (students.isEmpty()) {
            System.out.println("No students to validate.");
            return;
        }
        Student s = students.get(0);
        System.out.println("Validating: " + s);

        List<String> errors = Validator.validate(s);
        if (errors.isEmpty()) {
            System.out.println("Validation passed — no errors.");
        } else {
            System.out.println("Validation errors:");
            errors.forEach(err -> System.out.println("  - " + err));
        }
    }

    @MenuItem(label = "DANGER: print university pointer (ADMIN only)", role = Role.ADMIN, order = 5)
    public static void adminOnlyDemo() {
        System.out.println("University object reference: " + Main.university);
        System.out.println("Total faculties: " + Main.university.getFaculties().size());
        for (Faculty f : Main.university.getFaculties()) {
            System.out.println("  [" + f.getId() + "] " + f.getShortName());
        }
    }
}