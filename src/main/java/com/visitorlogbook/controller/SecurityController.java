package com.visitorlogbook.controller;

import com.visitorlogbook.dao.VisitRequestDAO;
import com.visitorlogbook.model.User;
import com.visitorlogbook.model.VisitRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * SecurityController - Handles visitor check-in/check-out operations.
 * Manages the physical access log for visitors.
 */
@Controller
@RequestMapping("/security")
public class SecurityController {

    private final VisitRequestDAO visitDAO;

    /**
     * Constructor injection for VisitRequestDAO.
     */
    public SecurityController(VisitRequestDAO visitDAO) {
        this.visitDAO = visitDAO;
    }

    private User guard(HttpSession s) {
        User u = (User) s.getAttribute("user");
        return (u != null && "SECURITY".equals(u.getRole())) ? u : null;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model m, HttpSession s) {
        if (guard(s) == null) return "redirect:/login";
        m.addAttribute("user",      guard(s));
        m.addAttribute("approved",  visitDAO.findByStatus("APPROVED"));
        m.addAttribute("checkedIn", visitDAO.findByStatus("CHECKED_IN"));
        return "security/dashboard";
    }

    @PostMapping("/checkin/{id}")
    public String checkIn(@PathVariable Long id, HttpSession s, RedirectAttributes ra) {
        if (guard(s) == null) return "redirect:/login";
        VisitRequest r = visitDAO.findById(id);
        if (r == null || !"APPROVED".equals(r.getStatus())) {
            ra.addFlashAttribute("error", "Request not found or not approved.");
            return "redirect:/security/dashboard";
        }
        visitDAO.updateCheckIn(id);
        ra.addFlashAttribute("success", "Visitor checked in.");
        return "redirect:/security/dashboard";
    }

    @PostMapping("/checkout/{id}")
    public String checkOut(@PathVariable Long id, HttpSession s, RedirectAttributes ra) {
        if (guard(s) == null) return "redirect:/login";
        visitDAO.updateCheckOut(id);
        ra.addFlashAttribute("success", "Visitor checked out. Duration recorded.");
        return "redirect:/security/dashboard";
    }
}
