package com.rect2m.controller;

import com.rect2m.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    private final AuthService authService;
    public String name;

    @Autowired
    public MenuController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/menu")
    public String menu(Model model, HttpSession session,
            @AuthenticationPrincipal OAuth2User principal) {
        String username_log = authService.getAuthenticatedUsername();
        String username_reg = (String) session.getAttribute("username");

        if (principal != null) {
            String name = principal != null ? principal.getAttribute("name") : null;
            model.addAttribute("username", name);
        } else if (username_log != null && !username_log.equals("anonymousUser")) {
            model.addAttribute("username", username_log);
        } else if (username_reg != null && !username_reg.equals("anonymousUser")) {
            model.addAttribute("username", username_reg);
        } else {
            model.addAttribute("username", null); // Користувач неавторизований
        }

        return "menu";
    }
}
