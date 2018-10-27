package com.pucpr.puctcc.model;

import com.google.firebase.database.Exclude;

public class User {
    private String name;
    private String email;
    private String password;
    private String typeUser;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    @Exclude
    public void setPassword(String password) {
        this.password = password;
    }

    @Exclude
    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }
}
