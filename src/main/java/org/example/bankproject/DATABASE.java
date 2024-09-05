package org.example.bankproject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class DATABASE {

    private static final Logger logger = LogManager.getLogger(DATABASE.class);

    public static void main(String[] args) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
                Statement statement = connection.createStatement()
        ) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "surname TEXT NOT NULL, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "balance REAL NOT NULL, " +
                    "bank_account_number TEXT UNIQUE NOT NULL)";
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public static void registerUser(User user) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO users (name, surname, email, password, balance, bank_account_number) " +
                                "VALUES (?, ?, ?, ?, ?, ?)"
                )
        ) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSecondName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setDouble(5, user.getBankAccount().getBalance());
            statement.setString(6, user.getBankAccount().getAccountNumber());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public static User readUserLogin(String email, String password) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM users WHERE email = ? AND password = ?"
                )
        ) {
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                double balance = resultSet.getDouble("balance");
                return new User(name, surname, email, password, new BankAccount(balance));
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        System.out.println("User not found or password incorrect");
        return null;
    }

    public static void sendMoney(String bankAccountNumber, double amount, String senderEmail) {

        User sender = readUserByEmail(senderEmail);

        assert sender != null;
        if (sender.getBankAccount().getBalance() < amount) {
            logger.error("Insufficient balance");
        } else {


            String updateReceiverUpd = "UPDATE users SET balance = balance + ? WHERE bank_account_number = ?";
            String updateSenderUpd = "UPDATE users SET balance = balance - ? WHERE email = ?";


            try (
                    Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");

                    PreparedStatement updateReceiverStmt = connection.prepareStatement(updateReceiverUpd);
                    PreparedStatement updateSenderStmt = connection.prepareStatement(updateSenderUpd);

            ) {


                updateReceiverStmt.setDouble(1, amount);
                updateReceiverStmt.setString(2, bankAccountNumber);

                updateSenderStmt.setDouble(1, amount);
                updateSenderStmt.setString(2, senderEmail);

                updateSenderStmt.executeUpdate();
                updateReceiverStmt.executeUpdate();


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static User readUserByEmail(String email) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM users WHERE email = ?"
                )
        ) {
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                double balance = resultSet.getDouble("balance");
                return new User(name, surname, email, "", new BankAccount(balance));
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    public static void updateBalance(User user) {
        String email = user.getEmail();
        double balance = user.getBankAccount().getBalance();

        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
                Statement statement = connection.createStatement()
        ) {
            String updateSQL = String.format("UPDATE users SET balance = '%f' WHERE email = '%s';", balance, email);
            statement.executeUpdate(updateSQL);
        } catch (SQLException e) {
            logger.error("Error: ", e);
        }
    }
}
