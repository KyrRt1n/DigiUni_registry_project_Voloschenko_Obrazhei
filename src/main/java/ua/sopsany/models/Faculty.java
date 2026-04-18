    package ua.sopsany.models;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Objects;

    public class Faculty implements Identifiable<Integer>{
        private int id;
        private String fullName;
        private String shortName;
        private Teacher decan;
        private String contacts;


        private List<Department> departments =  new ArrayList<>();

        public Faculty(int id, String fullName, String shortName, Teacher decan, String contacts) {
            this.id = id;
            this.fullName = fullName;
            this.shortName = shortName;
            this.decan = decan;
            this.contacts = contacts;//можливо тут видалив зайве, після тесту треба перевірити
        }

        public Faculty(int id, String fullName, String shortName) {
            this.id = id;
            this.fullName = fullName;
            this.shortName = shortName;

        }

        public Faculty() {}

        public void addDepartment(Department department) {
            departments.add(department);
        }


        public void setDepartments(List<Department> departments) {
            this.departments = departments;
        }
        public void setContacts(String contacts) {
            this.contacts = contacts;
        }

        public void setDecan(Teacher decan) {
            this.decan = decan;
        }

        public List<Department> getDepartments() {
            return departments;
        }

        @Override
        public Integer getId() {
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
            if (contacts == null)
                contacts = "not assigned";
            return shortName + " - " + fullName + " (Contacts: " + contacts + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Faculty faculty = (Faculty) o;
            return id == faculty.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
