package org.example.bankproject;

import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class DATABASE {


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
                    user.getName(),
                    user.getSecondName(),
                    user.getPassword(),
                    user.getBankAccount().getBalance());

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
            String querySQL = String.format("SELECT * FROM users WHERE name = '%s' and password = '%s'",name,password );
            ResultSet resultSet = statement.executeQuery(querySQL);
            while (resultSet.next()) {
                String secondName = resultSet.getString("second_name");
                double balance = resultSet.getDouble("balance");
                return new User(name,secondName,password,new BankAccount(balance));
            }


        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        System.out.println("User not found or password wrong");
        return null;
    }

    public static void sendMoney(String firstName, String lastName, double amount) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
                Statement statement = connection.createStatement();
        ) {
            String querySQL = String.format("SELECT balance FROM users WHERE name = '%s' and second_name = '%s'", firstName, lastName);
            ResultSet resultSet = statement.executeQuery(querySQL);

            if (resultSet.next()) {
                double currentBalance = resultSet.getDouble("balance");
                double newBalance = currentBalance + amount;

                String updateSQL = String.format("UPDATE users SET balance = '%f' WHERE name = '%s' and second_name = '%s'", newBalance, firstName, lastName);
                statement.executeUpdate(updateSQL);
            } else {
                System.out.println("User not found");
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }


    public static void updateBalance(User user) {
        String name = user.getName();
        String secondName = user.getSecondName();
        double balance = user.getBankAccount().getBalance();

        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
                Statement statement = connection.createStatement();
        ) {
            String updateSQL = String.format("UPDATE users SET balance = '%f' WHERE name = '%s' and second_name = '%s';",balance,name,secondName);
            statement.executeUpdate(updateSQL);
        }
        catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
}
