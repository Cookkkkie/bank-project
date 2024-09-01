package org.example.bankproject;


public class BankAccount {
    private double balance;

    public BankAccount() {
        this.balance = 0;
    }

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public double getBalance() {return this.balance;}

    public void deposit(double amount) {

        if (amount < 0) {
            System.out.println("Amount must be positive");
        }
        this.balance += amount;
        System.out.println("You successfully deposited " + amount + "$ to the bank.");
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

}