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

import java.time.LocalDate;
import java.util.List;

/**
 * VisitorController – Handles visitor request submission and tracking.
 *
 * Design Principles:
 *  - SRP : only handles visitor-facing web interactions.
 *  - DIP : depends on service interfaces, not DAO implementations.
 *  - Separation of Concerns : business logic lives in the service layer.
 */
@Controller
@RequestMapping("/visitor")
public class VisitorController {

    private final VisitRequestService   visitService;
    private final UserManagementService userService;

    /** Constructor injection ensures DIP compliance. */
    public VisitorController(VisitRequestService visitService,
                             UserManagementService userService) {
        this.visitService = visitService;
        this.userService  = userService;
    }

    // ── Guards ────────────────────────────────────────────────────────────────

    private User guard(HttpSession s) {
        User u = (User) s.getAttribute("user");
        return (u != null && "VISITOR".equals(u.getRole())) ? u : null;
    }

    // ── Dashboard ─────────────────────────────────────────────────────────────

    @GetMapping("/dashboard")
    public String dashboard(Model m, HttpSession s) {
        User u = guard(s);
        if (u == null) return "redirect:/login";

        List<VisitRequest> reqs = visitService.findByVisitorId(u.getId());
        m.addAttribute("user",     u);
        m.addAttribute("requests", reqs);
        m.addAttribute("pending",  reqs.stream().filter(r -> "PENDING".equals(r.getStatus())).count());
        m.addAttribute("approved", reqs.stream().filter(r -> "APPROVED".equals(r.getStatus())).count());
        m.addAttribute("rejected", reqs.stream().filter(r -> "REJECTED".equals(r.getStatus())).count());
        return "visitor/dashboard";
    }

    // ── New Request Form ──────────────────────────────────────────────────────

    @GetMapping("/request/new")
    public String newForm(Model m, HttpSession s) {
        User u = guard(s);
        if (u == null) return "redirect:/login";
        m.addAttribute("user",  u);
        m.addAttribute("req",   new VisitRequest());
        m.addAttribute("hosts", userService.findByRole("HOST"));
        return "visitor/request-form";
    }

    // ── Submit Request ────────────────────────────────────────────────────────

    /**
     * Submit a new visit request.
     * Controller only assembles the domain object and delegates to the
     * service (SRP); Observer events are fired inside the service layer.
     */
    @PostMapping("/request/submit")
    public String submit(@ModelAttribute("req") VisitRequest req,
                         @RequestParam Long hostId,
                         HttpSession s, RedirectAttributes ra) {
        User u = guard(s);
        if (u == null) return "redirect:/login";

        User host = userService.findById(hostId);
        if (host == null) {
            ra.addFlashAttribute("error", "Invalid host selected.");
            return "redirect:/visitor/request/new";
        }

        req.setVisitorId(u.getId());
        req.setVisitorName(u.getName());
        req.setHostId(hostId);
        req.setHostName(host.getName());
        if (req.getVisitDate() == null) req.setVisitDate(LocalDate.now());

        // Service handles persistence + fires Observer event (REQUEST_SUBMITTED).
        visitService.submit(req);

        ra.addFlashAttribute("success", "Visit request submitted successfully!");
        return "redirect:/visitor/dashboard";
    }

    // ── Check-In (Self) ───────────────────────────────────────────────────────

    @PostMapping("/check-in/{id}")
    public String checkIn(@PathVariable Long id, HttpSession s, RedirectAttributes ra) {
        User u = guard(s);
        if (u == null) return "redirect:/login";

        // Security check: ensure the request belongs to this visitor
        VisitRequest req = visitService.findAll().stream()
                .filter(r -> r.getId().equals(id) && r.getVisitorId().equals(u.getId()))
                .findFirst().orElse(null);

        if (req == null) {
            ra.addFlashAttribute("error", "Request not found.");
            return "redirect:/visitor/dashboard";
        }

        boolean success = visitService.checkIn(id);
        if (success) {
            ra.addFlashAttribute("success", "You have checked in successfully.");
        } else {
            ra.addFlashAttribute("error", "Check-in failed. Ensure your request is APPROVED.");
        }
        return "redirect:/visitor/dashboard";
    }

    // ── Check-Out (Self) ──────────────────────────────────────────────────────

    @PostMapping("/check-out/{id}")
    public String checkOut(@PathVariable Long id, HttpSession s, RedirectAttributes ra) {
        User u = guard(s);
        if (u == null) return "redirect:/login";

        boolean success = visitService.checkOut(id);
        if (success) {
            ra.addFlashAttribute("success", "You have checked out successfully.");
        } else {
            ra.addFlashAttribute("error", "Check-out failed.");
        }
        return "redirect:/visitor/dashboard";
    }

    // ── Profile ───────────────────────────────────────────────────────────────

    @GetMapping("/profile")
    public String profile(Model m, HttpSession s) {
        User u = guard(s);
        if (u == null) return "redirect:/login";

        List<VisitRequest> reqs = visitService.findByVisitorId(u.getId());
        m.addAttribute("user",          u);
        m.addAttribute("profileForm",   u);
        m.addAttribute("userRole",      u.getRole());
        m.addAttribute("roleIcon",      "bi bi-person-circle");
        m.addAttribute("roleDescription", "Visitors can submit visit requests and track their approval status in real time.");
        m.addAttribute("avatarColor",   "linear-gradient(135deg,#059669,#047857)");
        m.addAttribute("profileAction", "/visitor/profile");
        m.addAttribute("dashboardLink", "/visitor/dashboard");
        m.addAttribute("statA",  reqs.stream().filter(r -> "PENDING".equals(r.getStatus())).count());
        m.addAttribute("statALabel",  "Pending");
        m.addAttribute("statB",  reqs.stream().filter(r -> "APPROVED".equals(r.getStatus())).count());
        m.addAttribute("statBLabel",  "Approved");
        m.addAttribute("statC",  reqs.stream().filter(r -> "REJECTED".equals(r.getStatus())).count());
        m.addAttribute("statCLabel",  "Rejected");
        return "profile";
    }

    @PostMapping("/profile")
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
        return "redirect:/visitor/profile";
    }
}
