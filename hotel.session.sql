-- @block 
CREATE TABLE Utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    identifiant VARCHAR(255) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    statut VARCHAR(255) NOT NULL,
    date_inscription DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- @block 
CREATE TABLE Chambre (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero INT NOT NULL,
    categorie VARCHAR(255) NOT NULL,
    prix DECIMAL(10, 2) NOT NULL,
    statut VARCHAR(255) NOT NULL
);

-- @block
CREATE TABLE Evenement (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    date DATE NOT NULL,
    type VARCHAR(100) NOT NULL
);

-- @block
CREATE TABLE Avis (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_utilisateur INT NULL,
    commentaire VARCHAR(500) NOT NULL,
    note INT NOT NULL,
    reponse VARCHAR(500),
    FOREIGN KEY (id_utilisateur) REFERENCES Utilisateur(id) ON DELETE SET NULL
);

-- @block
CREATE TABLE Reservation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_chambre INT NOT NULL,
    id_utilisateur INT NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    FOREIGN KEY (id_chambre) REFERENCES Chambre(id) ON DELETE CASCADE,
    FOREIGN KEY (id_utilisateur) REFERENCES Utilisateur(id) ON DELETE CASCADE
);