package org.example.bankproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testUserCreation() {
        BankAccount account = new BankAccount(100.0);
        User user = new User("John", "Doe","gm@gm.c", "pwd123", account);
        Assertions.assertAll(
                ()->Assertions.assertEquals("John", user.getName()),
                ()->Assertions.assertEquals("Doe", user.getSecondName()),
                ()->Assertions.assertEquals("gm@gm.c", user.getEmail()),
                ()->Assertions.assertEquals("pwd123", user.getPassword()),
                ()->Assertions.assertEquals(100.0, user.getBankAccount().getBalance())
    );
    }



    @Test
    @DisplayName("Failure test")
    public void testFailure() {
        User user = new User("John", "Doe", "email@gmail.com","pwd", new BankAccount(300.0));
        Assertions.assertAll(
                ()->Assertions.assertEquals("John", user.getName()),
                ()->Assertions.assertEquals("Doe", user.getSecondName()),
                ()->Assertions.assertEquals("email@gmail.com", user.getEmail()),
                ()->Assertions.assertNotEquals("Pa$$word123", user.getPassword()),
                ()->Assertions.assertEquals(300.0, user.getBankAccount().getBalance())

        );
    }
}
