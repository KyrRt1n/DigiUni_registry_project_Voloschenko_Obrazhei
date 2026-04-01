package ua.sopsany.dto;

import ua.sopsany.models.Student;

public record StudentDTO(
        int id,
        String fullName,
        int course,
        String group,
        String faculty,
        String department,
        String educationForm,
        String state
) {


    public static StudentDTO from(Student student, String facultyName, String deptName) {
        return new StudentDTO(
                student.getId(),
                student.getLastname() + " " + student.getName() + " " + student.getSurname(),
                student.getCourse(),
                student.getGroup(),
                facultyName,
                deptName,
                student.getEducationForm().toString(),
                student.getStudentState().toString()
        );
    }

    public String toReportLine() {
        return String.format("%-35s | Курс: %d | Група: %-8s | %-10s | %-10s | %s | %s",
                fullName(), course(), group(), faculty(), department(), educationForm(), state());
    }
}