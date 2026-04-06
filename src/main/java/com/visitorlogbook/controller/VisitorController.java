package com.visitorlogbook.controller;

import com.visitorlogbook.model.User;
import com.visitorlogbook.model.VisitorRequest;
import com.visitorlogbook.service.VisitorService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class VisitorController {

    @Autowired
    private VisitorService service;

    // 🔹 Visitor Dashboard
    @GetMapping("/visitor")
    public String visitorPage() {
        return "visitor-dashboard";
    }

    // 🔹 Send Request
    @PostMapping("/request")
    public String create(VisitorRequest request, HttpSession session) {

        User user = (User) session.getAttribute("user");

        request.setVisitorId(user.getId()); // auto set visitor
        request.setVisitorName(user.getName()); // set visitor name for searching
        service.createRequest(request);

        return "redirect:/visitor";
    }
}