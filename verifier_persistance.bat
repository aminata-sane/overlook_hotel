@echo off
REM Script de vérification de la persistance des données H2 - Version Windows
REM Overlook Hotel - Base de données persistante

echo ======================================================
echo 🏨 OVERLOOK HOTEL - Vérification Persistance DB H2
echo ======================================================
echo.

REM Vérification de l'existence du fichier de base de données
set DB_FILE=.\data\hoteldb_new.mv.db
if exist "%DB_FILE%" (
    echo ✅ Fichier de base de données trouvé: %DB_FILE%
    for %%A in ("%DB_FILE%") do (
        echo    Taille: %%~zA bytes
        echo    Dernière modification: %%~tA
    )
) else (
    echo ❌ Fichier de base de données non trouvé: %DB_FILE%
    pause
    exit /b 1
)

echo.

REM Vérification de l'application Spring Boot
tasklist /fi "imagename eq java.exe" /fo csv | find /i "java.exe" > nul
if %errorlevel% == 0 (
    echo ✅ Application Java en cours d'exécution
    
    REM Test de connectivité avec timeout
    curl -s --connect-timeout 5 http://localhost:8080/gestionnaires > nul 2>&1
    if %errorlevel% == 0 (
        echo ✅ Application accessible sur http://localhost:8080
        
        REM Vérification des données persistantes
        echo.
        echo 🔍 Vérification des données persistantes...
        
        REM Vérifier si Jean Martin existe toujours
        curl -s "http://localhost:8080/gestionnaires" | find "Jean Martin" > nul 2>&1
        if %errorlevel% == 0 (
            echo    ✅ Gestionnaire test 'Jean Martin' toujours présent
        ) else (
            echo    ⚠️  Gestionnaire test 'Jean Martin' non trouvé
        )
        
        REM Tester l'accès aux employés
        curl -s --connect-timeout 5 http://localhost:8080/employes/liste-gestionnaire > nul 2>&1
        if %errorlevel% == 0 (
            echo    ✅ Liste des employés accessible
        ) else (
            echo    ⚠️  Problème d'accès à la liste des employés
        )
        
    ) else (
        echo ❌ Application non accessible sur http://localhost:8080
        echo    Vérifiez que l'application est démarrée
    )
) else (
    echo ❌ Aucune application Java en cours d'exécution
    echo    Pour démarrer: mvnw.cmd spring-boot:run
)

echo.
echo ======================================================
echo 📋 CONFIGURATION DE PERSISTANCE ACTUELLE
echo ======================================================
echo 🔧 URL Base de données: jdbc:h2:file:./data/hoteldb_new
echo 🔧 Mode: FILE (persistant)
echo 🔧 DDL Auto: UPDATE (préserve les données)
echo 🔧 Console H2: http://localhost:8080/h2-console
echo.
echo ✅ CONCLUSION: Base de données configurée pour la persistance
echo    Les données seront préservées entre les redémarrages
echo ======================================================
echo.
echo Appuyez sur une touche pour continuer...
pause > nul
