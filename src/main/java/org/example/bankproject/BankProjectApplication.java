package org.example.bankproject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;


@SpringBootApplication
@Controller
public class BankProjectApplication {
    private static final Logger logger = LogManager.getLogger(BankProjectApplication.class);

    public static void main(String[] args) {

        logger.info("Application started");
        SpringApplication.run(BankProjectApplication.class, args);
    }

    @GetMapping("/")
    public String homePage() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String name, @RequestParam String password,  Model model) {
        User user = DATABASE.readUserLogin(name, password);
        if (user != null) {
            model.addAttribute("user", user);
            return "redirect:/main?name=" + name;
        } else {
            model.addAttribute("error", "Invalid username or password.");
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String name, @RequestParam String surname, @RequestParam String password, Model model) {
        User user = new User(name, surname, password, new BankAccount(0));
        DATABASE.registerUser(user);
        model.addAttribute("message", "Registration successful! Please login.");
        return "login";
    }

    @GetMapping("/main")
    public String mainPage(@RequestParam String name, Model model) {
        User user = DATABASE.readUser(name);
        if (user != null) {
            model.addAttribute("user", user);
            return "main";
        } else {
            return "redirect:/login?name=";
        }
    }


    @PostMapping("/deposit")
    public String deposit(@RequestParam double amount, @RequestParam String name) {
        User user = DATABASE.readUser(name);
        BankAccount bankAccount = user.getBankAccount();
        bankAccount.deposit(amount);
        DATABASE.updateBalance(user);
        return "redirect:/main?name=" + name;
    }


    @PostMapping("/withdraw")
    public String widthdraw(@RequestParam double amount, @RequestParam String name) {
        User user = DATABASE.readUser(name);
        BankAccount bankAccount = user.getBankAccount();
        bankAccount.withdraw(amount);
        DATABASE.updateBalance(user);
        return "redirect:/main?name=" + name;
    }

    @PostMapping("/send_money")
    public String send_money(@RequestParam String receiverN, @RequestParam String receiverS, @RequestParam double amount, @RequestParam String name) throws SQLException {
        DATABASE.sendMoney(receiverN, receiverS, amount, name);
        return "redirect:/main?name=" + name;
    }
}
