package org.example.bankproject;

public class User {
    private String name;
    private String secondName;
    private String password;
    private BankAccount bankAccount;

    public User() {
        this.name = "";
        this.secondName = "";
        this.password = "";
        this.bankAccount = new BankAccount();
    }

    public User(String name, String secondName, String password, BankAccount bankAccount) {
        this.name = name;
        this.secondName = secondName;
        this.password = password;
        this.bankAccount = bankAccount;
    }

    public User(String name, String secondName, BankAccount bankAccount) {
        this.name = name;
        this.secondName = secondName;
        this.bankAccount = bankAccount;
    }

    public String getName() {return name;}

    public String getSecondName() {return secondName;}

    public String getPassword() {return password;}

    public BankAccount getBankAccount() {return bankAccount;}

    public void setName(String name) {this.name = name;}

    public void setSecondName(String secondName) {this.secondName = secondName;}

    public void setPassword(String password) {this.password = password;}

    public void setBankAccount(BankAccount bankAccount) {this.bankAccount = bankAccount;}

}