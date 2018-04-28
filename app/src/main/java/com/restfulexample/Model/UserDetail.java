package com.restfulexample.Model;


/**
 * @author siddhesh gawde trainer at suven consultant
 * @since 2016
 */
public class UserDetail {
    int id;
    String name;
    String profession;

    public UserDetail() {
        this.id = 0;
        this.name = "";
        this.profession = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
