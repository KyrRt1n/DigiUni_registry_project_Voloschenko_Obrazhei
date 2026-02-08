package ua.sopsany.models;

import java.util.Arrays;

public class Faculty {
    private int id;
    private String fullName;
    private String shortName;
    private Teacher decan;
    private String contacts;

    private Department[] departments;
    private int departmentCount;

    public Faculty(int id, String fullName, String shortName, Teacher decan, String contacts) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
        this.decan = decan;
        this.contacts = contacts;
        this.departments = new Department[3];
        this.departmentCount = 0;
    }

    public Faculty(int id, String fullName, String shortName) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
    }

    public Department[] getDepartments() {
        return Arrays.copyOf(this.departments, this.departmentCount);
    }

    public int getDepartmentCount() {
        return departmentCount;
    }

    public void setDepartmentCount(int departmentCount) {
        this.departmentCount = departmentCount;
    }

    public void setDepartments(Department[] departments) {
        this.departments = departments;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public void setDecan(Teacher decan) {
        this.decan = decan;
    }

    public void addDepartment(Department department) {
        if(departmentCount == departments.length)
            departments = Arrays.copyOf(departments, departments.length + 5);


        departments[departmentCount] = department;
        departmentCount++;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public Teacher getDecan() {
        return decan;
    }

    public String getContacts() {
        return contacts;
    }

    @Override
    public String toString() {
        return shortName + " - " + fullName + " (Contacts: " + contacts + ")";
    }
}
