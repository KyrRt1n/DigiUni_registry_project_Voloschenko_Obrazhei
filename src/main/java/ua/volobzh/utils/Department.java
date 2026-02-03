package ua.volobzh.utils;

public class Department {

    private int ID;
    private String name;
    //private String head; Sanya there must be zav kafedry
    private String office;

    private Object[] students; //There you need replace Object with Student class
    private int studentCount;

    private Object[] teachers; //analogichno
    private int teacherCount;

    public Department(int ID, String Name, String office) {
        this.ID = ID;
        this.name = Name;
        this.office = office;

        this.students = new Object[10];
        this.studentCount = 0;
        this.teachers = new Object[10];
        this.teacherCount = 0;
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

    @Override
    public String toString() {
        return "Department" + name;
    }

}
