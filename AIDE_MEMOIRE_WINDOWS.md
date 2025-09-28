# ⚡ AIDE-MÉMOIRE WINDOWS - Présentation Rapide

## 🚀 **DÉMARRAGE RAPIDE WINDOWS**

```cmd
# 1. Aller dans le dossier du projet
cd overlook_hotel\demo

# 2. Lancer l'application
mvnw.cmd spring-boot:run

# 3. Attendre 30-60 secondes
# 4. Ouvrir: http://localhost:8080
```

## 📋 **URLs ESSENTIELLES**

| Page | URL |
|------|-----|
| 🏠 Accueil | http://localhost:8080 |
| 👥 Employés | http://localhost:8080/employes/liste-gestionnaire |
| 👔 Gestionnaires | http://localhost:8080/gestionnaires |
| ➕ Nouveau gestionnaire | http://localhost:8080/gestionnaires/nouveau |
| 🗃️ Base de données | http://localhost:8080/h2-console |

## 🎯 **DÉMONSTRATION (5 minutes)**

1. **Interface moderne** → Montrer la liste des employés
2. **Responsive design** → Redimensionner la fenêtre
3. **Formulaire validé** → Créer un gestionnaire
4. **Persistance** → Redémarrer et vérifier les données

## 🔧 **COMMANDES WINDOWS**

```cmd
# Démarrer
mvnw.cmd spring-boot:run

# Vérifier persistance
verifier_persistance.bat

# Guide complet
demo_presentation.bat

# Arrêter (dans la fenêtre du serveur)
Ctrl+C
```

## ⚠️ **EN CAS DE PROBLÈME**

```cmd
# Java non trouvé
java -version

# Port occupé
taskkill /f /im java.exe

# Redémarrer proprement
Ctrl+C puis mvnw.cmd spring-boot:run
```

## 🎨 **POINTS CLÉS À EXPLIQUER**

- ✨ **Design glassmorphism** (effet verre)
- 📱 **Responsive** (s'adapte aux écrans)
- ✅ **Validation temps réel** (formulaires)
- 💾 **Persistance** (données sauvées)
- 🏗️ **Architecture MVC** (Spring Boot)

## 🗃️ **BASE DE DONNÉES H2**

**Console:** http://localhost:8080/h2-console
- **JDBC URL:** `jdbc:h2:file:./data/hoteldb_new`
- **Username:** `sa`
- **Password:** *(vide)*

## ✅ **TOUT FONCTIONNE SUR WINDOWS !**

**Temps total:** 5 minutes de préparation + démonstration fluide 🚀
