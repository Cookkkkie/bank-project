package org.example.bankproject;

import java.util.concurrent.ThreadLocalRandom;

public class BankAccount {

    private double balance;
    private final String bankAccountNumber;

    public BankAccount(double balance) {
        this.balance = balance;
        this.bankAccountNumber = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999 + 1));
    }

    public void deposit(double amount) {
        if (amount < 0) {
            System.out.println("Amount must be positive");
        } else {
            this.balance += amount;
            System.out.println("You successfully deposited " + amount + "$ to the bank.");
        }
    }

    public void withdraw(double amount) {
        amount = Math.abs(amount);
        if (amount > this.balance) {
            System.out.println("Insufficient funds!");
        } else {
            this.balance -= amount;
            System.out.println("You successfully withdrew " + amount + "$ from the bank.");
        }
    }

    public double getBalance() { return this.balance; }
    public String getAccountNumber() { return bankAccountNumber; }

}
