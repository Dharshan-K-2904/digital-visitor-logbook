package com.visitorlogbook.controller;

import com.visitorlogbook.dao.UserDAO;
import com.visitorlogbook.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/")
    public String root() { return "redirect:/login"; }

    @GetMapping("/login")
    public String loginPage(HttpSession s) {
        if (s.getAttribute("user") != null) return redirect((User) s.getAttribute("user"));
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password,
                        HttpSession s, RedirectAttributes ra) {
        User u = userDAO.findByEmail(email.trim());
        if (u == null || !u.getPassword().equals(password)) {
            ra.addFlashAttribute("error", "Invalid email or password.");
            return "redirect:/login";
        }
        if (!u.isActive()) {
            ra.addFlashAttribute("error", "Account is disabled. Contact admin.");
            return "redirect:/login";
        }
        s.setAttribute("user", u);
        return redirect(u);
    }

    @GetMapping("/logout")
    public String logout(HttpSession s) {
        s.invalidate();
        return "redirect:/login";
    }

    private String redirect(User u) {
        return switch (u.getRole()) {
            case "ADMIN"    -> "redirect:/admin/dashboard";
            case "HOST"     -> "redirect:/host/dashboard";
            case "SECURITY" -> "redirect:/security/dashboard";
            default         -> "redirect:/visitor/dashboard";
        };
    }
}
