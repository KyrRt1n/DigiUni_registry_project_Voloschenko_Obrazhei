package ua.sopsany;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import ua.sopsany.auth.AuthService;
import ua.sopsany.auth.Role;
import ua.sopsany.auth.User;
import ua.sopsany.exceptions.DuplicateIdException;
import ua.sopsany.exceptions.EntityNotFoundException;
import ua.sopsany.exceptions.UnauthorizedException;
import ua.sopsany.models.*;
import ua.sopsany.utils.GenericRepository;
import ua.sopsany.utils.SearchService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UniversityTest {

    private AuthService authService;
    private GenericRepository<Student> studentRepo;
    private SearchService searchService;
    private Student testStudent1;
    private Student testStudent2;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
        studentRepo = new GenericRepository<>();
        searchService = new SearchService();

        authService.addUser(new User("admin", "admin123", Role.ADMIN));
        authService.addUser(new User("user", "user123".hashCode(), Role.USER, true));  // blocked user

        testStudent1 = new Student("Ivan", "Ivanovych", "Ivanov", LocalDate.of(2005, 1, 1),
                "ivan@gmail", "123", 1, 1, "IPZ", 2023, Student.FormEducation.BUDGET, Student.StudentState.STUDYING, "T101");
        testStudent2 = new Student("Petro", "Petrovych", "Petrov", LocalDate.of(2004, 2, 2),
                "petro@gmail", "321", 2, 2, "MARK", 2022, Student.FormEducation.CONTRACT, Student.StudentState.STUDYING, "T102");
    }

    // auth tests
    // auth tests
    // auth tests
    // auth tests
    // auth tests
    @Test
    void testSuccessfulLogin() throws UnauthorizedException {
        Optional<User> user = authService.login("admin", "admin123");
        assertTrue(user.isPresent());
        assertEquals(Role.ADMIN, user.get().getRole());
    }

    @Test
    void testLoginInvalidPassword() {
        assertThrows(UnauthorizedException.class, () -> authService.login("admin", "wrong_pass"));
    }

    @Test
    void testLoginNonExistentUser() {
        assertThrows(UnauthorizedException.class, () -> authService.login("ghost", "123"));
    }

    @Test
    void testLoginBlockedUser() {
        assertThrows(UnauthorizedException.class, () -> authService.login("user", "user123"));
    }

    @Test
    void testRemoveUser() {
        assertTrue(authService.removeUser("admin"));
        assertNull(authService.findByLogin("admin"));
    }

    @Test
    void testChangePassword() throws UnauthorizedException {
        User u = authService.findByLogin("admin");
        authService.changePassword(u, "new123");
        assertTrue(authService.login("admin", "new123").isPresent());
    }

    // generic repository tests
    // generic repository tests
    // generic repository tests
    // generic repository tests
    // generic repository tests
    // generic repository tests
    @Test
    void testAddToRepo() throws DuplicateIdException {
        studentRepo.add(testStudent1);
        assertEquals(1, studentRepo.getAll().size());
    }

    @Test
    void testAddDuplicateToRepo() throws DuplicateIdException {
        studentRepo.add(testStudent1);
        assertThrows(DuplicateIdException.class, () -> studentRepo.add(testStudent1));
    }

    @Test
    void testRemoveFromRepo() throws DuplicateIdException, EntityNotFoundException {
        studentRepo.add(testStudent1);
        studentRepo.remove(testStudent1);
        assertTrue(studentRepo.getAll().isEmpty());
    }

    @Test
    void testRemoveNonExistentFromRepo() {
        assertThrows(EntityNotFoundException.class, () -> studentRepo.remove(testStudent2));
    }

    @Test
    void testGetAllRepo() throws DuplicateIdException {
        studentRepo.add(testStudent1);
        studentRepo.add(testStudent2);
        assertEquals(2, studentRepo.getAll().size());
    }

    // search tests
    // search tests
    // search tests
    // search tests
    // search tests
    // search tests
    @Test
    void testFindByLastName() {
        List<Student> list = List.of(testStudent1, testStudent2);
        List<Student> res = searchService.findByLastName(list, "Ivanov");
        assertEquals(1, res.size());
        assertEquals("Ivanov", res.get(0).getLastname());
    }

    @Test
    void testGetAllUniqueGroupsUsingSet() {
        Student s3 = new Student("Anna", "A", "A", LocalDate.of(2005,1,1), "e", "p", 3, 1, "IPZ", 2023, Student.FormEducation.BUDGET, Student.StudentState.STUDYING, "T103");
        List<Student> list = List.of(testStudent1, testStudent2, s3);
        Set<String> uniqueGroups = searchService.getAllUniqueGroups(list);
        assertEquals(2, uniqueGroups.size()); // IPZ і MARK
        assertTrue(uniqueGroups.contains("IPZ"));
    }

    @Test
    void testSortStudentsByCourse() {
        List<Student> list = List.of(testStudent2, testStudent1); // course 2, then 1
        List<Student> sorted = searchService.sortStudentsByCourse(list);
        assertEquals(1, sorted.get(0).getCourse());
        assertEquals(2, sorted.get(1).getCourse());
    }

    @Test
    void testFindByStudentId() {
        List<Student> list = List.of(testStudent1, testStudent2);
        Student found = searchService.findByStudentId(list, "T102");
        assertNotNull(found);
        assertEquals("Petrov", found.getLastname());
    }

    @Test
    void testFindByStudentIdNotFound() {
        List<Student> list = List.of(testStudent1);
        assertNull(searchService.findByStudentId(list, "999"));
    }

    // parametrized tests
    // parametrized tests
    // parametrized tests
    // parametrized tests
    // parametrized tests
    @ParameterizedTest
    @CsvSource({
            "Ivanov, 1",
            "Petrov, 1",
            "Sidorov, 0"
    })
    void testFindByLastNameParameterized(String lastName, int expectedCount) {
        List<Student> list = List.of(testStudent1, testStudent2);
        assertEquals(expectedCount, searchService.findByLastName(list, lastName).size());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void testFindByCourseParameterized(int course) {
        List<Student> list = List.of(testStudent1, testStudent2);
        List<Student> res = searchService.findByCourse(list, course);
        assertFalse(res.isEmpty());
        assertEquals(course, res.get(0).getCourse());
    }

    // model tests
    // model tests
    // model tests
    // model tests
    // model tests
    @Test
    void testAgeCalculation() {
        assertEquals(LocalDate.now().getYear() - 2005, testStudent1.getAge());
    }

    @Test
    void testTeacherYearsOfWork() {
        Teacher t = new Teacher("T", "T", "T", LocalDate.of(1980,1,1), "e", "p", 1, "Pos", "Deg", "Title", LocalDate.now().minusYears(5), 20);
        assertEquals(5, t.getYearsOfWork());
    }

    @Test
    void testFacultyEqualsAndHashCode() {
        Faculty f1 = new Faculty(1, "Fac1", "F1");
        Faculty f2 = new Faculty(1, "Fac2", "F2");
        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());
    }
    //dept + course tests
    @Test
    void testGetDeptStudentsByCoursePlain() {
        Department dept = new Department(11, "DeptX");
        dept.addStudent(testStudent1);
        dept.addStudent(testStudent2);

        List<Student> onlyCourse1 = searchService.getDeptStudentsByCourse(dept, 1);
        assertEquals(1, onlyCourse1.size());
        assertEquals("Ivanov", onlyCourse1.get(0).getLastname());

        List<Student> onlyCourse5 = searchService.getDeptStudentsByCourse(dept, 5);
        assertTrue(onlyCourse5.isEmpty());
    }

    @Test
    void testGetDeptStudentsByCourseAlpha() {
        Department dept = new Department(12, "DeptY");
        Student sameCourseO = new Student("Olena", "O", "Ooo", LocalDate.of(2005, 1, 1),
                "e", "p", 9, 1, "IPZ", 2023,
                Student.FormEducation.BUDGET, Student.StudentState.STUDYING, "T999");
        dept.addStudent(sameCourseO);
        dept.addStudent(testStudent1);
        dept.addStudent(testStudent2);

        List<Student> alpha = searchService.getDeptStudentsByCourseAlpha(dept, 1);
        assertEquals(2, alpha.size());
        assertEquals("Ivanov", alpha.get(0).getLastname());
        assertEquals("Ooo",    alpha.get(1).getLastname());
    }

    //Optional in GenericRepository + LocalDateTime/Duration for session

    @Test
    void testGenericRepoFindByReturnsOptional() throws DuplicateIdException {
        studentRepo.add(testStudent1);
        studentRepo.add(testStudent2);

        Optional<Student> hit  = studentRepo.findBy(s -> "T102".equals(s.getStudentID()));
        Optional<Student> miss = studentRepo.findBy(s -> "XXX".equals(s.getStudentID()));

        assertTrue(hit.isPresent());
        assertEquals("Petrov", hit.get().getLastname());
        assertTrue(miss.isEmpty());

        assertEquals(2, studentRepo.size());
        assertFalse(studentRepo.isEmpty());
    }

    @Test
    void testLoginSetsLastLoginTimestamp() throws UnauthorizedException {
        java.time.LocalDateTime before = java.time.LocalDateTime.now();
        Optional<User> u = authService.login("admin", "admin123");

        assertTrue(u.isPresent());
        java.time.LocalDateTime lastLogin = u.get().getLastLogin();
        assertNotNull(lastLogin, "lastLogin should be set on successful login");
        assertFalse(lastLogin.isBefore(before), "lastLogin must be at or after the call time");
    }

    @Test
    void testSessionDurationViaDuration() throws UnauthorizedException, InterruptedException {
        Optional<User> u = authService.login("admin", "admin123");
        assertTrue(u.isPresent());
        Thread.sleep(50);

        java.time.Duration session = java.time.Duration.between(
                u.get().getLastLogin(), java.time.LocalDateTime.now());

        assertTrue(session.toMillis() >= 50,
                "Session duration must be at least the sleep period, got " + session.toMillis() + " ms");
    }
}