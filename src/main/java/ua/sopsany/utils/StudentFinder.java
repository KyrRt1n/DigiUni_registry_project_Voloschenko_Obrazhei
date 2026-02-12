package ua.sopsany.utils;

import ua.sopsany.Main;
import ua.sopsany.models.Department;
import ua.sopsany.models.Faculty;
import ua.sopsany.models.Student;

public class StudentFinder {

    static InputHandler input = new InputHandler();

    public void ByFullName(String nameToFind, String lastnameToFind, String surnameToFind){
        Student foundStudent = null;
        boolean found = false;
        for (Faculty f : Main.university.getFaculties()) {
            for (Department d : f.getDepartments()) {
                for (Student s : d.getStudents()) {
                    if (s.getLastname().equalsIgnoreCase(lastnameToFind) &&  s.getSurname().equalsIgnoreCase(surnameToFind) && s.getName().equalsIgnoreCase(nameToFind)) {
                        System.out.println("FOUND: " + s);
                        found = true;
                    }
                }
            }
        }
        if (!found) System.out.println("Student not found.");

    }

    public void ByGroup(String group){
        boolean found = false;
        for (Faculty f : Main.university.getFaculties()) {
            for (Department d : f.getDepartments()) {
                for (Student s : d.getStudents()) {
                    if (s.getGroup().equalsIgnoreCase(group)) {
                        System.out.println("FOUND: " + s);
                        found = true;
                    }
                }
            }
        }
        if (!found) System.out.println("Student not found.");
    }

    public void ByCourse(int course){
        boolean found = false;
        for (Faculty f : Main.university.getFaculties()) {
            for (Department d : f.getDepartments()) {
                for (Student s : d.getStudents()) {
                    if (s.getCourse() == (course)) {
                        System.out.println("FOUND: " + s);
                        found = true;
                    }
                }
            }
        }
        if (!found) System.out.println("Student not found.");

    }

}
