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
                Statement statement = connection.createStatement()
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


    public static User readUser(String name) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
                Statement statement = connection.createStatement()
        ) {
            String querySQL = String.format("SELECT * FROM users WHERE name = '%s'",name );
            ResultSet resultSet = statement.executeQuery(querySQL);
            if (resultSet.next()) {
                String secondName = resultSet.getString("second_name");
                double balance = resultSet.getDouble("balance");
                return new User(name, secondName, new BankAccount(balance));
            }

        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        System.out.println("User not found or password wrong");
        return null;
    }

    public static User readUserLogin(String name, String password) {

        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
                Statement statement = connection.createStatement()
        ) {
            String querySQL = String.format("SELECT * FROM users WHERE name = '%s' and password = '%s';",name, password);
            ResultSet resultSet = statement.executeQuery(querySQL);
            if(resultSet.next()) {
                String secondName = resultSet.getString("second_name");
                double balance = resultSet.getDouble("balance");
                return new User(name,secondName, new BankAccount(balance));
            }

        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        System.out.println("User not found or password wrong");
        return null;
    }

    public static void sendMoney(String firstName, String lastName, double amount, String sender){
        User senderUser = readUser(sender);

        String selectSenderQuery = "SELECT * FROM users WHERE name = ? AND second_name = ?";
        String selectReceiverQuery = "SELECT * FROM users WHERE name = ? AND second_name = ?";
        String updateUserBalanceQuery = "UPDATE users SET balance = ? WHERE name = ? AND second_name = ?";

        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
                PreparedStatement selectSenderStmt = connection.prepareStatement(selectSenderQuery);
                PreparedStatement selectReceiverStmt = connection.prepareStatement(selectReceiverQuery);
                PreparedStatement updateUserBalanceStmt = connection.prepareStatement(updateUserBalanceQuery)
        ) {

            assert senderUser != null;
            selectSenderStmt.setString(1, senderUser.getName());
            selectSenderStmt.setString(2, senderUser.getSecondName());


            try (ResultSet resultSetSender = selectSenderStmt.executeQuery()) {
                if (!resultSetSender.next()) {
                    return;
                }

                selectReceiverStmt.setString(1, firstName);
                selectReceiverStmt.setString(2, lastName);


                try (ResultSet resultSetReceiver = selectReceiverStmt.executeQuery()) {
                    if (!resultSetReceiver.next()) {
                        return;
                    }

                    double currentBalanceSender = resultSetSender.getDouble("balance");
                    if(amount>currentBalanceSender){logger.info("Insufficient funds!");}else {
                        double newBalanceSender = currentBalanceSender - amount;

                        double currentBalanceReceiver = resultSetReceiver.getDouble("balance");
                        double newBalanceReceiver = currentBalanceReceiver + amount;


                        updateUserBalanceStmt.setDouble(1, newBalanceReceiver);
                        updateUserBalanceStmt.setString(2, firstName);
                        updateUserBalanceStmt.setString(3, lastName);
                        updateUserBalanceStmt.executeUpdate();


                        updateUserBalanceStmt.setDouble(1, newBalanceSender);
                        updateUserBalanceStmt.setString(2, senderUser.getName());
                        updateUserBalanceStmt.setString(3, senderUser.getSecondName());
                        updateUserBalanceStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error during money transfer", e);
        }
    }


    public static void updateBalance(User user) {
        String name = user.getName();
        String secondName = user.getSecondName();
        double balance = user.getBankAccount().getBalance();

        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:bank.db");
                Statement statement = connection.createStatement()
        ) {
            String updateSQL = String.format("UPDATE users SET balance = '%f' WHERE name = '%s' and second_name = '%s';",balance,name,secondName);
            statement.executeUpdate(updateSQL);
        }
        catch (SQLException e) {
            logger.error("Error: ", e);
        }
    }
}
