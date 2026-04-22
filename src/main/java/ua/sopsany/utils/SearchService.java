package ua.sopsany.utils;

import ua.sopsany.dto.FacultyStatsRecord;
import ua.sopsany.dto.StudentDTO;
import ua.sopsany.models.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SearchService {

    public List<Student> findByLastName(List<Student> students, String lastName) {
        return students.stream().filter(s -> s.getLastname().equalsIgnoreCase(lastName)).toList();
    }

    public List<Student> findByGroup(List<Student> students, String group) {
        return students.stream().filter(s -> s.getGroup().equalsIgnoreCase(group)).toList();
    }

    public List<Student> findByCourse(List<Student> students, int course) {
        return students.stream().filter(s -> s.getCourse() == course).toList();
    }

    public List<Student> sortByLastname(List<Student> students) {
        return students.stream().sorted(Comparator.comparing(Person::getLastname)).toList();
    }

    public List<Student> findByFullName(List<Student> students, String lastname, String name, String surname) {
        return students.stream().filter(s -> s.getSurname().equalsIgnoreCase(surname)
                && s.getLastname().equalsIgnoreCase(lastname)
                && s.getName().equalsIgnoreCase(name)).toList();
    }

    public List<Teacher> findTeacherByFullName(List<Teacher> teachers, String lastname, String name, String surname) {
        return teachers.stream().filter(t -> t.getSurname().equalsIgnoreCase(surname)
                && t.getLastname().equalsIgnoreCase(lastname)
                && t.getName().equalsIgnoreCase(name)).toList();
    }

    public Student findByStudentId(List<Student> students, String studentID) {
        return students.stream()
                .filter(s -> s.getStudentID().equalsIgnoreCase(studentID))
                .findFirst()
                .orElse(null);
    }

    public List<Teacher> findTeacherByLastName(List<Teacher> teachers, String lastname) {
        return teachers.stream().filter(t -> t.getLastname().equalsIgnoreCase(lastname)).toList();
    }

    public List<FacultyStatsRecord> generateFacultyStatistics(University university) {
        List<FacultyStatsRecord> statsList = new ArrayList<>();

        for (Faculty faculty : university.getFaculties()) {

            List<Student> facultyStudents = faculty.getDepartments().stream()
                    .flatMap(dept -> dept.getStudents().stream())
                    .toList();

            long totalStudents = facultyStudents.size();

            long budgetStudents = facultyStudents.stream()
                    .filter(s -> s.getEducationForm() == Student.FormEducation.BUDGET)
                    .count();

            long contractStudents = facultyStudents.stream()
                    .filter(s -> s.getEducationForm() == Student.FormEducation.CONTRACT)
                    .count();

            statsList.add(new FacultyStatsRecord(
                    faculty.getShortName(),
                    totalStudents,
                    budgetStudents,
                    contractStudents));
        }
        return statsList;
    }

    public List<Student> getStudentsByCourseSorted(List<Student> allStudents, int course) {
        return allStudents.stream()
                .filter(s -> s.getCourse() == course)
                .sorted(Comparator.comparing(Person::getLastname))
                .toList();
    }

    public List<Teacher> sortTeachersByLastname(List<Teacher> allTeachers) {
        return allTeachers.stream().sorted(Comparator.comparing(Person::getLastname)).toList();
    }

    public List<Student> sortStudentsByCourse(List<Student> allStudents) {
        return allStudents.stream().sorted(Comparator.comparing(Student::getCourse)).toList();
    }

    public List<Student> sortStudentsByGroup(List<Student> allStudents) {
        return allStudents.stream().sorted(Comparator.comparing(Student::getGroup)).toList();
    }

    public List<StudentDTO> generateFullStudentReport(University university) {
        return university.getFaculties().stream()
                .flatMap(f -> f.getDepartments().stream()
                        .flatMap(d -> d.getStudents().stream()
                                .map(s -> StudentDTO.from(s, f.getShortName(), d.getName()))
                        ))
                .sorted(Comparator.comparing(StudentDTO::fullName))
                .toList();
    }

    public java.util.Set<String> getAllUniqueGroups(List<Student> students) {
        return students.stream()
                .map(Student::getGroup)
                .collect(java.util.stream.Collectors.toSet());
    }

    public List<Student> getDeptStudentsByCourse(Department dept, int course) {
        return dept.getStudents().stream()
                .filter(s -> s.getCourse() == course)
                .toList();
    }

    public List<Student> getDeptStudentsByCourseAlpha(Department dept, int course) {
        return dept.getStudents().stream()
                .filter(s -> s.getCourse() == course)
                .sorted(Comparator.comparing(Person::getLastname))
                .toList();
    }

    public List<Student> getDeptStudentsSortedByCourse(Department dept) {
        return dept.getStudents().stream()
                .sorted(Comparator.comparing(Student::getCourse)
                        .thenComparing(Person::getLastname))
                .toList();
    }

    public List<Teacher> getFacultyTeachersAlpha(Faculty faculty) {
        java.util.LinkedHashSet<Teacher> collected = new java.util.LinkedHashSet<>();
        if (faculty.getDecan() != null) collected.add(faculty.getDecan());
        for (Department d : faculty.getDepartments()) {
            if (d.getHead() != null) collected.add(d.getHead());
            if (d.getTeachers() != null) collected.addAll(d.getTeachers());
        }
        return collected.stream()
                .sorted(Comparator.comparing(Person::getLastname))
                .toList();
    }

    public List<Teacher> getDeptTeachersAlpha(Department dept) {
        java.util.LinkedHashSet<Teacher> collected = new java.util.LinkedHashSet<>();
        if (dept.getHead() != null) collected.add(dept.getHead());
        if (dept.getTeachers() != null) collected.addAll(dept.getTeachers());
        return collected.stream()
                .sorted(Comparator.comparing(Person::getLastname))
                .toList();
    }
}