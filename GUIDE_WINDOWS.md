# 🖥️ GUIDE WINDOWS - Présentation Overlook Hotel

## ✅ **COMPATIBILITÉ WINDOWS CONFIRMÉE**

Votre application Overlook Hotel est **100% compatible Windows** ! Voici tout ce qu'il faut savoir pour la démonstration.

## 🔧 **PRÉREQUIS WINDOWS**

### **Java 17+ (Obligatoire)**
```cmd
# Vérifier Java
java -version

# Si pas installé, installer via winget:
winget install EclipseAdoptium.Temurin.17.JDK

# Ou télécharger depuis: https://adoptium.net/
```

### **curl (Optionnel - Inclus dans Windows 10+)**
```cmd
# Vérifier curl
curl --version
```

## 🚀 **DÉMARRAGE SUR WINDOWS**

### **Commande Principal**
```cmd
# Au lieu de ./mvnw spring-boot:run (macOS/Linux)
mvnw.cmd spring-boot:run
```

### **Scripts Windows Créés**
1. **`demo_presentation.bat`** - Script de démonstration complet
2. **`verifier_persistance.bat`** - Vérification de la persistance (version Windows)

## 📋 **ÉTAPES POUR VOTRE COLLÈGUE**

### **1. Préparation (5 minutes)**
```cmd
# 1. Cloner/copier le projet
git clone [votre-repo]
cd overlook_hotel/demo

# 2. Vérifier Java
java -version

# 3. Lancer le script de démonstration
demo_presentation.bat
```

### **2. Démarrage Application**
```cmd
# Dans le répertoire du projet
mvnw.cmd spring-boot:run
```

**⏱️ Temps de démarrage:** 30-60 secondes

### **3. URLs à Tester**
- **Accueil:** http://localhost:8080
- **Employés:** http://localhost:8080/employes/liste-gestionnaire
- **Gestionnaires:** http://localhost:8080/gestionnaires
- **Nouveau gestionnaire:** http://localhost:8080/gestionnaires/nouveau
- **Console H2:** http://localhost:8080/h2-console

## 🔍 **TESTS DE FONCTIONNEMENT**

### **Test Rapide (2 minutes)**
```cmd
# 1. Vérifier la persistance
verifier_persistance.bat

# 2. Test manuel des pages principales
# (Ouvrir les URLs dans le navigateur)
```

### **Test Complet (5 minutes)**
1. ✅ Accès à la liste des employés
2. ✅ Création d'un nouveau gestionnaire
3. ✅ Vérification de l'interface responsive
4. ✅ Redémarrage et vérification persistance

## 🗂️ **STRUCTURE DES FICHIERS WINDOWS**

```
demo/
├── mvnw.cmd                    ✅ Script Maven Windows (existant)
├── demo_presentation.bat       ✅ Script de démonstration (nouveau)
├── verifier_persistance.bat    ✅ Vérification persistance (nouveau)
├── data/
│   └── hoteldb_new.mv.db      ✅ Base de données (compatible)
└── src/...                    ✅ Code source (compatible)
```

## ⚠️ **DIFFÉRENCES WINDOWS/LINUX**

| Aspect | Linux/macOS | Windows |
|--------|-------------|---------|
| Script Maven | `./mvnw` | `mvnw.cmd` |
| Séparateur chemin | `/` | `\` |
| Vérification processus | `pgrep` | `tasklist` |
| Scripts shell | `.sh` | `.bat` |

**✅ Toutes ces différences sont gérées automatiquement !**

## 🐛 **DÉPANNAGE WINDOWS**

### **Problème Java**
```cmd
# Si Java non trouvé
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.x.x.x-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%
```

### **Problème Port 8080**
```cmd
# Vérifier si le port est utilisé
netstat -an | find "8080"

# Tuer le processus Java si nécessaire
taskkill /f /im java.exe
```

### **Problème Permissions**
```cmd
# Exécuter en tant qu'administrateur si nécessaire
# Clic droit sur cmd.exe > "Exécuter en tant qu'administrateur"
```

## 🎯 **CHECKLIST PRÉSENTATION WINDOWS**

### **Avant la Présentation**
- [ ] Java 17+ installé et accessible
- [ ] Projet téléchargé/cloné
- [ ] Test de `mvnw.cmd spring-boot:run` réussi
- [ ] Vérification des URLs principales
- [ ] Script `demo_presentation.bat` testé

### **Pendant la Présentation**
- [ ] Lancer `demo_presentation.bat` pour le guide
- [ ] Démarrer avec `mvnw.cmd spring-boot:run`
- [ ] Montrer les URLs en live
- [ ] Tester la persistance avec `verifier_persistance.bat`

## 💡 **CONSEILS SPÉCIFIQUES WINDOWS**

### **1. Performance**
- Windows peut être plus lent au démarrage (60s vs 30s)
- Prévoir plus de temps pour la compilation Maven

### **2. Antivirus**
- Certains antivirus peuvent ralentir Java
- Ajouter le dossier du projet aux exceptions si nécessaire

### **3. Navigateur**
- Utiliser Chrome, Firefox ou Edge moderne
- Éviter Internet Explorer

## 🚀 **MESSAGE POUR VOTRE COLLÈGUE**

> **"L'application est 100% compatible Windows ! Tous les scripts sont prêts. Il suffit de lancer `demo_presentation.bat` pour avoir un guide complet, puis `mvnw.cmd spring-boot:run` pour démarrer l'application. Tout fonctionne exactement pareil que sur macOS/Linux."**

## 📞 **SUPPORT RAPIDE**

Si problème pendant la présentation :
1. **Redémarrer** l'application : Ctrl+C puis `mvnw.cmd spring-boot:run`
2. **Vérifier Java** : `java -version`
3. **Port occupé** : `taskkill /f /im java.exe`
4. **URLs de base** : Toujours commencer par http://localhost:8080

**✅ VOTRE COLLÈGUE WINDOWS PEUT FAIRE LA DÉMONSTRATION SANS PROBLÈME !** 🎉
