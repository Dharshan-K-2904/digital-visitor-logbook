package com.visitorlogbook.controller;

import com.visitorlogbook.model.User;
import com.visitorlogbook.model.VisitRequest;
import com.visitorlogbook.service.UserManagementService;
import com.visitorlogbook.service.VisitRequestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SecurityController – Handles visitor check-in / check-out at reception.
 */
@Controller
public class SecurityController {

    private final VisitRequestService visitService;
    private final UserManagementService userService;

    public SecurityController(VisitRequestService visitService, UserManagementService userService) {
        this.visitService = visitService;
        this.userService  = userService;
    }

    private User guard(HttpSession s) {
        User u = (User) s.getAttribute("user");
        return (u != null && "SECURITY".equals(u.getRole())) ? u : null;
    }

    @GetMapping({"/security", "/security/dashboard"})
    public String dashboard(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "") String status,
            Model m, HttpSession s) {

        User u = guard(s);
        if (u == null) return "redirect:/login";

        List<VisitRequest> all = visitService.findAll();

        List<VisitRequest> filtered = all.stream()
                .filter(r -> matchesSearch(r, search))
                .filter(r -> status.isBlank() || status.equals(r.getStatus()))
                .collect(Collectors.toList());

        m.addAttribute("user",           u);
        m.addAttribute("visitors",       filtered);
        m.addAttribute("pendingCount",   countByStatus(all, "APPROVED"));
        m.addAttribute("checkedInCount", countByStatus(all, "CHECKED_IN"));
        m.addAttribute("searchTerm",     search);
        m.addAttribute("selectedStatus", status);
        return "security/dashboard";
    }

    @PostMapping("/security/check-in/{id}")
    public String checkIn(@PathVariable Long id, HttpSession s, RedirectAttributes ra) {
        if (guard(s) == null) return "redirect:/login";

        boolean success = visitService.checkIn(id);
        if (success) {
            ra.addFlashAttribute("success", "Visitor checked in successfully.");
        } else {
            ra.addFlashAttribute("error", "Check-in failed.");
        }
        return "redirect:/security";
    }

    @PostMapping("/security/check-out/{id}")
    public String checkOut(@PathVariable Long id, HttpSession s, RedirectAttributes ra) {
        if (guard(s) == null) return "redirect:/login";

        boolean success = visitService.checkOut(id);
        if (success) {
            ra.addFlashAttribute("success", "Visitor checked out.");
        } else {
            ra.addFlashAttribute("error", "Check-out failed.");
        }
        return "redirect:/security";
    }

    @GetMapping("/security/visitor/{id}")
    public String viewVisitor(@PathVariable Long id, Model m, HttpSession s) {
        User u = guard(s);
        if (u == null) return "redirect:/login";

        VisitRequest req = visitService.findAll().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst().orElse(null);

        if (req == null) return "redirect:/security";

        m.addAttribute("user", u);
        m.addAttribute("req",  req);
        return "security/visitor-detail";
    }

    private boolean matchesSearch(VisitRequest r, String term) {
        if (term == null || term.isBlank()) return true;
        String t = term.toLowerCase();
        return (r.getVisitorName() != null && r.getVisitorName().toLowerCase().contains(t))
            || (r.getHostName()    != null && r.getHostName().toLowerCase().contains(t));
    }

    private long countByStatus(List<VisitRequest> list, String status) {
        return list.stream().filter(r -> status.equals(r.getStatus())).count();
    }

    @GetMapping("/security/profile")
    public String profile(Model m, HttpSession s) {
        User u = guard(s);
        if (u == null) return "redirect:/login";

        List<VisitRequest> all = visitService.findAll();
        m.addAttribute("user",          u);
        m.addAttribute("profileForm",   u);
        m.addAttribute("userRole",      u.getRole());
        m.addAttribute("roleIcon",      "bi bi-shield-check");
        m.addAttribute("roleDescription", "Security personnel manage visitor check-ins and check-outs.");
        m.addAttribute("avatarColor",   "linear-gradient(135deg,#7c3aed,#5b21b6)");
        m.addAttribute("profileAction", "/security/profile");
        m.addAttribute("dashboardLink", "/security/dashboard");
        m.addAttribute("statA",  countByStatus(all, "APPROVED"));
        m.addAttribute("statALabel",  "Awaiting In");
        m.addAttribute("statB",  countByStatus(all, "CHECKED_IN"));
        m.addAttribute("statBLabel",  "Checked In");
        m.addAttribute("statC",  countByStatus(all, "CHECKED_OUT"));
        m.addAttribute("statCLabel",  "Checked Out");
        return "profile";
    }

    @PostMapping("/security/profile")
    public String updateProfile(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String newPassword,
            HttpSession s, RedirectAttributes ra) {
        User u = guard(s);
        if (u == null) return "redirect:/login";

        u.setName(name);
        u.setEmail(email);
        if (newPassword != null && !newPassword.isBlank()) {
            u.setPassword(newPassword);
        }
        userService.update(u);
        s.setAttribute("user", u);
        ra.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/security/profile";
    }
}
