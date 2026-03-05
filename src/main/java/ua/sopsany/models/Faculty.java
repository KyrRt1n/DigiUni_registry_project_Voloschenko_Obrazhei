    package ua.sopsany.models;

    import java.util.ArrayList;
    import java.util.List;

    public class Faculty {
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
