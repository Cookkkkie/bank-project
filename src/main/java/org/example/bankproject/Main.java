package org.example.bankproject;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        User currentUser = null;

        System.out.println("Dear User, welcome to our banking system!");

        while (currentUser == null) {
            System.out.println("Would you like to:\n1) Register\n2) Login");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                currentUser = registerUser(sc);
            } else if (choice == 2) {
                currentUser = loginUser(sc);
            } else {
                System.out.println("Bad input!(choose 1-2)");
            }
        }

        while (true) {
            System.out.println("Dear " + currentUser.getName() + ", what operation would you like to do?\n1) Checking current balance\n2) Deposit\n3) Withdrawal\n4) Make a transaction\n5) Exit");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.printf("%s your current balance is: %s$%n", currentUser.getName(), currentUser.getBankAccount().getBalance());
                    break;
                case 2:
                    System.out.println("How much would you like to deposit?");
                    double deposit = sc.nextDouble();
                    if (deposit < 0) {
                        System.out.println("You cannot deposit a negative amount.");
                    } else {
                        currentUser.getBankAccount().deposit(deposit);
                        DBSetup.updateUserBalance(currentUser.getName(), currentUser.getSecondName(), currentUser.getBankAccount().getBalance());
                    }
                    break;

                case 3:
                    System.out.println("How much would you like to withdraw?");
                    double withdraw = sc.nextDouble();
                    if (withdraw < 0) {
                        System.out.println("You cannot withdraw a negative amount.");
                    } else {
                        currentUser.getBankAccount().withdraw(withdraw);
                        DBSetup.updateUserBalance(currentUser.getName(), currentUser.getSecondName(), currentUser.getBankAccount().getBalance());
                    }
                    break;

                case 4:
                    System.out.println("Please, write receiver full name and how much you want to send in format: Name Surname, AMOUNT");
                    String request = sc.nextLine();

                    sendMoney(request);
                    currentUser.getBankAccount().withdraw(Double.parseDouble(request.split(",")[1]));
                    DBSetup.updateUserBalance(currentUser.getName(),
                            currentUser.getSecondName(),
                            currentUser.getBankAccount().getBalance());
                    break;
                case 5:
                    System.exit(0);
                default:
                    System.out.println("Bad input!(choose 1-5)");
            }
        }
    }

    private static User registerUser(Scanner sc) {
        System.out.println("Enter your name:");
        String name = sc.nextLine();
        System.out.println("Enter your surname:");
        String surname = sc.nextLine();
        System.out.println("Enter your password:");
        String password = sc.nextLine();

        User user = new User(name, surname, password, new BankAccount(0));
        DBSetup.registerUser(user);
        return user;
    }

    private static User loginUser(Scanner sc) {
        System.out.println("Enter your name:");
        String name = sc.nextLine();
        System.out.println("Enter your password:");
        String password = sc.nextLine();

        return DBSetup.readUser(name, password);
    }

    public static void sendMoney(String request) {
        String[] tmp = request.split(",");

        String fullName = tmp[0].trim();
        double amount = Double.parseDouble(tmp[1]);

        String[] nameParts = fullName.split(" ");
        String firstName = nameParts[0];
        String lastName = nameParts[1];

        User receiver = DBSetup.readUser(firstName, lastName);
        if (receiver != null) {
            double newBalance = receiver.getBankAccount().getBalance() + amount;
            DBSetup.updateUserBalance(firstName, lastName, newBalance);
        } else {
            System.out.println("Receiver not found!");
        }
    }
}
