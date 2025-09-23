package com.example.demo.repository;

import com.example.demo.entity.Feedback;
import com.example.demo.entity.Gestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // Liste des feedbacks auxquels un gestionnaire peut répondre
    List<Feedback> findByGestionnaire(Gestionnaire gestionnaire);
}

