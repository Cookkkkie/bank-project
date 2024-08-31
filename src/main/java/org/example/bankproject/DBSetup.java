package org.example.bankproject;

import java.sql.*;

public class DBSetup {


    public static void main(String[] args) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
                Statement statement = connection.createStatement();
        ) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "second_name TEXT NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "balance REAL NOT NULL)";
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public static void registerUser(User user) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
                Statement statement = connection.createStatement();
        ) {
            String insertSQL = String.format("INSERT INTO users (name, second_name, password, balance) " +
                            "VALUES ('%s', '%s', '%s', %f)",
                    user.getName(), user.getSecondName(), user.getPassword(), user.getBankAccount().getBalance());
            statement.executeUpdate(insertSQL);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }


    public static User readUser(String name, String password) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
                Statement statement = connection.createStatement();
        ) {
            String querySQL = String.format("SELECT * FROM users WHERE name = '%s' AND password = '%s'", name, password);
            ResultSet resultSet = statement.executeQuery(querySQL);
            if (resultSet.next()) {
                String secondName = resultSet.getString("second_name");
                double balance = resultSet.getDouble("balance");
                return new User(name, secondName, password, new BankAccount(balance));
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        System.out.println("User not found or wrong password!");
        return null;
    }


    public static void updateUserBalance(String name, String secondName, double newBalance) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
                Statement statement = connection.createStatement();
        ) {
            String updateSQL = String.format("UPDATE users SET balance = %f WHERE name = '%s' AND second_name = '%s'",
                    newBalance, name, secondName);
            statement.executeUpdate(updateSQL);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }
}
