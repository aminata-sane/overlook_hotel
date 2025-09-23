package com.example.demo.repository;

import com.example.demo.entity.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmployeRepository extends JpaRepository<Employe, Long> {
    Optional<Employe> findByEmail(String email);
}

