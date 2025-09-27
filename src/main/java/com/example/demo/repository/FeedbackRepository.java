package com.example.demo.repository;

import com.example.demo.model.Feedback;
import com.example.demo.model.Client;
import com.example.demo.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Trouver tous les feedbacks par statut
    List<Feedback> findByStatut(Feedback.StatutFeedback statut);

    // Trouver tous les feedbacks par type
    List<Feedback> findByType(Feedback.TypeFeedback type);

    // Trouver tous les feedbacks d'un client
    List<Feedback> findByClientOrderByDateCreationDesc(Client client);

    // Trouver tous les feedbacks assignés à un employé
    List<Feedback> findByEmployeOrderByDateCreationDesc(Employe employe);

    // Trouver les feedbacks par note (rating)
    List<Feedback> findByNote(Integer note);

    // Trouver les feedbacks avec une note supérieure ou égale à X
    List<Feedback> findByNoteGreaterThanEqual(Integer noteMin);

    // Trouver les feedbacks avec une note inférieure ou égale à X
    List<Feedback> findByNoteLessThanEqual(Integer noteMax);

    // Trouver les feedbacks non résolus
    @Query("SELECT f FROM Feedback f WHERE f.statut != 'RESOLU' AND f.statut != 'FERME'")
    List<Feedback> findFeedbacksNonResolus();

    // Trouver les feedbacks avec réponse
    @Query("SELECT f FROM Feedback f WHERE f.reponse IS NOT NULL AND f.reponse != ''")
    List<Feedback> findFeedbacksAvecReponse();

    // Trouver les feedbacks sans réponse
    @Query("SELECT f FROM Feedback f WHERE f.reponse IS NULL OR f.reponse = ''")
    List<Feedback> findFeedbacksSansReponse();

    // Trouver les feedbacks par période
    @Query("SELECT f FROM Feedback f WHERE f.dateCreation BETWEEN :dateDebut AND :dateFin ORDER BY f.dateCreation DESC")
    List<Feedback> findByDateCreationBetween(@Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin);

    // Rechercher dans les commentaires
    @Query("SELECT f FROM Feedback f WHERE LOWER(f.commentaire) LIKE LOWER(CONCAT('%', :terme, '%')) OR LOWER(f.reponse) LIKE LOWER(CONCAT('%', :terme, '%'))")
    List<Feedback> rechercherFeedbacks(@Param("terme") String terme);

    // Statistiques - Compter par statut
    long countByStatut(Feedback.StatutFeedback statut);

    // Statistiques - Compter par type
    long countByType(Feedback.TypeFeedback type);

    // Statistiques - Compter par note
    long countByNote(Integer note);

    // Statistiques - Note moyenne
    @Query("SELECT AVG(f.note) FROM Feedback f")
    Double getNoteMoyenne();

    // Statistiques - Note moyenne par type
    @Query("SELECT AVG(f.note) FROM Feedback f WHERE f.type = :type")
    Double getNoteMoyenneParType(@Param("type") Feedback.TypeFeedback type);

    // Trouver les feedbacks récents (derniers X jours)
    @Query("SELECT f FROM Feedback f WHERE f.dateCreation >= :dateLimit ORDER BY f.dateCreation DESC")
    List<Feedback> findFeedbacksRecents(@Param("dateLimit") LocalDateTime dateLimit);

    // Trouver les feedbacks d'un client par type
    List<Feedback> findByClientAndType(Client client, Feedback.TypeFeedback type);

    // Trouver les feedbacks les plus récents (limite)
    @Query("SELECT f FROM Feedback f ORDER BY f.dateCreation DESC")
    List<Feedback> findTopByOrderByDateCreationDesc();
}
