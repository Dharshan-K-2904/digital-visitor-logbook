package com.visitorlogbook.controller;

<<<<<<< HEAD
import com.visitorlogbook.model.User;
import com.visitorlogbook.service.VisitorService;

=======
import com.visitorlogbook.dao.VisitRequestDAO;
import com.visitorlogbook.model.User;
import com.visitorlogbook.model.VisitRequest;
>>>>>>> branch-4
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD

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
=======
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/host")
public class HostController {

    @Autowired private VisitRequestDAO visitDAO;

    private User guard(HttpSession s) {
        User u = (User) s.getAttribute("user");
        return (u != null && "HOST".equals(u.getRole())) ? u : null;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model m, HttpSession s) {
        User u = guard(s);
        if (u == null) return "redirect:/login";
        List<VisitRequest> all = visitDAO.findByHostId(u.getId());
        m.addAttribute("user",      u);
        m.addAttribute("requests",  all);
        m.addAttribute("pending",   all.stream().filter(r -> "PENDING".equals(r.getStatus())).count());
        m.addAttribute("approved",  all.stream().filter(r -> "APPROVED".equals(r.getStatus())).count());
        m.addAttribute("checkedIn", all.stream().filter(r -> "CHECKED_IN".equals(r.getStatus())).count());
        return "host/dashboard";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id, HttpSession s, RedirectAttributes ra) {
        if (guard(s) == null) return "redirect:/login";
        visitDAO.updateStatus(id, "APPROVED", null);
        ra.addFlashAttribute("success", "Request approved.");
        return "redirect:/host/dashboard";
    }

    @PostMapping("/reject/{id}")
    public String reject(@PathVariable Long id,
                         @RequestParam(required = false) String reason,
                         HttpSession s, RedirectAttributes ra) {
        if (guard(s) == null) return "redirect:/login";
        visitDAO.updateStatus(id, "REJECTED", reason);
        ra.addFlashAttribute("success", "Request rejected.");
        return "redirect:/host/dashboard";
    }
}
>>>>>>> branch-4
