package com.bjet.ems.repository;

import com.bjet.ems.model.Punch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PunchRepository extends JpaRepository<Punch, Long> {
    List<Punch> findByEmpIdAndDate(Long empId, LocalDate date);
}
