package com.visitorlogbook.controller;

import com.visitorlogbook.model.VisitorRequest;
import com.visitorlogbook.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * MEMBER 3: Security Controller
 * Handles visitor check-in, check-out, and search functionality
 */
@Controller
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private VisitorService visitorService;

    /**
     * Security Dashboard - Shows pending check-ins and currently checked-in visitors
     */
    @GetMapping
    public String securityDashboard(Model model,
                                   @RequestParam(required = false) String status,
                                   @RequestParam(required = false) String search) {
        
        List<VisitorRequest> visitors;
        
        // Search and filter logic
        if ((status != null && !status.isEmpty()) || (search != null && !search.isEmpty())) {
            visitors = visitorService.searchVisitors(
                (status != null && !status.isEmpty()) ? status : null,
                (search != null && !search.isEmpty()) ? search : null
            );
        } else {
            // Default: show all visitors
            visitors = visitorService.getAllVisitors();
        }
        
        // Get pending check-ins and currently checked-in for quick stats
        List<VisitorRequest> pendingCheckIns = visitorService.getPendingCheckIns();
        List<VisitorRequest> currentlyCheckedIn = visitorService.getCurrentlyCheckedIn();
        
        model.addAttribute("visitors", visitors);
        model.addAttribute("pendingCount", pendingCheckIns.size());
        model.addAttribute("checkedInCount", currentlyCheckedIn.size());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("searchTerm", search);
        
        return "security-dashboard";
    }

    /**
     * UC-04: Check-In a Visitor
     * Security personnel verifies approval and logs entry time
     */
    @PostMapping("/check-in/{id}")
    public String checkIn(@PathVariable Long id, Model model) {
        boolean success = visitorService.checkIn(id);
        
        if (success) {
            model.addAttribute("message", "Visitor checked in successfully!");
        } else {
            model.addAttribute("error", "Failed to check in. Visitor may not be approved or already checked in.");
        }
        
        return "redirect:/security?checkin=success";
    }

    /**
     * UC-05: Check-Out a Visitor
     * Security personnel logs exit time and calculates duration
     */
    @PostMapping("/check-out/{id}")
    public String checkOut(@PathVariable Long id, Model model) {
        boolean success = visitorService.checkOut(id);
        
        if (success) {
            // Get the updated request to show duration
            Optional<VisitorRequest> request = visitorService.getRequestById(id);
            if (request.isPresent()) {
                model.addAttribute("message", "Visitor checked out successfully! Visit duration: " 
                    + request.get().getVisitDuration() + " minutes");
            }
        } else {
            model.addAttribute("error", "Failed to check out. Visitor may not be checked in.");
        }
        
        return "redirect:/security?checkout=success";
    }

    /**
     * View Visitor Details
     */
    @GetMapping("/visitor/{id}")
    public String viewVisitorDetails(@PathVariable Long id, Model model) {
        Optional<VisitorRequest> request = visitorService.getRequestById(id);
        
        if (request.isPresent()) {
            model.addAttribute("visitor", request.get());
            return "visitor-details";
        } else {
            return "redirect:/security?error=notfound";
        }
    }
}
