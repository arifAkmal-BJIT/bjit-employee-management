package com.bjet.ems.service.impl;

import com.bjet.ems.model.Punch;
import com.bjet.ems.repository.PunchRepository;
import com.bjet.ems.service.AttendanceService;
import com.bjet.ems.util.TokenExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    @Autowired
    private PunchRepository punchRepository;

    @Override
    @Transactional
    public void punchIn() {
        Long empId = TokenExtractor.getEmpId();
        LocalDate today = LocalDate.now();
        
        logger.info("Processing punch-in request for employee ID: {}", empId);

        // Check if employee has already punched in today without punching out
        List<Punch> todayPunches = punchRepository.findByEmpIdAndDate(empId, today);
        if (!todayPunches.isEmpty()) {
            Punch lastPunch = todayPunches.get(todayPunches.size() - 1);
            if (lastPunch.getCheckout() == null) {
                logger.warn("Employee {} attempted to punch in again without punching out", empId);
                throw new IllegalStateException("Already punched in");
            }
        }

        // Create new punch record
        Punch punch = new Punch();
        punch.setEmpId(empId);
        punch.setDate(today);
        punch.setCheckin(LocalDateTime.now());
        
        punchRepository.save(punch);
        logger.info("Employee {} successfully punched in at {}", empId, punch.getCheckin());
    }

    @Override
    @Transactional
    public void punchOut() {
        Long empId = TokenExtractor.getEmpId();
        LocalDate today = LocalDate.now();
        
        logger.info("Processing punch-out request for employee ID: {}", empId);

        // Find today's punches
        List<Punch> todayPunches = punchRepository.findByEmpIdAndDate(empId, today);
        
        // Validate employee has punched in
        if (todayPunches.isEmpty()) {
            logger.warn("Employee {} attempted to punch out without punching in", empId);
            throw new IllegalStateException("Not punched in");
        }

        // Get the latest punch record
        Punch lastPunch = todayPunches.get(todayPunches.size() - 1);
        
        // Validate employee hasn't already punched out
        if (lastPunch.getCheckout() != null) {
            logger.warn("Employee {} attempted to punch out again", empId);
            throw new IllegalStateException("Already punched out");
        }

        LocalDateTime punchOutTime = LocalDateTime.now();
        
        // Validate punch-out time is after punch-in time
        if (punchOutTime.isBefore(lastPunch.getCheckin())) {
            logger.error("Invalid punch-out time for employee {}: punch-out time before punch-in time", empId);
            throw new IllegalArgumentException("Punch-out time cannot be before punch-in time");
        }

        // Update punch record
        lastPunch.setCheckout(punchOutTime);
        punchRepository.save(lastPunch);
        
        logger.info("Employee {} successfully punched out at {}", empId, punchOutTime);
    }
}
