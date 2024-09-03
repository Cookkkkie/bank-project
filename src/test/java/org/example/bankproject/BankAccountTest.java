package org.example.bankproject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class BankAccountTest {

    @Test
    void testDeposit() {
        BankAccount account = new BankAccount(100.0);
        account.deposit(-50.0);
        assertEquals(100, account.getBalance());
        assertNotEquals(50.0, account.getBalance());
    }

    @Test

    void testWithdraw() {
        BankAccount account = new BankAccount(100.0);
        User user = new User("q", "q", "q",account);
        account.withdraw(-50.0);
        DATABASE.updateBalance(user);
//
//        assertEquals(50.0, DATABASE.readUser("q","q").getBankAccount().getBalance());
//        assertNotEquals(150.0, DATABASE.readUser("q","q").getBankAccount().getBalance());
    }

    @Test
    void testWithdrawInsufficientFunds() {
        BankAccount account = new BankAccount(50.0);
        account.withdraw(100.0);
        assertEquals(50.0, account.getBalance());
    }

    @Test
    void testGetBalance() {
        BankAccount account = new BankAccount(200.0);
        assertEquals(200.0, account.getBalance());
    }
}
