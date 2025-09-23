package com.example.demo.controller;

import com.example.demo.entity.Feedback;
import com.example.demo.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/feedbacks") // endpoint accessible aux clients pour créer un feedback
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    // === Gestionnaire ===

    @GetMapping("/gestionnaire")
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @GetMapping("/gestionnaire/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
        Optional<Feedback> feedback = feedbackRepository.findById(id);
        return feedback.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/gestionnaire/{id}/reponse")
    public ResponseEntity<Feedback> respondToFeedback(@PathVariable Long id, @RequestBody String reponse) {
        Optional<Feedback> feedbackOptional = feedbackRepository.findById(id);
        if (!feedbackOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Feedback feedback = feedbackOptional.get();
        feedback.setReponse(reponse);
        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return ResponseEntity.ok(updatedFeedback);
    }

    @DeleteMapping("/gestionnaire/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        if (!feedbackRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        feedbackRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // === Client ===

    @PostMapping
    public Feedback createFeedback(@RequestBody Feedback feedback) {
        return feedbackRepository.save(feedback);
    }
}

