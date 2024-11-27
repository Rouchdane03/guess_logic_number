# Guess Logic Number

**Guess Logic Number** est un projet de jeu interactif où les joueurs doivent deviner un numéro à travers une logique spécifique. Le projet utilise Java 17, Spring Boot et Hibernate pour créer une application web robuste et performante.

## Fonctionnalités

- **Jeu de devinette** : Les utilisateurs doivent trouver le numéro logique caché.
- **Collecte de feedback** : Une fois le jeu terminé, les utilisateurs peuvent donner leur avis sur l'expérience.
- **API REST** : Fournit des endpoints pour interagir avec l'application via un frontend ou des outils tiers.
- **Gestion des données** : Persistance des informations grâce à Hibernate JPA.
- **CI/CD** : Déploiement continu grâce à GitHub Actions.

## Prérequis

- Java 17
- Maven
- Docker (pour le déploiement sur des environnements Docker)

## Installation

1. Clonez le dépôt :

   ```bash
   git clone https://github.com/Rouchdane03/guess_logic_number.git
   ```

2. Accédez au répertoire du projet :

   ```bash
   cd guess_logic_number
   ```

3. Installez les dépendances et compilez le projet :

   ```bash
   mvn clean install
   ```

4. Lancez l'application :

   ```bash
   mvn spring-boot:run
   ```

5. Ouvrez votre navigateur à l'adresse suivante :

   ```
   http://localhost:8081
   ```

## Tests

Pour exécuter les tests unitaires et d’intégration :

```bash
mvn test
```

## Déploiement

1. Construisez l'image Docker :

   ```bash
   docker build -t guess_logic_number .
   ```

2. Lancez le conteneur :

   ```bash
   docker-compose up
   ```

3. L’application sera accessible sur le port configuré (par défaut : `8081`).

## Auteur

- **Rouchdane03**

Si vous avez des questions ou des suggestions, n’hésitez pas à ouvrir une issue sur le dépôt GitHub.

