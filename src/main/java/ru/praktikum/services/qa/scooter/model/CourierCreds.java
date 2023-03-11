package ru.praktikum.services.qa.scooter.model;

public class CourierCreds {
    private String login;
    private String password;

    @Override
    public String toString() {
        return "CourierCreds{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public CourierCreds(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierCreds from(Courier courier) {
        return new CourierCreds(courier.getLogin(), courier.getPassword());
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
