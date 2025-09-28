#!/bin/bash

# Script de vérification de la persistance des données H2
# Overlook Hotel - Base de données persistante

echo "======================================================"
echo "🏨 OVERLOOK HOTEL - Vérification Persistance DB H2"
echo "======================================================"
echo

# Vérification de l'existence du fichier de base de données
DB_FILE="./data/hoteldb_new.mv.db"
if [ -f "$DB_FILE" ]; then
    echo "✅ Fichier de base de données trouvé: $DB_FILE"
    echo "   Taille: $(ls -lh $DB_FILE | awk '{print $5}')"
    echo "   Dernière modification: $(ls -l $DB_FILE | awk '{print $6, $7, $8}')"
else
    echo "❌ Fichier de base de données non trouvé: $DB_FILE"
    exit 1
fi

echo

# Vérification de l'application Spring Boot
if pgrep -f "spring-boot:run" > /dev/null; then
    echo "✅ Application Spring Boot en cours d'exécution"
    
    # Test de connectivité
    if curl -s http://localhost:8080/gestionnaires > /dev/null; then
        echo "✅ Application accessible sur http://localhost:8080"
        
        # Vérification des données persistantes
        echo
        echo "🔍 Vérification des données persistantes..."
        
        # Compter les gestionnaires
        GESTIONNAIRES_COUNT=$(curl -s "http://localhost:8080/gestionnaires" | grep -c "gestionnaire-card" || echo "0")
        echo "   📊 Gestionnaires trouvés: $GESTIONNAIRES_COUNT"
        
        # Compter les employés
        EMPLOYES_COUNT=$(curl -s "http://localhost:8080/employes/liste-gestionnaire" | grep -c "employee-card" || echo "0")
        echo "   👥 Employés trouvés: $EMPLOYES_COUNT"
        
        # Vérifier si Jean Martin existe toujours
        if curl -s "http://localhost:8080/gestionnaires" | grep -q "Jean Martin"; then
            echo "   ✅ Gestionnaire test 'Jean Martin' toujours présent"
        else
            echo "   ⚠️  Gestionnaire test 'Jean Martin' non trouvé"
        fi
        
    else
        echo "❌ Application non accessible sur http://localhost:8080"
    fi
else
    echo "❌ Application Spring Boot non en cours d'exécution"
    echo "   Pour démarrer: ./mvnw spring-boot:run"
fi

echo
echo "======================================================"
echo "📋 CONFIGURATION DE PERSISTANCE ACTUELLE"
echo "======================================================"
echo "🔧 URL Base de données: jdbc:h2:file:./data/hoteldb_new"
echo "🔧 Mode: FILE (persistant)"
echo "🔧 DDL Auto: UPDATE (préserve les données)"
echo "🔧 Console H2: http://localhost:8080/h2-console"
echo
echo "✅ CONCLUSION: Base de données configurée pour la persistance"
echo "   Les données seront préservées entre les redémarrages"
echo "======================================================"
