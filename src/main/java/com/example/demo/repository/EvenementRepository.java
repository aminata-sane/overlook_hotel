package com.example.demo.repository;

import com.example.demo.entity.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {
}

