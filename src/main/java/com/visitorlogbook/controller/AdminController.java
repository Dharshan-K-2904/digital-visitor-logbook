package com.visitorlogbook.controller;

import com.visitorlogbook.model.User;
import com.visitorlogbook.model.VisitRequest;
import com.visitorlogbook.service.UserManagementService;
import com.visitorlogbook.service.VisitRequestService;
import com.visitorlogbook.strategy.ReportContext;
import com.visitorlogbook.strategy.ReportStrategy;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * AdminController – Admin dashboard, user management, and report generation.
 *
 * Report generation uses the Strategy Pattern via {@link ReportContext},
 * so CSV, PDF, and Excel formats are fully interchangeable without modifying
 * this controller (Open/Closed Principle).
 *
 * Design Principles:
 *  - SRP : only responsible for admin-facing web interactions.
 *  - DIP : depends on service interfaces, not DAO implementations.
 *  - OCP : new report formats → add a {@link ReportStrategy}; no controller change.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final VisitRequestService   visitService;
    private final UserManagementService userService;
    private final ReportContext         reportContext;

    public AdminController(VisitRequestService visitService,
                           UserManagementService userService,
                           ReportContext reportContext) {
        this.visitService  = visitService;
        this.userService   = userService;
        this.reportContext = reportContext;
    }

    // ── Guard ─────────────────────────────────────────────────────────────────

    /**
     * Session guard – returns the logged-in ADMIN user, or null if unauthorised.
     * Callers assign the result to a local variable once and reuse it,
     * avoiding double session-attribute lookups.
     */
    private User guard(HttpSession s) {
        User u = (User) s.getAttribute("user");
        return (u != null && "ADMIN".equals(u.getRole())) ? u : null;
    }

    // ── Dashboard ─────────────────────────────────────────────────────────────

    @GetMapping("/dashboard")
    public String dashboard(Model m, HttpSession s) {
        User u = guard(s);
        if (u == null) return "redirect:/login";

        m.addAttribute("user",            u);
        m.addAttribute("totalVisitors",   userService.countByRole("VISITOR"));
        m.addAttribute("totalHosts",      userService.countByRole("HOST"));
        m.addAttribute("pendingRequests", visitService.countByStatus("PENDING"));
        m.addAttribute("todayVisits",     visitService.countByDate(LocalDate.now()));
        m.addAttribute("recentLogs",      visitService.findRecent(10));
        return "admin/dashboard";
    }

    // ── Reports ───────────────────────────────────────────────────────────────

    @GetMapping("/reports")
    public String reports(Model m, HttpSession s) {
        User u = guard(s);                              // single lookup
        if (u == null) return "redirect:/login";

        m.addAttribute("user",    u);
        m.addAttribute("logs",    visitService.findAll());
        m.addAttribute("from",    null);
        m.addAttribute("to",      null);
        m.addAttribute("status",  "");
        m.addAttribute("formats", reportContext.availableFormats());
        return "admin/reports";
    }

    @GetMapping("/reports/filter")
    public String filterReports(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String status,
            Model m, HttpSession s) {

        User u = guard(s);                              // single lookup
        if (u == null) return "redirect:/login";

        m.addAttribute("user",    u);
        m.addAttribute("logs",    visitService.findFiltered(from, to, status));
        m.addAttribute("from",    from);
        m.addAttribute("to",      to);
        m.addAttribute("status",  status != null ? status : "");
        m.addAttribute("formats", reportContext.availableFormats());
        return "admin/reports";
    }

    /**
     * Download a report in the requested format.
     *
     * Strategy Pattern: the {@code format} request parameter selects the
     * concrete {@link ReportStrategy} at runtime via {@link ReportContext}.
     * This controller contains zero format-specific logic (Open/Closed Principle).
     *
     * Supported formats: csv | pdf | xlsx
     */
    @GetMapping("/reports/download")
    public void downloadReport(
            @RequestParam(defaultValue = "csv") String format,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String status,
            HttpSession s, HttpServletResponse response) throws IOException {

        if (guard(s) == null) {
            response.sendRedirect("/login");
            return;
        }

        // Fetch filtered records then delegate format selection to ReportContext
        List<VisitRequest> logs = visitService.findFiltered(from, to, status);
        byte[] data             = reportContext.generate(format, logs);

        // Strategy resolves the correct MIME type and file extension
        ReportStrategy strategy = reportContext.getStrategy(format);
        response.setContentType(strategy.getMimeType());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"visitor-report." + strategy.getFileExtension() + "\"");
        response.getOutputStream().write(data);
    }

    // ── User Management ───────────────────────────────────────────────────────

    @GetMapping("/users")
    public String users(Model m, HttpSession s) {
        User u = guard(s);
        if (u == null) return "redirect:/login";

        m.addAttribute("user",  u);
        m.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/users/add")
    public String addForm(Model m, HttpSession s) {
        User u = guard(s);
        if (u == null) return "redirect:/login";

        m.addAttribute("user",     u);
        m.addAttribute("formUser", new User());
        m.addAttribute("isEdit",   false);
        return "admin/user-form";
    }

    @PostMapping("/users/add")
    public String addUser(@ModelAttribute("formUser") User fu,
                          RedirectAttributes ra, HttpSession s) {
        if (guard(s) == null) return "redirect:/login";

        userService.save(fu);
        ra.addFlashAttribute("success", "User \"" + fu.getName() + "\" created.");
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editForm(@PathVariable Long id, Model m, HttpSession s) {
        User u = guard(s);
        if (u == null) return "redirect:/login";

        m.addAttribute("user",     u);
        m.addAttribute("formUser", userService.findById(id));
        m.addAttribute("isEdit",   true);
        return "admin/user-form";
    }

    @PostMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id,
                           @ModelAttribute("formUser") User fu,
                           RedirectAttributes ra, HttpSession s) {
        if (guard(s) == null) return "redirect:/login";

        fu.setId(id);
        userService.update(fu);
        ra.addFlashAttribute("success", "User updated.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra, HttpSession s) {
        if (guard(s) == null) return "redirect:/login";

        userService.delete(id);
        ra.addFlashAttribute("success", "User deleted.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/toggle/{id}")
    public String toggleUser(@PathVariable Long id, RedirectAttributes ra, HttpSession s) {
        if (guard(s) == null) return "redirect:/login";

        userService.toggleStatus(id);
        ra.addFlashAttribute("success", "User status updated.");
        return "redirect:/admin/users";
    }

    // ── Profile ───────────────────────────────────────────────────────────────

    @GetMapping("/profile")
    public String profile(Model m, HttpSession s) {
        User u = guard(s);
        if (u == null) return "redirect:/login";

        m.addAttribute("user",          u);
        m.addAttribute("profileForm",   u);
        m.addAttribute("userRole",      u.getRole());
        m.addAttribute("roleIcon",      "bi bi-shield-lock-fill");
        m.addAttribute("roleDescription", "Administrators have full access to manage users, view reports, and configure the system.");
        m.addAttribute("avatarColor",   "linear-gradient(135deg,#4f46e5,#7c3aed)");
        m.addAttribute("profileAction", "/admin/profile");
        m.addAttribute("dashboardLink", "/admin/dashboard");
        m.addAttribute("statA",         userService.countByRole("VISITOR"));
        m.addAttribute("statALabel",    "Total Visitors");
        m.addAttribute("statB",         userService.countByRole("HOST"));
        m.addAttribute("statBLabel",    "Total Hosts");
        m.addAttribute("statC",         visitService.countByStatus("PENDING"));
        m.addAttribute("statCLabel",    "Pending Requests");
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
        return "redirect:/admin/profile";
    }

    // ── System Settings ───────────────────────────────────────────────────────

    @GetMapping("/settings")
    public String settings(Model m, HttpSession s) {
        User u = guard(s);
        if (u == null) return "redirect:/login";

        m.addAttribute("user", u);
        return "admin/settings";
    }
}
