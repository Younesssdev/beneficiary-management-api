# beneficiary-management-api

Cette API RESTful, développée en Java 21 et Spring Boot 3.4.2, permet de gérer les bénéficiaires d'une entreprise. Elle stocke les données en mémoire.

## Comment exécuter l'API

### 1. **Prérequis :**
   * Java Development Kit (JDK) 21 ou version ultérieure.
   * Un outil de construction Maven.
   * Un IDE tel qu'IntelliJ IDEA ou VS Code.

### 2. **Cloner le dépôt :**

   ```bash
   git clone https://github.com/Younesssdev/beneficiary-management-api.git
   cd beneficiary-management-api
   ```

### 3. **Construire le projet :**

   * **Maven :**
     ```bash
     mvn clean install
     ```

### 4. **Exécuter l'application :**

   * **Depuis l'IDE :** Exécutez la classe `BeneficiaryManagementApiApplication.java`.
   * **Depuis la ligne de commande :** À la racine du projet.
     ```bash
     # Maven
     mvn spring-boot:run
     ```

### 5. **Accéder à l'API :**
   L'API démarre par défaut sur `http://localhost:8080`.

---

## Points de terminaison de l'API

Je te partage un fichier collection Postman qui contient les endpoints que j'ai testés avec Postman : BpiAPI.postman_collection.
Il suffit d'importer ce fichier pour voir tous les endpoints avec les routes et les données de test. Tu peux modifier les données afin de couvrir tous les tests possibles (notFound, noContent, etc.).

Un fichier contenant le point d'entrée de l'API (baseUrl : "http://localhost:8080") est également fourni. Il suffit de l'importer dans Postman (baseUrl.postman_environment) pour éviter les copier-coller.
  

## Réalisations

* Implémentation des points de terminaison "MUST HAVE" et "NICE TO HAVE" avec les codes de réponse et d'erreur appropriés.
* Utilisation des records Java pour définir les entités.
* Mise en place d'une architecture en couches avec une séparation claire des responsabilités.
* Utilisation des `UUID` pour les identifiants.

## Améliorations souhaitées

* **Validation des entrées** : Ajouter des validations avec les contraintes Javax et `@Valid` pour vérifier les entrées avant traitement.
* **Gestion des erreurs** : Fournir des messages d'erreur plus détaillés dans les réponses.
* **Tests** : Ajouter des tests unitaires et des tests d'intégration pour chaque couche de l'application.
* **Documentation** : Ajouter davantage de documentation pour l'API en utilisant Swagger ou OpenAPI.
* **Authentification** : Intégrer un système d'authentification pour sécuriser l'API.
* **Authentification** : Remplacer le stockage en mémoire par une base de données pour permettre une persistance des données et une meilleure indexation des informations.
* **Perf** : Optimisation des performances des points de terminaison pour des volumes de données importants.


## Ce que je n'ai pas accompli

* L'API ne dispose actuellement pas de validation des entrées.
* Aucun test n'a été inclus.
* L'API n'est pas correctement documentée.
* L'API ne comporte pas de système d'authentification. 


