package org.example.bankproject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
        DATABASE.main(args);
        logger.info("Application started");
        SpringApplication.run(BankProjectApplication.class, args);
    }

    @GetMapping("/")
    public String homePage() {return "redirect:/login";}

    @GetMapping("/login")
    public String loginPage() {return "login";}

    @GetMapping("/register")
    public String registerPage() {return "register";}

    
    @GetMapping("/main")
    public String mainPage(@RequestParam String email, Model model) {
        User user = DATABASE.readUserByEmail(email);
        if (user != null) {
            model.addAttribute("user", user);
            return "main";
        } else {
            return "redirect:/login?name=";
        }
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model) {
        User user = DATABASE.readUserLogin(email, password);
        if (user != null) {
            model.addAttribute("user", user);
            return "redirect:/main?email=" + email;
        } else {
            model.addAttribute("error", "Invalid email or password.");
            return "redirect:/login";
        }
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String name, @RequestParam String surname, @RequestParam String email, @RequestParam String password, Model model) {
        logger.info("Registering user");
        User user = new User(name, surname, email, password, new BankAccount(0));
        DATABASE.registerUser(user);
        model.addAttribute("message", "Registration successful! Please log in.");
        return "redirect:/login";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam double amount, @RequestParam String email) {
        User user = DATABASE.readUserByEmail(email);
        assert user != null;
        BankAccount bankAccount = user.getBankAccount();
        bankAccount.deposit(amount);
        DATABASE.updateBalance(user);
        return "redirect:/main?email=" + email;
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam double amount, @RequestParam String email) {
        User user = DATABASE.readUserByEmail(email);
        assert user != null;
        BankAccount bankAccount = user.getBankAccount();
        bankAccount.withdraw(amount);
        DATABASE.updateBalance(user);
        return "redirect:/main?email=" + email;
    }

    @PostMapping("/send_money")
    public String sendMoney(@RequestParam String receiverBankAccountNumber, @RequestParam double amount, @RequestParam String email) {
        DATABASE.sendMoney(receiverBankAccountNumber, amount, email);
        return "redirect:/main?email=" + email;
    }
}
