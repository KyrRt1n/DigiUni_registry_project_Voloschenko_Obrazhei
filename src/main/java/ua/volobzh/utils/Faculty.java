package ua.volobzh.utils;

public class Faculty {
    private int id;
    private String fullName;
    private String shortName;
    private String decan; //Must be teacher class instead of String
    private String contacts;

    private Department[] departments;
    private int departmentCount;

    public Faculty(int id, String fullName, String shortName, String decan, String contacts) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
        this.decan = decan;
        this.contacts = contacts;
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

    public String getDecan() {
        return decan;
    }

    public String getContacts() {
        return contacts;
    }
}
