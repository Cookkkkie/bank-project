//package org.example.bankproject;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//public class UserTest {
//
//    @Test
//    public void testUserCreation() {
//        BankAccount account = new BankAccount(100.0);
//        User user = new User("John", "Doe", "pwd123", account);
//        Assertions.assertAll(
//                ()->Assertions.assertEquals("John", user.getName()),
//                ()->Assertions.assertEquals("Doe", user.getSecondName()),
//                ()->Assertions.assertEquals("pwd123", user.getPassword()),
//                ()->Assertions.assertEquals(100.0, user.getBankAccount().getBalance())
//    );
//    }
//
//    @Test
//    public void testSetName() {
//        User user = new User();
//        user.setName("Alice");
//        Assertions.assertEquals("Alice", user.getName());
//    }
//
//    @Test
//    public void testSetSecondName() {
//        User user = new User();
//        user.setSecondName("Smith");
//        Assertions.assertEquals("Smith", user.getSecondName());
//    }
//
//    @Test
//    public void testSetPassword() {
//        User user = new User();
//        user.setPassword("newpass");
//        Assertions.assertEquals("newpass", user.getPassword());
//    }
//
//    @Test
//    public void testSetBankAccount() {
//        BankAccount account = new BankAccount(300.0);
//        User user = new User();
//        user.setBankAccount(account);
//        Assertions.assertEquals(300.0, user.getBankAccount().getBalance());
//    }
//
//    @Test
//    @DisplayName("Failure test")
//    public void testFailure() {
//        User user = new User("John", "Doe", "pwd", new BankAccount(300.0));
//        Assertions.assertAll(
//                ()->Assertions.assertEquals("John", user.getName()),
//                ()->Assertions.assertEquals("Doe", user.getSecondName()),
//                ()->Assertions.assertNotEquals("Pa$$word123", user.getPassword()),
//                ()->Assertions.assertEquals(300.0, user.getBankAccount().getBalance())
//
//        );
//    }
//}
