package ua.sopsany;

import ua.sopsany.models.Department;
import ua.sopsany.models.Faculty;
import ua.sopsany.utils.InputHandler;
import ua.sopsany.models.University;

public class Main {
    public static void main(String[] args) {
        University university = new University("НаУКМА", "НаУКМА", "Киев", "Сковороды 2");
        Faculty fi = new Faculty(1, "Faculty of Infomatics", "FI", "Hlybovets","044-111-22-33");
        university.addFaculty(fi);

        Department se = new Department(1, "Software Engineering", "1-225");
        fi.addDepartment(se);

        InputHandler input = new InputHandler();

        while (true) {
            System.out.println("Right now there is " + university.getFacultyCount() + " faculties");

            int choice = input.readInt("Select", 1, 3);
        }
    }
}