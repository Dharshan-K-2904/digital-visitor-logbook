package com.visitorlogbook.controller;

import jakarta.servlet.http.HttpSession;
import com.visitorlogbook.model.User;
import com.visitorlogbook.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService service;

    // 🔹 HOME → redirect to login
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    // 🔹 REGISTER PAGE
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // 🔹 REGISTER USER
    @PostMapping("/register")
    public String register(User user) {
        service.register(user);
        return "redirect:/login";
    }

    // 🔹 LOGIN PAGE
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // 🔹 LOGIN LOGIC (FIXED ✅)
    @PostMapping("/login")
    public String login(String email, String password, Model model, HttpSession session) {

        User user = service.login(email, password);

        if (user == null) {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }

        // ✅ store user in session
        session.setAttribute("user", user);

        // ✅ role-based redirect
        if (user.getRole().equals("VISITOR"))
            return "redirect:/visitor";

        if (user.getRole().equals("HOST"))
            return "redirect:/host";
        
        if (user.getRole().equals("SECURITY"))
            return "redirect:/security";
        
        if (user.getRole().equals("ADMIN"))
            return "redirect:/admin";

        return "login";
    }

    // 🔹 LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // clear session
        return "redirect:/login";
    }
}