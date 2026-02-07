package ua.sopsany.models;

public class Department {

    private int ID;
    private String name;
    private Teacher head;
    private String office;

    private Student[] students;
    private int studentsCount;

    private Teacher[] teachers;
    private int teachersCount;

    public Department(int ID, String Name, String office) {
        this.ID = ID;
        this.name = Name;
        this.office = office;

        this.students = new Student[10];
        this.studentsCount = 0;
        this.teachers = new Teacher[10];
        this.teachersCount = 0;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getOffice() {
        return office;
    }

    public void addStudent(Student student) {
        if (studentsCount == students.length) {
            students = java.util.Arrays.copyOf(students, students.length + 5);
        }
        students[studentsCount] = student;
        studentsCount++;
    }

    public Student[] getStudents() {
        return java.util.Arrays.copyOf(students, studentsCount);
    }

    @Override
    public String toString() {
        return "Department" + name;
    }

}
