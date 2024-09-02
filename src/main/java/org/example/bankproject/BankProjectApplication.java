package org.example.bankproject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@Controller
public class BankProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankProjectApplication.class, args);
    }

    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String name, @RequestParam String password, Model model) {
        User user = DATABASE.readUser(name, password);
        if (user != null) {
            model.addAttribute("user", user);
            return "redirect:/main?name=" + name + "&password=" + password;
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
    public String mainPage(@RequestParam String name, @RequestParam String password, Model model) {
        User user = DATABASE.readUser(name, password);
        if (user != null) {
            model.addAttribute("user", user);
            return "main";
        } else {
            return "login";
        }
    }

//    @PostMapping("/deposit")
//    public String deposit(@RequestParam double amount, @RequestParam String name, @RequestParam String password, Model model) {
//        User user = DATABASE.readUser(name, password);
//        if (user != null) {
//            user.getBankAccount().deposit(amount);
//            DATABASE.updateBalance(user);
//            model.addAttribute("user", user);
//            model.addAttribute("message", "Deposit successful!");
//            return "main";
//        } else {
//            return "login";
//        }
//    }
//
//    @PostMapping("/withdraw")
//    public String withdraw(@RequestParam double amount, @RequestParam String name, @RequestParam String password, Model model) {
//        User user = DATABASE.readUser(name, password);
//        if (user != null) {
//            user.getBankAccount().withdraw(amount);
//            DATABASE.updateBalance(user);
//            model.addAttribute("user", user);
//            model.addAttribute("message", "Withdrawal successful!");
//            return "main";
//        } else {
//            return "login";
//        }
//    }
//
//    @PostMapping("/send")
//    public String sendMoney(@RequestParam String receiver, @RequestParam double amount, @RequestParam String name, @RequestParam String password, Model model) {
//        User user = DATABASE.readUser(name, password);
//        if (user != null) {
//            String[] nameParts = receiver.split(" ");
//            String firstName = nameParts[0];
//            String lastName = nameParts[1];
//            DATABASE.sendMoney(firstName, lastName, amount);
//            user.getBankAccount().withdraw(amount);
//            DATABASE.updateBalance(user);
//            model.addAttribute("user", user);
//            model.addAttribute("message", "Money sent successfully!");
//            return "main";
//        } else {
//            return "login";
//        }
//    }
//
//    @PostMapping("/logout")
//    public String logout() {
//        return "redirect:/login";
//    }
}
