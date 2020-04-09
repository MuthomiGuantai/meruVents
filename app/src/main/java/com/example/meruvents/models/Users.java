package com.example.meruvents.models;

public class Users {
    private String username, phone, password,email;

    public Users()
    {

    }

    public Users(String name, String phone, String password, String email) {
        this.username = name;
        this.phone = phone;
        this.password = password;
        this.email = email;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
