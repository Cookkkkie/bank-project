package org.example.bankproject;

import javax.xml.crypto.Data;

public class BankAccount {
    private double balance;

    public BankAccount() {
        this.balance = 0;
    }

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        if(amount < 0){
            System.out.println("ERROR! Put only positive values");
            return;  // Exit the method if the amount is negative
        }
        this.balance += amount;  // Correctly update the balance
        System.out.println("You successfully deposited " + amount + "$ to the bank.");
    }


    public void withdraw(double amount) {
        amount = Math.abs(amount);  // Ensure the withdrawal amount is positive

        if(amount > this.balance) {
            System.out.println("Insufficient funds!");
        } else {
            this.balance -= amount;  // Correctly update the balance
            System.out.println("You successfully withdrew " + amount + " from the bank.");
        }
    }


    public double getBalance() {
        this.balance = 0;
        return this.balance;
    }
}