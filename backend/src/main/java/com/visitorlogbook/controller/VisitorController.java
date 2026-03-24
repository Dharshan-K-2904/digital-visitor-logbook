package com.visitorlogbook.controller;

import com.visitorlogbook.model.User;
import com.visitorlogbook.model.Visitor;
import com.visitorlogbook.service.VisitorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class VisitorController {

    private final VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String phone,
                           @RequestParam String org,
                           Model model) {

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            model.addAttribute("error", "All fields required");
            return "register";
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("VISITOR");

        Visitor visitor = new Visitor();
        visitor.setPhone(phone);
        visitor.setOrganization(org);

        boolean success = visitorService.registerVisitor(user, visitor);

        if (!success) {
            model.addAttribute("error", "Email already exists");
            return "register";
        }

        return "login";
    }
}