package com.rect2m;

import com.rect2m.controller.AuthController;
import com.rect2m.entity.User;
import com.rect2m.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowRegistrationForm() {
        String viewName = authController.showRegistrationForm(model);
        assertEquals("register", viewName);
        verify(model).addAttribute(eq("user"), any(User.class));
        verify(model).addAttribute("pageTitle", "Sign Up");
    }

    @Test
    public void testRegisterUser_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(userService.registerUser(user)).thenReturn(true);

        String viewName = authController.registerUser(user, model, session);
        assertEquals("redirect:/menu", viewName);
        verify(session).setAttribute("username", user.getUsername());
    }

    @Test
    public void testRegisterUser_UserExists() {
        User user = new User();
        user.setUsername("existinguser");
        user.setPassword("password");

        when(userService.registerUser(user)).thenReturn(false);

        String viewName = authController.registerUser(user, model, session);
        assertEquals("register", viewName);
        verify(model).addAttribute("error", "Користувач з таким ім'ям вже існує.");
    }

    @Test
    public void testLogin() {
        String viewName = authController.login(model);
        assertEquals("login", viewName);
        verify(model).addAttribute("pageTitle", "Sign In");
    }

    @Test
    public void testAuthenticateUser() {
        String viewName = authController.authenticateUser(mock(UserDetails.class));
        assertEquals("redirect:/menu", viewName);
    }
}
