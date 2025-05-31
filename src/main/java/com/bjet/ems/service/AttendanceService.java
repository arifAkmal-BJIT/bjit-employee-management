package com.bjet.ems.service;

/**
 * Service interface for employee attendance operations.
 */
public interface AttendanceService {
    
    /**
     * Records a punch-in for the current employee.
     * 
     * @throws IllegalStateException if employee has already punched in
     */
    void punchIn();
    
    /**
     * Records a punch-out for the current employee.
     * 
     * @throws IllegalStateException if employee has not punched in or has already punched out
     * @throws IllegalArgumentException if punch-out time is before punch-in time
     */
    void punchOut();
}
