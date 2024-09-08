package org.example.bankproject;

public class User {
    private final String name;
    private final String surname;
    private final String email;
    private final String password;
    private final BankAccount bankAccount;


    public User(String name, String surname, String email, String password, BankAccount bankAccount) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.bankAccount = bankAccount;
    }


    public String getName() {return name;}

    public String getSecondName() {return surname;    }

    public String getEmail() {return email;}

    public String getPassword() {return password;}

    public BankAccount getBankAccount() {return bankAccount;}

}