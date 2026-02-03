package ua.sopsany.Models;

    public class Teacher extends Person {

    private String position;
    private  String academicDegree;
    private String academicTitle;
    private String dateOfEmployment;
    private int workLoad;



        public Teacher(String name, String surname, String lastname, String birthday, String email, int phone, int id,
                       String position, String academicDegree, String academicTitle, String dateOfEmployment, int workLoad) {
            super(name, surname, lastname, birthday, email, phone, id);
            this.position = position;
            this.academicDegree = academicDegree;
            this.academicTitle = academicTitle;
            this.dateOfEmployment = dateOfEmployment;
            this.workLoad = workLoad;
        }

        public int getWorkLoad() {
            return workLoad;
        }

        public String getDateOfEmployment() {
            return dateOfEmployment;
        }

        public String getAcademicTitle() {
            return academicTitle;
        }

        public String getAcademicDegree() {
            return academicDegree;
        }

        public String getPosition() {
            return position;
        }
        @Override
        public String toString() {
            return super.toString()+ " Teacher " + position + ", work experience " + dateOfEmployment;
        }
    }
