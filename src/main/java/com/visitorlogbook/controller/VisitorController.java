package com.visitorlogbook.controller;

<<<<<<< HEAD
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
=======
import com.visitorlogbook.dao.UserDAO;
import com.visitorlogbook.dao.VisitRequestDAO;
import com.visitorlogbook.model.User;
import com.visitorlogbook.model.VisitRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/visitor")
public class VisitorController {

    @Autowired private VisitRequestDAO visitDAO;
    @Autowired private UserDAO userDAO;

    private User guard(HttpSession s) {
        User u = (User) s.getAttribute("user");
        return (u != null && "VISITOR".equals(u.getRole())) ? u : null;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model m, HttpSession s) {
        User u = guard(s);
        if (u == null) return "redirect:/login";
        List<VisitRequest> reqs = visitDAO.findByVisitorId(u.getId());
        m.addAttribute("user",     u);
        m.addAttribute("requests", reqs);
        m.addAttribute("pending",  reqs.stream().filter(r -> "PENDING".equals(r.getStatus())).count());
        m.addAttribute("approved", reqs.stream().filter(r -> "APPROVED".equals(r.getStatus())).count());
        m.addAttribute("rejected", reqs.stream().filter(r -> "REJECTED".equals(r.getStatus())).count());
        return "visitor/dashboard";
    }

    @GetMapping("/request/new")
    public String newForm(Model m, HttpSession s) {
        User u = guard(s);
        if (u == null) return "redirect:/login";
        m.addAttribute("user",  u);
        m.addAttribute("req",   new VisitRequest());
        m.addAttribute("hosts", userDAO.findByRole("HOST"));
        return "visitor/request-form";
    }

    @PostMapping("/request/submit")
    public String submit(@ModelAttribute("req") VisitRequest req,
                         @RequestParam Long hostId,
                         HttpSession s, RedirectAttributes ra) {
        User u = guard(s);
        if (u == null) return "redirect:/login";
        User host = userDAO.findById(hostId);
        if (host == null) { ra.addFlashAttribute("error", "Invalid host."); return "redirect:/visitor/request/new"; }
        req.setVisitorId(u.getId());
        req.setVisitorName(u.getName());
        req.setHostId(hostId);
        req.setHostName(host.getName());
        if (req.getVisitDate() == null) req.setVisitDate(LocalDate.now());
        visitDAO.save(req);
        ra.addFlashAttribute("success", "Visit request submitted!");
        return "redirect:/visitor/dashboard";
    }
}
>>>>>>> branch-4
