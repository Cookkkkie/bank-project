package org.example.bankproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        User currentUser = null;
        logger.info("Application started");
        System.out.println("Dear User, welcome to our banking system!");

        while (currentUser == null) {
            System.out.println("Would you like to:\n1) Register\n2) Login");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                currentUser = registerUser(sc);
                logger.info("User created: " + currentUser.getName() + " " + currentUser.getSecondName());
            } else if (choice == 2) {
                currentUser = loginUser(sc);
                logger.info("User logged in: " + currentUser.getName() + " " + currentUser.getSecondName());
            } else {
                System.out.println("Bad input!(choose 1-2)");
            }
        }

        while (true) {
            System.out.printf("Dear %s, what operation would you like to do?\n1) Checking current balance\n2) Deposit\n3) Withdrawal\n4) Make a transaction\n5) Exit%n", currentUser.getName());
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.printf("%s your current balance is: %s$%n", currentUser.getName(), currentUser.getBankAccount().getBalance());
                    logger.info("User " + currentUser.getName() + " checked balance");
                    break;
                case 2:
                    System.out.println("How much would you like to deposit?");
                    double deposit = sc.nextDouble();
                    currentUser.getBankAccount().deposit(deposit);
                    DATABASE.updateBalance(currentUser);
                    logger.info("User:"+currentUser.getName()+"deposited:"+deposit+"$");
                    break;

                case 3:
                    System.out.println("How much would you like to withdraw?");
                    double withdraw = sc.nextDouble();
                    currentUser.getBankAccount().withdraw(withdraw);
                    DATABASE.updateBalance(currentUser);
                    logger.info("User:"+currentUser.getName()+"withdrawed:"+withdraw+"$");
                    break;

                case 4:
                    System.out.println("Please, write receiver full name and how much you want to send in format: Name Surname, AMOUNT");
                    String request = sc.nextLine();

                    String[] tmp = request.split(",");

                    String fullName = tmp[0].trim();
                    double amount = Double.parseDouble(tmp[1]);

                    String[] nameParts = fullName.split(" ");
                    String firstName = nameParts[0];
                    String lastName = nameParts[1];


                    DATABASE.sendMoney(firstName, lastName, amount);
                    currentUser.getBankAccount().withdraw(amount);
                    DATABASE.updateBalance(currentUser);
                    logger.info("User"+currentUser+"sent money to"+tmp);

                    break;
                case 5:
                    logger.info("Application closed");
                    System.exit(0);
                    break;
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
        DATABASE.registerUser(user);
        return user;
    }

    private static User loginUser(Scanner sc) {
        System.out.println("Enter your name:");
        String name = sc.nextLine();
        System.out.println("Enter your password:");
        String password = sc.nextLine();
        return DATABASE.readUser(name, password);
    }


}
