@echo off
REM Script de démonstration Overlook Hotel - Version Windows
REM Guide pour présentation sur Windows

echo ======================================================
echo 🚀 OVERLOOK HOTEL - Script de Démonstration Windows
echo ======================================================
echo.

REM Vérifier Java
echo 🔍 Vérification de l'environnement...
java -version > nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java n'est pas installé ou pas dans le PATH
    echo    Téléchargez Java 17+ depuis: https://adoptium.net/
    echo    Ou utilisez: winget install EclipseAdoptium.Temurin.17.JDK
    pause
    exit /b 1
) else (
    echo ✅ Java détecté
)

REM Vérifier curl (disponible dans Windows 10+)
curl --version > nul 2>&1
if %errorlevel% neq 0 (
    echo ⚠️  curl non disponible, certains tests seront ignorés
    set CURL_AVAILABLE=false
) else (
    echo ✅ curl disponible
    set CURL_AVAILABLE=true
)

echo.
echo ======================================================
echo 📋 ÉTAPES DE DÉMONSTRATION
echo ======================================================

echo.
echo 1️⃣ DÉMARRAGE DE L'APPLICATION
echo ======================================================
echo.
echo 🔧 Commande à exécuter:
echo    mvnw.cmd spring-boot:run
echo.
echo 📝 Ce qui va se passer:
echo    - Compilation du projet Java
echo    - Démarrage du serveur Tomcat sur le port 8080
echo    - Initialisation de la base de données H2
echo    - Application prête en 30-60 secondes
echo.
set /p choice="Voulez-vous démarrer l'application maintenant? (O/N): "
if /i "%choice%" == "O" (
    echo.
    echo 🚀 Démarrage de l'application...
    echo ⏳ Cela peut prendre 1-2 minutes...
    echo.
    start "Overlook Hotel" cmd /k "mvnw.cmd spring-boot:run"
    
    echo ⏱️  Attente du démarrage (30 secondes)...
    timeout /t 30 /nobreak > nul
    
    if "%CURL_AVAILABLE%" == "true" (
        echo 🔍 Test de connectivité...
        curl -s --connect-timeout 10 http://localhost:8080 > nul 2>&1
        if %errorlevel% == 0 (
            echo ✅ Application démarrée avec succès!
        ) else (
            echo ⚠️  Application en cours de démarrage, patientez...
        )
    )
) else (
    echo ⏭️  Démarrage manuel requis plus tard
)

echo.
echo 2️⃣ PAGES À MONTRER DANS LA DÉMONSTRATION
echo ======================================================
echo.
echo 🏠 Page d'accueil:
echo    http://localhost:8080
echo.
echo 👥 Liste des employés (interface moderne):
echo    http://localhost:8080/employes/liste-gestionnaire
echo.
echo 👔 Liste des gestionnaires:
echo    http://localhost:8080/gestionnaires
echo.
echo ➕ Créer un gestionnaire (nouveau formulaire):
echo    http://localhost:8080/gestionnaires/nouveau
echo.
echo 🏨 Dashboard gestionnaire:
echo    http://localhost:8080/dashboard-gestionnaire
echo.
echo 🗃️  Console base de données (H2):
echo    http://localhost:8080/h2-console
echo    JDBC URL: jdbc:h2:file:./data/hoteldb_new
echo    Username: sa
echo    Password: (laisser vide)

echo.
echo 3️⃣ POINTS CLÉS À EXPLIQUER
echo ======================================================
echo 🎨 Interface moderne avec glassmorphism
echo 📱 Design responsive (redimensionner la fenêtre)
echo ✅ Validation en temps réel des formulaires
echo 💾 Persistance des données (redémarrer l'app)
echo 🔧 Architecture MVC avec Spring Boot

echo.
echo 4️⃣ TESTS DE PERSISTANCE
echo ======================================================
if exist "verifier_persistance.bat" (
    echo 📊 Script de vérification disponible:
    echo    verifier_persistance.bat
    echo.
    set /p choice2="Voulez-vous exécuter la vérification maintenant? (O/N): "
    if /i "%choice2%" == "O" (
        call verifier_persistance.bat
    )
) else (
    echo ⚠️  Script verifier_persistance.bat non trouvé
)

echo.
echo 5️⃣ ARRÊT DE L'APPLICATION
echo ======================================================
echo 🛑 Pour arrêter l'application:
echo    - Aller dans la fenêtre du serveur
echo    - Appuyer sur Ctrl+C
echo    - Confirmer avec 'O' si demandé

echo.
echo ======================================================
echo 🎯 CHECKLIST POUR LA PRÉSENTATION
echo ======================================================
echo ✅ Java 17+ installé
if "%CURL_AVAILABLE%" == "true" (
    echo ✅ curl disponible pour les tests
) else (
    echo ⚠️  curl non disponible ^(tests manuels nécessaires^)
)
echo ✅ Application démarrable avec mvnw.cmd
echo ✅ Base de données persistante configurée
echo ✅ Scripts de démonstration prêts

echo.
echo 📋 URLS À MARQUER:
echo - http://localhost:8080 ^(accueil^)
echo - http://localhost:8080/employes/liste-gestionnaire ^(employés^)
echo - http://localhost:8080/gestionnaires/nouveau ^(créer gestionnaire^)
echo - http://localhost:8080/h2-console ^(base de données^)

echo.
echo 💡 CONSEIL: Gardez cette fenêtre ouverte pendant la présentation
echo    pour référence rapide des URLs et commandes
echo.
echo ======================================================
echo 🚀 BONNE CHANCE POUR LA PRÉSENTATION!
echo ======================================================
echo.
pause
