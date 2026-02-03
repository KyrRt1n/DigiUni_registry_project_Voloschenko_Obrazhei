package ua.sopsany.models;

public abstract class Person {

    private String name;
    private String surname;
    private String lastname;
    private String birthday;
    private String email;
    private int phone;
    private int id;



        public Person(String name, String surname, String lastname, String birthday, String email, int phone, int id) {
            this.name = name;
            this.surname = surname;
            this.lastname = lastname;
            this.birthday = birthday;
            this.email = email;
            this.phone = phone;
            this.id = id;
        }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }

    public int getPhone() {
        return phone;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return id + ": " + surname + " " + name + " (" + phone + ")";
    }

}
