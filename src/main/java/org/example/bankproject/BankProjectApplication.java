package org.example.bankproject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@Controller
public class BankProjectApplication {
    private static final Logger logger = LogManager.getLogger(BankProjectApplication.class);
    public static void main(String[] args) {

        logger.info("Application started");
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

}
