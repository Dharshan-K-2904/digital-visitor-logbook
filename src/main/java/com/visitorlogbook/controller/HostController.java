package com.visitorlogbook.controller;

import com.visitorlogbook.model.User;
import com.visitorlogbook.service.VisitorService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HostController {

    @Autowired
    private VisitorService service;

    // 🔹 Host Dashboard
    @GetMapping("/host")
    public String dashboard(Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");

        model.addAttribute("requests",
                service.getRequests(user.getEmail()));

        return "host-dashboard";
    }

    // 🔹 Approve
    @PostMapping("/approve")
    public String approve(Long id) {
        service.updateStatus(id, "APPROVED");
        return "redirect:/host";
    }

    // 🔹 Reject
    @PostMapping("/reject")
    public String reject(Long id) {
        service.updateStatus(id, "REJECTED");
        return "redirect:/host";
    }
}