#!/bin/bash

# Script de test pour la connexion employé
echo "=== Test de connexion employé - Overlook Hotel ==="

# Test 1: Accès à la page de connexion
echo "1. Test d'accès à la page de connexion..."
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/employes/connexion
echo ""

# Test 2: Tentative de connexion avec des identifiants valides
echo "2. Test de connexion avec Sophie Martin (Réceptionniste)..."
curl -X POST http://localhost:8080/employes/connexion \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "email=sophie.martin@overlook-hotel.com&motDePasse=password123" \
  -c cookies_employe.txt -L -s -o response.html -w "%{http_code}"
echo ""

# Test 3: Vérifier la redirection vers le dashboard
echo "3. Vérification de la redirection vers le dashboard..."
curl -b cookies_employe.txt http://localhost:8080/employes/dashboard -s -o dashboard.html -w "%{http_code}"
echo ""

# Test 4: Test avec des identifiants incorrects
echo "4. Test avec des identifiants incorrects..."
curl -X POST http://localhost:8080/employes/connexion \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "email=test@test.com&motDePasse=wrongpassword" \
  -L -s -o /dev/null -w "%{http_code}"
echo ""

echo "Test terminé!"
echo "Vous pouvez maintenant tester manuellement avec :"
echo "- Email: sophie.martin@overlook-hotel.com"
echo "- Mot de passe: password123"
echo ""
echo "Autres comptes disponibles :"
echo "- pierre.dubois@overlook-hotel.com (Maintenance)"
echo "- marie.lemaire@overlook-hotel.com (Femme de ménage)"
echo "- jean.moreau@overlook-hotel.com (Gestionnaire)"
