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

/**
 * HostController – Handles host approval/rejection of visit requests.
 *
 * Design Principles:
 *  - SRP : only responsible for host-facing web interactions.
 *  - DIP : depends on {@link VisitRequestService} interface.
 *  - Separation of Concerns : approve/reject business logic + Observer events
 *                             are delegated to the service layer.
 */
@Controller
@RequestMapping("/host")
public class HostController {

    private final VisitRequestService visitService;
    private final UserManagementService userService;

    public HostController(VisitRequestService visitService, UserManagementService userService) {
        this.visitService = visitService;
        this.userService  = userService;
    }

    // ── Guard ─────────────────────────────────────────────────────────────────

    private User guard(HttpSession s) {
        User u = (User) s.getAttribute("user");
        return (u != null && "HOST".equals(u.getRole())) ? u : null;
    }

    // ── Dashboard ─────────────────────────────────────────────────────────────

    @GetMapping("/dashboard")
    public String dashboard(Model m, HttpSession s) {
        User u = guard(s);
        if (u == null) return "redirect:/login";

        List<VisitRequest> all = visitService.findByHostId(u.getId());
        m.addAttribute("user",      u);
        m.addAttribute("requests",  all);
        m.addAttribute("pending",   all.stream().filter(r -> "PENDING".equals(r.getStatus())).count());
        m.addAttribute("approved",  all.stream().filter(r -> "APPROVED".equals(r.getStatus())).count());
        m.addAttribute("checkedIn", all.stream().filter(r -> "CHECKED_IN".equals(r.getStatus())).count());
        return "host/dashboard";
    }

    // ── Approve ───────────────────────────────────────────────────────────────

    /**
     * Approve a pending visit request.
     * Service fires the REQUEST_APPROVED Observer event internally (Observer Pattern).
     */
    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id, HttpSession s, RedirectAttributes ra) {
        if (guard(s) == null) return "redirect:/login";
        visitService.approve(id);
        ra.addFlashAttribute("success", "Visit request approved.");
        return "redirect:/host/dashboard";
    }

    // ── Reject ────────────────────────────────────────────────────────────────

    /**
     * Reject a pending visit request with an optional reason.
     * Service fires the REQUEST_REJECTED Observer event internally (Observer Pattern).
     */
    @PostMapping("/reject/{id}")
    public String reject(@PathVariable Long id,
                         @RequestParam(required = false) String reason,
                         HttpSession s, RedirectAttributes ra) {
        if (guard(s) == null) return "redirect:/login";
        visitService.reject(id, reason);
        ra.addFlashAttribute("success", "Visit request rejected.");
        return "redirect:/host/dashboard";
    }

    // ── Profile ───────────────────────────────────────────────────────────────

    @GetMapping("/profile")
    public String profile(Model m, HttpSession s) {
        User u = guard(s);
        if (u == null) return "redirect:/login";

        List<VisitRequest> all = visitService.findByHostId(u.getId());
        m.addAttribute("user",          u);
        m.addAttribute("profileForm",   u);
        m.addAttribute("userRole",      u.getRole());
        m.addAttribute("roleIcon",      "bi bi-person-badge-fill");
        m.addAttribute("roleDescription", "Hosts can approve or reject visitor requests and manage their own visit schedule.");
        m.addAttribute("avatarColor",   "linear-gradient(135deg,#2563eb,#1d4ed8)");
        m.addAttribute("profileAction", "/host/profile");
        m.addAttribute("dashboardLink", "/host/dashboard");
        m.addAttribute("statA",  all.stream().filter(r -> "PENDING".equals(r.getStatus())).count());
        m.addAttribute("statALabel",  "Pending");
        m.addAttribute("statB",  all.stream().filter(r -> "APPROVED".equals(r.getStatus())).count());
        m.addAttribute("statBLabel",  "Approved");
        m.addAttribute("statC",  all.stream().filter(r -> "CHECKED_IN".equals(r.getStatus())).count());
        m.addAttribute("statCLabel",  "Checked In");
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
        return "redirect:/host/profile";
    }
}
