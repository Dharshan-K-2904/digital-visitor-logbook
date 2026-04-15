package com.visitorlogbook.controller;

import com.visitorlogbook.dao.UserDAO;
import com.visitorlogbook.dao.VisitRequestDAO;
import com.visitorlogbook.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

/**
 * AdminController - Handles admin dashboard and user management.
 * Manages user CRUD operations, reports, and system statistics.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final VisitRequestDAO visitDAO;
    private final UserDAO userDAO;

    /**
     * Constructor injection for DAOs.
     */
    public AdminController(VisitRequestDAO visitDAO, UserDAO userDAO) {
        this.visitDAO = visitDAO;
        this.userDAO = userDAO;
    }

    private User guard(HttpSession s) {
        User u = (User) s.getAttribute("user");
        return (u != null && "ADMIN".equals(u.getRole())) ? u : null;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model m, HttpSession s) {
        if (guard(s) == null) return "redirect:/login";
        m.addAttribute("user",            guard(s));
        m.addAttribute("totalVisitors",   userDAO.countByRole("VISITOR"));
        m.addAttribute("totalHosts",      userDAO.countByRole("HOST"));
        m.addAttribute("pendingRequests", visitDAO.countByStatus("PENDING"));
        m.addAttribute("todayVisits",     visitDAO.countByDate(LocalDate.now()));
        m.addAttribute("recentLogs",      visitDAO.findRecent(10));
        return "admin/dashboard";
    }

    @GetMapping("/reports")
    public String reports(Model m, HttpSession s) {
        if (guard(s) == null) return "redirect:/login";
        m.addAttribute("user",   guard(s));
        m.addAttribute("logs",   visitDAO.findAll());
        m.addAttribute("from",   null);
        m.addAttribute("to",     null);
        m.addAttribute("status", "");
        return "admin/reports";
    }

    @GetMapping("/reports/filter")
    public String filterReports(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String status,
            Model m, HttpSession s) {
        if (guard(s) == null) return "redirect:/login";
        m.addAttribute("user",   guard(s));
        m.addAttribute("logs",   visitDAO.findFiltered(from, to, status));
        m.addAttribute("from",   from);
        m.addAttribute("to",     to);
        m.addAttribute("status", status != null ? status : "");
        return "admin/reports";
    }

    @GetMapping("/users")
    public String users(Model m, HttpSession s) {
        if (guard(s) == null) return "redirect:/login";
        m.addAttribute("user",  guard(s));
        m.addAttribute("users", userDAO.findAll());
        return "admin/users";
    }

    @GetMapping("/users/add")
    public String addForm(Model m, HttpSession s) {
        if (guard(s) == null) return "redirect:/login";
        m.addAttribute("user",     guard(s));
        m.addAttribute("formUser", new User());
        m.addAttribute("isEdit",   false);
        return "admin/user-form";
    }

    @PostMapping("/users/add")
    public String addUser(@ModelAttribute("formUser") User fu, RedirectAttributes ra, HttpSession s) {
        if (guard(s) == null) return "redirect:/login";
        userDAO.save(fu);
        ra.addFlashAttribute("success", "User \"" + fu.getName() + "\" created.");
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editForm(@PathVariable Long id, Model m, HttpSession s) {
        if (guard(s) == null) return "redirect:/login";
        m.addAttribute("user",     guard(s));
        m.addAttribute("formUser", userDAO.findById(id));
        m.addAttribute("isEdit",   true);
        return "admin/user-form";
    }

    @PostMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, @ModelAttribute("formUser") User fu,
                           RedirectAttributes ra, HttpSession s) {
        if (guard(s) == null) return "redirect:/login";
        fu.setId(id);
        userDAO.update(fu);
        ra.addFlashAttribute("success", "User updated.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra, HttpSession s) {
        if (guard(s) == null) return "redirect:/login";
        userDAO.delete(id);
        ra.addFlashAttribute("success", "User deleted.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/toggle/{id}")
    public String toggleUser(@PathVariable Long id, RedirectAttributes ra, HttpSession s) {
        if (guard(s) == null) return "redirect:/login";
        userDAO.toggleStatus(id);
        ra.addFlashAttribute("success", "User status updated.");
        return "redirect:/admin/users";
    }
}
