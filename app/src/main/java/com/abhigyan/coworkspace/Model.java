package com.abhigyan.coworkspace;

public class Model {

//    public static status;
    public String user;
    public String type;
    public String value;
    public String status;

    public Model(String user, String type, String value, String status) {
        this.user = user;
        this.type = type;
        this.value = value;
        this.status = status;
    }
    Model(){

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
