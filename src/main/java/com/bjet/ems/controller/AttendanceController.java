package com.bjet.ems.controller;

import com.bjet.ems.service.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    private AttendanceService attendanceService;

    /**
     * Display the main attendance page
     */
    @GetMapping
    public String attendancePage() {
        return "attendance/attendance-page";
    }

    /**
     * Handle punch-in request
     */
    @PostMapping("/punch-in")
    public String punchIn(RedirectAttributes redirectAttributes) {
        try {
            attendanceService.punchIn();
            redirectAttributes.addFlashAttribute("successMessage", "Successfully punched in!");
        } catch (Exception e) {
            logger.error("Error during punch-in: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/api/v1/attendance";
    }

    /**
     * Handle punch-out request
     */
    @PostMapping("/punch-out")
    public String punchOut(RedirectAttributes redirectAttributes) {
        try {
            attendanceService.punchOut();
            redirectAttributes.addFlashAttribute("successMessage", "Successfully punched out!");
        } catch (Exception e) {
            logger.error("Error during punch-out: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/api/v1/attendance";
    }
}
