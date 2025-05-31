package com.bjet.ems.util;

/**
 * Utility class for extracting employee ID from security context.
 * Currently returns a dummy ID for development purposes.
 */
public class TokenExtractor {
    
    /**
     * Returns the employee ID from the current security context.
     * Currently returns a dummy ID (123L) for development purposes.
     * 
     * @return Long employee ID
     */
    public static Long getEmpId() {
        // Dummy implementation until security is integrated
        return 123L;
    }
}
