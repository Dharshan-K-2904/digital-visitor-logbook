package com.visitorlogbook.controller;

import com.visitorlogbook.dao.UserDAO;
import com.visitorlogbook.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * AuthController - Handles user authentication and registration.
 * Manages login, logout, registration, and role-based redirects.
 */
@Controller
public class AuthController {

    private final UserDAO userDAO;

    /**
     * Constructor injection for UserDAO.
     */
    public AuthController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // Root redirects to login
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    // Login page
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return redirect((User) session.getAttribute("user"));
        }
        return "auth/login";
    }

    // Login process
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes ra) {

        User user = userDAO.findByEmail(email.trim());

        if (user == null || !user.getPassword().equals(password)) {
            ra.addFlashAttribute("error", "Invalid email or password.");
            return "redirect:/login";
        }

        if (!user.isActive()) {
            ra.addFlashAttribute("error", "Account is disabled. Contact admin.");
            return "redirect:/login";
        }

        session.setAttribute("user", user);
        return redirect(user);
    }

    // Register page
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Register process
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                               RedirectAttributes ra) {

        if (userDAO.findByEmail(user.getEmail()) != null) {
            ra.addFlashAttribute("error", "Email already registered.");
            return "redirect:/register";
        }

        user.setActive(true);

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("VISITOR");
        }

        userDAO.save(user);

        ra.addFlashAttribute("success", "Registration successful. Please login.");
        return "redirect:/login";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // Role-based redirect
    private String redirect(User user) {
        return switch (user.getRole()) {
            case "ADMIN"    -> "redirect:/admin/dashboard";
            case "HOST"     -> "redirect:/host/dashboard";
            case "SECURITY" -> "redirect:/security/dashboard";
            default         -> "redirect:/visitor/dashboard";
        };
    }
}