# 🏨 Guide de Démonstration - Overlook Hotel

## 🚀 Démarrage de l'Application

### Commandes à exécuter :
```bash
cd /Users/mameaminataconstancesane/Desktop/overlook_hotel/demo
mvn spring-boot:run
```

### ✅ Confirmation de démarrage :
Attendre ce message : `Tomcat started on port 8080 (http) with context path '/'`

---

## 🎯 Parcours de Démonstration Recommandé

### 1️⃣ **Introduction** (2 min)
- URL : `http://localhost:8080/`
- Présenter l'accueil moderne et responsive
- Montrer la navigation intuitive

### 2️⃣ **Dashboard Gestionnaire** (5 min)
- URL : `http://localhost:8080/dashboard-gestionnaire`
- ✨ **Points clés à montrer :**
  - Design moderne avec cartes métriques
  - Boutons d'actions avec icônes
  - Section "Actions Administrateur"
  - Bouton "Console Développeur" (nouveauté)

### 3️⃣ **Statistiques Avancées** (3 min)
- URL : `http://localhost:8080/gestionnaires/statistiques`
- ✨ **Points clés :**
  - Métriques en temps réel
  - Graphiques et visualisations
  - Design professionnel et responsive

### 4️⃣ **Module Financier** (4 min)
- URL : `http://localhost:8080/finances`
- ✨ **Fonctionnalités à présenter :**
  - Dashboard financier avec métriques
  - Graphiques de revenus
  - Actions rapides (Nouveau Rapport, Budget)
  - Navigation vers rapports et budget

### 5️⃣ **Gestion des Employés** (3 min)
- URL : `http://localhost:8080/gestionnaires/employes/nouveau`
- ✨ **Démontrer :**
  - Formulaire de création d'employé
  - Validation des champs
  - Interface utilisateur moderne

### 6️⃣ **Espace Employé** (2 min)
- URL : `http://localhost:8080/dashboard-employe`
- ✨ **Montrer :**
  - Dashboard spécifique aux employés
  - Fonctionnalités de réservation
  - Interface adaptée au rôle

### 7️⃣ **Outils Développeur** (2 min)
- URL : `http://localhost:8080/dev/console`
- ✨ **Démontrer :**
  - Page console développeur stylisée
  - Informations de connexion H2
  - Accès direct à la base de données

---

## 💡 **Points Forts à Mentionner**

### 🎨 **Aspects Techniques :**
- Architecture Spring Boot moderne
- Base de données H2 intégrée
- Templates Thymeleaf responsive
- Contrôleurs séparés par domaine métier

### 🚀 **Fonctionnalités Métier :**
- Gestion complète des chambres et réservations
- Module financier avec métriques
- Système de gestion des employés
- Outils de développement intégrés

### 🎯 **Expérience Utilisateur :**
- Design moderne et professionnel
- Navigation intuitive
- Responsive design (mobile-friendly)
- Interfaces spécialisées par rôle

---

## 🔍 **Base de Données - Requêtes de Démonstration**

### Pour accéder à H2 Console :
- URL : `http://localhost:8080/h2-console`
- JDBC URL : `jdbc:h2:file:./data/hoteldb`
- Username : `sa`
- Password : (laisser vide)

### Requêtes utiles pour la démo :
```sql
-- Voir tous les clients
SELECT * FROM clients;

-- Voir toutes les chambres
SELECT * FROM chambres;

-- Voir toutes les réservations
SELECT * FROM reservations;

-- Voir tous les employés
SELECT * FROM employes;

-- Statistiques rapides
SELECT COUNT(*) as total_chambres FROM chambres;
SELECT COUNT(*) as total_clients FROM clients;
```

---

## ⚠️ **Checklist Avant Démonstration**

- [ ] Application démarrée (`mvn spring-boot:run`)
- [ ] Navigateur ouvert sur `http://localhost:8080`
- [ ] Tous les liens testés et fonctionnels
- [ ] Base de données H2 accessible
- [ ] Présentation préparée (15-20 minutes)

---

## 🎯 **Conclusion Recommandée**

**Points à souligner :**
1. **Système complet** de gestion hôtelière
2. **Architecture moderne** et scalable
3. **Interfaces utilisateur** professionnelles
4. **Séparation des rôles** (clients, employés, gestionnaires)
5. **Outils de développement** intégrés
6. **Prêt pour production** avec améliorations futures

---

**🚀 Bonne démonstration ! 🏨**
