package com.example.demo.service;

import com.example.demo.model.Feedback;
import com.example.demo.model.Client;
import com.example.demo.model.Employe;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmployeRepository employeRepository;

    // CRUD de base
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    public Feedback createFeedback(Feedback feedback) {
        // Validation de la note
        if (feedback.getNote() == null || feedback.getNote() < 1 || feedback.getNote() > 5) {
            throw new RuntimeException("La note doit être comprise entre 1 et 5");
        }

        // Validation du commentaire
        if (feedback.getCommentaire() == null || feedback.getCommentaire().trim().isEmpty()) {
            throw new RuntimeException("Le commentaire ne peut pas être vide");
        }

        // Définir la date de création si non définie
        if (feedback.getDateCreation() == null) {
            feedback.setDateCreation(LocalDateTime.now());
        }

        // Définir le statut par défaut
        if (feedback.getStatut() == null) {
            feedback.setStatut(Feedback.StatutFeedback.EN_ATTENTE);
        }

        // Définir le type par défaut
        if (feedback.getType() == null) {
            feedback.setType(Feedback.TypeFeedback.GENERAL);
        }

        return feedbackRepository.save(feedback);
    }

    public Feedback updateFeedback(Long id, Feedback feedbackDetails) {
        return feedbackRepository.findById(id)
                .map(feedback -> {
                    feedback.setCommentaire(feedbackDetails.getCommentaire());
                    feedback.setNote(feedbackDetails.getNote());
                    feedback.setType(feedbackDetails.getType());
                    feedback.setStatut(feedbackDetails.getStatut());
                    
                    return feedbackRepository.save(feedback);
                })
                .orElseThrow(() -> new RuntimeException("Feedback non trouvé avec l'ID : " + id));
    }

    public void deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new RuntimeException("Feedback non trouvé avec l'ID : " + id);
        }
        feedbackRepository.deleteById(id);
    }

    // Gestion des réponses
    public Feedback ajouterReponse(Long feedbackId, String reponse, Long employeId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback non trouvé"));

        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));

        feedback.setReponse(reponse);
        feedback.setEmploye(employe);
        feedback.setStatut(Feedback.StatutFeedback.EN_COURS);

        return feedbackRepository.save(feedback);
    }

    public Feedback marquerCommeResolu(Long id) {
        return feedbackRepository.findById(id)
                .map(feedback -> {
                    feedback.setStatut(Feedback.StatutFeedback.RESOLU);
                    return feedbackRepository.save(feedback);
                })
                .orElseThrow(() -> new RuntimeException("Feedback non trouvé avec l'ID : " + id));
    }

    // Méthodes de recherche et filtrage
    public List<Feedback> getFeedbacksByStatut(Feedback.StatutFeedback statut) {
        return feedbackRepository.findByStatut(statut);
    }

    public List<Feedback> getFeedbacksByType(Feedback.TypeFeedback type) {
        return feedbackRepository.findByType(type);
    }

    public List<Feedback> getFeedbacksByClient(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        return feedbackRepository.findByClientOrderByDateCreationDesc(client);
    }

    public List<Feedback> getFeedbacksByEmploye(Long employeId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        return feedbackRepository.findByEmployeOrderByDateCreationDesc(employe);
    }

    public List<Feedback> getFeedbacksByNote(Integer note) {
        return feedbackRepository.findByNote(note);
    }

    public List<Feedback> getFeedbacksNonResolus() {
        return feedbackRepository.findFeedbacksNonResolus();
    }

    public List<Feedback> getFeedbacksSansReponse() {
        return feedbackRepository.findFeedbacksSansReponse();
    }

    public List<Feedback> rechercherFeedbacks(String terme) {
        return feedbackRepository.rechercherFeedbacks(terme);
    }

    public List<Feedback> getFeedbacksRecents(int jours) {
        LocalDateTime dateLimit = LocalDateTime.now().minusDays(jours);
        return feedbackRepository.findFeedbacksRecents(dateLimit);
    }

    // Gestion du statut
    public Feedback changerStatut(Long id, Feedback.StatutFeedback nouveauStatut) {
        return feedbackRepository.findById(id)
                .map(feedback -> {
                    feedback.setStatut(nouveauStatut);
                    return feedbackRepository.save(feedback);
                })
                .orElseThrow(() -> new RuntimeException("Feedback non trouvé avec l'ID : " + id));
    }

    // Statistiques
    public long getNombreFeedbacks() {
        return feedbackRepository.count();
    }

    public long getNombreFeedbacksParStatut(Feedback.StatutFeedback statut) {
        return feedbackRepository.countByStatut(statut);
    }

    public long getNombreFeedbacksParType(Feedback.TypeFeedback type) {
        return feedbackRepository.countByType(type);
    }

    public long getNombreFeedbacksParNote(Integer note) {
        return feedbackRepository.countByNote(note);
    }

    public Double getNoteMoyenne() {
        Double moyenne = feedbackRepository.getNoteMoyenne();
        return moyenne != null ? moyenne : 0.0;
    }

    public Double getNoteMoyenneParType(Feedback.TypeFeedback type) {
        Double moyenne = feedbackRepository.getNoteMoyenneParType(type);
        return moyenne != null ? moyenne : 0.0;
    }

    // Méthodes d'analyse
    public List<Feedback> getFeedbacksPositifs() {
        return feedbackRepository.findByNoteGreaterThanEqual(4);
    }

    public List<Feedback> getFeedbacksNegatifs() {
        return feedbackRepository.findByNoteLessThanEqual(2);
    }

    public double getPourcentageSatisfaction() {
        long total = getNombreFeedbacks();
        if (total == 0) return 0.0;
        
        long positifs = feedbackRepository.findByNoteGreaterThanEqual(4).size();
        return (double) positifs / total * 100;
    }

    public String getClassificationSatisfaction() {
        double pourcentage = getPourcentageSatisfaction();
        
        if (pourcentage >= 90) return "Excellent";
        if (pourcentage >= 80) return "Très bien";
        if (pourcentage >= 70) return "Bien";
        if (pourcentage >= 60) return "Acceptable";
        if (pourcentage >= 50) return "Moyen";
        return "Médiocre";
    }
}
