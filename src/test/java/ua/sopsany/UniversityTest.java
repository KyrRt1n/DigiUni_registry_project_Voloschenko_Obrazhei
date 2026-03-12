package ua.sopsany;

import org.junit.jupiter.api.Test;
import ua.sopsany.exceptions.DuplicateIdException;
import ua.sopsany.models.Faculty;
import ua.sopsany.models.Student;
import ua.sopsany.models.University;
import ua.sopsany.utils.GenericRepository;
import ua.sopsany.utils.SearchService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UniversityTest {

    @Test
    void testStudentName() {
        Student student = new Student("Sanya", "Valeriyovych", "Obrazhei",
                LocalDate.of(2007, 10, 11), "email@gmail.com", "+380978620341",
                1, 1, "IPZ", 2025,
                Student.FormEducation.BUDGET, Student.StudentState.STUDYING, "148228");

        String name = student.getName();

        assertEquals("Sanya", name);
    }

    @Test
    void testStudentAge(){
        Student student = new Student("Sanya", "Valeriyovych", "Obrazhei",
                LocalDate.of(2007, 10, 11), "email@gmail.com", "+380978620341",
                1, 1, "IPZ", 2025,
                Student.FormEducation.BUDGET, Student.StudentState.STUDYING, "148228");
        int age = student.getAge();
        assertEquals(18, age);
    }

    @Test
    void testGenericRepository() throws DuplicateIdException {
        GenericRepository<Student> repo = new GenericRepository<>();

        Student student = new Student("Sanya", "Valeriyovych", "Obrazhei",
                LocalDate.of(2007, 10, 11), "email@gmail.com", "+380978620341",
                1, 1, "IPZ", 2025,
                Student.FormEducation.BUDGET, Student.StudentState.STUDYING, "148228");
        repo.add(student);

        assertEquals(1, repo.getAll().size());
    }

    @Test
    void testFindByCourse() throws DuplicateIdException {

        GenericRepository<Student> repo = new GenericRepository<>();
        SearchService searchService = new SearchService();

        Student Sanya = new Student("Sanya", "Valeriyovych", "Obrazhei",
                LocalDate.of(2007, 10, 11), "email@gmail.com", "+380978620341",
                1, 1, "IPZ", 2025,
                Student.FormEducation.BUDGET, Student.StudentState.STUDYING, "148228");
        repo.add(Sanya);
        Student Oleg = new Student("Oleg", "Kyrylovych", "Kyrolov", LocalDate.of(2007, 7, 29),
                "OlegRagul@gmail", "+3809783493", 1232343565, 2, "MARK", 2025, Student.FormEducation.BUDGET , Student.StudentState.STUDYING, "1232343565");
        System.out.println("Oleg: " + Oleg);
        repo.add(Oleg);

        List<Student> result = searchService.findByCourse(repo.getAll(), 1);

        assertEquals(1,  result.size());
        assertEquals(Sanya, result.get(0));
    }


    @Test
    void testFindByLastname() throws DuplicateIdException {
        GenericRepository<Student> repo = new GenericRepository<>();
        SearchService searchService = new SearchService();

        Student Sanya = new Student("Sanya", "Valeriyovych", "Obrazhei",
                LocalDate.of(2007, 10, 11), "email@gmail.com", "+380978620341",
                1, 1, "IPZ", 2025,
                Student.FormEducation.BUDGET, Student.StudentState.STUDYING, "148228");
        repo.add(Sanya);
        Student Oleg = new Student("Oleg", "Kyrylovych", "Kyrolov", LocalDate.of(2007, 7, 29),
                "OlegRagul@gmail", "+3809783493", 1232343565, 2, "MARK", 2025, Student.FormEducation.BUDGET , Student.StudentState.STUDYING, "1232343565");
        repo.add(Oleg);

        List<Student> result = searchService.findByLastName(repo.getAll(), "Obrazhei");

        assertEquals("Sanya", result.get(0).getName());
    }
}