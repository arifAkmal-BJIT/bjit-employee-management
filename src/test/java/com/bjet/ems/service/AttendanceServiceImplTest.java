package com.bjet.ems.service;

import com.bjet.ems.model.Punch;
import com.bjet.ems.repository.PunchRepository;
import com.bjet.ems.service.impl.AttendanceServiceImpl;
import com.bjet.ems.util.TokenExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceImplTest {

    @Mock
    private PunchRepository punchRepository;

    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    private final Long testEmpId = 123L;
    private final LocalDate today = LocalDate.now();

    @BeforeEach
    void setUp() {
        // Clear any previous mock configurations
        reset(punchRepository);
    }

    @Test
    void punchIn_Success() {
        // Mock empty list of punches (employee hasn't punched in today)
        when(punchRepository.findByEmpIdAndDate(eq(testEmpId), eq(today)))
            .thenReturn(new ArrayList<>());
        
        try (MockedStatic<TokenExtractor> tokenExtractor = Mockito.mockStatic(TokenExtractor.class)) {
            tokenExtractor.when(TokenExtractor::getEmpId).thenReturn(testEmpId);
            
            // Execute the method
            attendanceService.punchIn();
            
            // Verify repository was called to save the punch
            verify(punchRepository, times(1)).save(any(Punch.class));
        }
    }

    @Test
    void punchIn_AlreadyPunchedIn() {
        // Create a punch with null checkout (employee has punched in but not out)
        Punch existingPunch = new Punch();
        existingPunch.setEmpId(testEmpId);
        existingPunch.setDate(today);
        existingPunch.setCheckin(LocalDateTime.now().minusHours(2));
        
        List<Punch> punches = new ArrayList<>();
        punches.add(existingPunch);
        
        // Mock the repository to return the existing punch
        when(punchRepository.findByEmpIdAndDate(eq(testEmpId), eq(today)))
            .thenReturn(punches);
        
        try (MockedStatic<TokenExtractor> tokenExtractor = Mockito.mockStatic(TokenExtractor.class)) {
            tokenExtractor.when(TokenExtractor::getEmpId).thenReturn(testEmpId);
            
            // Execute and verify exception is thrown
            assertThrows(IllegalStateException.class, () -> attendanceService.punchIn());
            
            // Verify repository was not called to save
            verify(punchRepository, never()).save(any(Punch.class));
        }
    }

    @Test
    void punchOut_Success() {
        // Create a punch with null checkout (employee has punched in but not out)
        Punch existingPunch = new Punch();
        existingPunch.setEmpId(testEmpId);
        existingPunch.setDate(today);
        existingPunch.setCheckin(LocalDateTime.now().minusHours(2));
        
        List<Punch> punches = new ArrayList<>();
        punches.add(existingPunch);
        
        // Mock the repository to return the existing punch
        when(punchRepository.findByEmpIdAndDate(eq(testEmpId), eq(today)))
            .thenReturn(punches);
        
        try (MockedStatic<TokenExtractor> tokenExtractor = Mockito.mockStatic(TokenExtractor.class)) {
            tokenExtractor.when(TokenExtractor::getEmpId).thenReturn(testEmpId);
            
            // Execute the method
            attendanceService.punchOut();
            
            // Verify repository was called to save
            verify(punchRepository, times(1)).save(any(Punch.class));
        }
    }

    @Test
    void punchOut_NotPunchedIn() {
        // Mock empty list of punches (employee hasn't punched in today)
        when(punchRepository.findByEmpIdAndDate(eq(testEmpId), eq(today)))
            .thenReturn(new ArrayList<>());
        
        try (MockedStatic<TokenExtractor> tokenExtractor = Mockito.mockStatic(TokenExtractor.class)) {
            tokenExtractor.when(TokenExtractor::getEmpId).thenReturn(testEmpId);
            
            // Execute and verify exception is thrown
            assertThrows(IllegalStateException.class, () -> attendanceService.punchOut());
            
            // Verify repository was not called to save
            verify(punchRepository, never()).save(any(Punch.class));
        }
    }
}
