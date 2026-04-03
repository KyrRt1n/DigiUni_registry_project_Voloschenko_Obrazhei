package ua.sopsany.utils;

import ua.sopsany.auth.User;
import ua.sopsany.dto.FacultyStatsRecord;
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

    public Student findByStudentId(List<Student> students, String StudentID) {
        return students.stream().filter(s -> s.getStudentID().equalsIgnoreCase(StudentID)).toList().get(0);
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
}