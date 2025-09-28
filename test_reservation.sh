#!/bin/bash

# Script de test pour la réservation
echo "=== Test de réservation - Overlook Hotel ==="

# 1. Créer un compte client de test
echo "1. Création d'un compte client de test..."
curl -X POST http://localhost:8080/inscription \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "nom=Testeur&prenom=Jean&email=jean.testeur@test.com&telephone=0123456789&motDePasse=test123" \
  -c cookies.txt -L -v

echo -e "\n\n2. Connexion avec le compte créé..."
curl -X POST http://localhost:8080/connexion \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "email=jean.testeur@test.com&motDePasse=test123" \
  -b cookies.txt -c cookies.txt -L -v

echo -e "\n\n3. Test de l'API panier count..."
curl -X GET http://localhost:8080/panier/count \
  -H "X-Requested-With: XMLHttpRequest" \
  -b cookies.txt -v

echo -e "\n\n4. Test de réservation (ajout au panier)..."
curl -X POST http://localhost:8080/reservation/ajouter-panier \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -H "X-Requested-With: XMLHttpRequest" \
  -d "chambreId=1&dateArrivee=2025-09-29&dateDepart=2025-09-30&nombreAdultes=2&nombreEnfants=0&commentaires=Test+de+reservation" \
  -b cookies.txt -v

echo -e "\n\n5. Vérification du panier après ajout..."
curl -X GET http://localhost:8080/panier/count \
  -H "X-Requested-With: XMLHttpRequest" \
  -b cookies.txt -v

echo -e "\n\nTest terminé!"
