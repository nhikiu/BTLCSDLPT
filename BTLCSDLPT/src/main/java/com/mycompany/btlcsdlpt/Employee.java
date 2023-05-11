package com.mycompany.btlcsdlpt;

public class Employee {
    private String id;
    private String fullname;
    private String role;
    private String phonenumber;
    private String id_branch;

    public Employee() {
    }

    public Employee(String id, String fullname, String role, String phonenumber, String id_branch) {
        this.id = id;
        this.fullname = fullname;
        this.role = role;
        this.phonenumber = phonenumber;
        this.id_branch = id_branch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getId_branch() {
        return id_branch;
    }

    public void setId_branch(String id_branch) {
        this.id_branch = id_branch;
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", fullname=" + fullname + ", role=" + role + ", phonenumber=" + phonenumber + ", id_branch=" + id_branch + '}';
    }
}
