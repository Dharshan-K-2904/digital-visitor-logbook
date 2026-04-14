package com.visitorlogbook.controller;

<<<<<<< HEAD
import jakarta.servlet.http.HttpSession;
import com.visitorlogbook.model.User;
import com.visitorlogbook.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
=======
import com.visitorlogbook.dao.UserDAO;
import com.visitorlogbook.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
>>>>>>> branch-4

@Controller
public class AuthController {

    @Autowired
<<<<<<< HEAD
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
=======
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
>>>>>>> branch-4
