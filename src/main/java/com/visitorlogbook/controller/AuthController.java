package com.visitorlogbook.controller;

import com.visitorlogbook.factory.UserFactoryRegistry;
import com.visitorlogbook.model.User;
import com.visitorlogbook.model.UserRole;
import com.visitorlogbook.service.UserManagementService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * AuthController – Handles user authentication and self-registration.
 *
 * Design principles applied:
 *  - SRP : only responsible for auth / session concerns.
 *  - DIP : depends on {@link UserManagementService} interface, not any DAO impl.
 *  - Factory Method : uses {@link UserFactoryRegistry} to select the correct
 *                     concrete {@link com.visitorlogbook.factory.UserFactory}
 *                     at runtime (GoF Factory Method Pattern).
 */
@Controller
public class AuthController {

    private final UserManagementService userService;
    private final UserFactoryRegistry   factoryRegistry;

    /**
     * Constructor injection – both collaborators are injected as abstractions (DIP).
     */
    public AuthController(UserManagementService userService,
                          UserFactoryRegistry factoryRegistry) {
        this.userService     = userService;
        this.factoryRegistry = factoryRegistry;
    }

    // ── Root ──────────────────────────────────────────────────────────────────

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return dashboardFor((User) session.getAttribute("user"));
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes ra) {

        // Delegates auth concern to service layer (SRP, DIP).
        User user = userService.authenticate(email, password);

        if (user == null) {
            ra.addFlashAttribute("error", "Invalid credentials or account disabled.");
            return "redirect:/login";
        }

        session.setAttribute("user", user);
        return dashboardFor(user);
    }

    // ── Registration ──────────────────────────────────────────────────────────

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User formUser, RedirectAttributes ra) {
        // GoF Factory Method Pattern:
        // UserFactoryRegistry selects VisitorUserFactory at runtime and delegates
        // to its createUser() factory method via the getUser() template method.
        User newUser;
        try {
            UserRole role = UserRole.fromString(formUser.getRole());
            newUser = factoryRegistry.create(
                    formUser.getName(),
                    formUser.getEmail(),
                    formUser.getPassword(),
                    role
            );
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/register";
        }

        // Service handles duplicate-email check (SRP, DIP).
        User saved = userService.register(newUser);
        if (saved == null) {
            ra.addFlashAttribute("error", "Email already registered.");
            return "redirect:/register";
        }

        ra.addFlashAttribute("success", "Registration successful! Please log in.");
        return "redirect:/login";
    }

    // ── Logout ────────────────────────────────────────────────────────────────

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Role-based redirect after successful login.
     * Uses {@link UserRole} enum so no raw String literals appear here (OCP).
     */
    private String dashboardFor(User user) {
        UserRole role = UserRole.fromString(user.getRole());
        return "redirect:" + role.getDefaultPath();
    }
}