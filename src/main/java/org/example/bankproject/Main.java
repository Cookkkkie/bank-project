package org.example.bankproject;

public class Main {


    public static void main(String[] args) {
        User user = new User("asd","qwe","asdlalsd@asd.asd", "123", new BankAccount(1234));
        System.out.println(user.getBankAccount().getAccountNumber());
    }

}