package org.example.bankproject;

public class RegisterUserThread implements Runnable {
    private User user;

    public RegisterUserThread(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        DATABASE.registerUser(user);
    }
}