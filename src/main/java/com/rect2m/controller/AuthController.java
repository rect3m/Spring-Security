package com.rect2m.controller;

import com.rect2m.entity.User;
import com.rect2m.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Sign Up");
        return "register"; // Назва вашого HTML шаблону для реєстрації
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model, HttpSession session) {
        boolean registered = userService.registerUser(user);
        if (!registered) {
            model.addAttribute("error", "Користувач з таким ім'ям вже існує.");
            return "register"; // Повертаємо на форму реєстрації з помилкою
        }
        session.setAttribute("username", user.getUsername());
        return "redirect:/menu"; // Перенаправлення на сторінку входу після успішної реєстрації
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "Sign In");
        return "login"; // Повертає HTML-сторінку для входу
    }

    @PostMapping("/login")
    public String authenticateUser(@AuthenticationPrincipal UserDetails userDetails) {
        // Цей метод викликається після успішної аутентифікації
        return "redirect:/menu"; // Перенаправляємо на домашню сторінку
    }
}
