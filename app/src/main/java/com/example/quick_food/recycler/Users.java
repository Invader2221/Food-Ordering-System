package com.example.quick_food.recycler;

public class Users {

    private String Name, admin, password, mobile, stuId, email;

    public Users() {
    }

    public Users(String name, String password, String admin, String mobile, String stuId, String email) {
        this.Name = name;
        this.password = password;
        this.admin = admin;
        this.mobile = mobile;
        this.stuId = stuId;
        this.email = email;
    }

    public String getStuid() {
        return stuId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) { Name = name; }

    public String getPassword() {
        return password;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAdmin() {
        return admin;
    }
}
