package com.example.sahandilshan.geekcartest;

import java.io.Serializable;

public class Users implements Serializable{

    private String username;
    private char [] password;
    private String firstName;
    private String lastName;
    private int wins;
    private int loss;
    private String key;



    public Users(String username, char[] password,String firstName,String lastName) {
        this.username = username;
        this.password = password;
        this.firstName=firstName;
        this.lastName=lastName;
    }

    public Users(String username, char[] password, String firstName, String lastName, int wins, int loss) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.wins = wins;
        this.loss = loss;
    }

    public Users(String username, char[] password, String firstName, String lastName, int wins, int loss, String key) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.wins = wins;
        this.loss = loss;
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getWins() {
        return wins;
    }

    public int getLoss() {
        return loss;
    }

    public String getKey() {
        return key;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }
}

