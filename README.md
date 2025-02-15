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

* Je te partage un fichier collection Postman qui contient les endpoints que j'ai testés avec Postman : BpiAPI.postman_collection.
Il suffit d'importer ce fichier pour voir tous les endpoints avec les routes et les données de test. Tu peux modifier les données afin de couvrir tous les tests possibles (notFound, noContent, etc.).

* Un fichier contenant le point d'entrée de l'API (baseUrl : "http://localhost:8080") est également fourni. Il suffit de l'importer dans Postman (baseUrl.postman_environment) pour éviter les copier-coller.

* J'ai ajouté un endpoint getAllCompanies pour récupérer la liste des entreprises et voir leurs IDs afin de tester la première API « MUST HAVE » (Voir collection Postamn).

* J'ai ajouté des données de test similaires au cas n°3 : « détention directe et indirecte du capital », présenté dans le test technique, afin de tester la première API spécifiée comme « MUST HAVE ».


## Réalisations

* Implémentation des points de terminaison "MUST HAVE" et "NICE TO HAVE" avec les codes de réponse et d'erreur appropriés.
* Utilisation des records Java pour définir les entités.
* Mise en place d'une architecture en couches avec une séparation claire des responsabilités.
* Utilisation des `UUID` pour les identifiants.

## Améliorations souhaitées

* * **Couche repository** : Ajouter une couche repository contenant des méthodes telles que findById et save... pour la partie entreprise et bénéficiaire.
* **Gestion des erreurs** : Fournir des messages d'erreur plus détaillés dans les réponses, en utilisant des exceptions personnalisées et une gestion centralisée des erreurs via @ControllerAdvice.
* * **Gestion des logs** : Ajouter les logs dans les différentes API et dans les services.
* **Tests** : Ajouter des tests unitaires et des tests d'intégration pour chaque couche de l'application.
* **Documentation** : Ajouter davantage de documentation pour l'API en utilisant Swagger ou OpenAPI.
* **Authentification** : Intégrer un système d'authentification pour sécuriser l'API.
* **Authentification** : Remplacer le stockage en mémoire par une base de données pour permettre une persistance des données et une meilleure indexation des informations.
* **Perf** : Optimisation des performances des points de terminaison pour des volumes de données importants.
* * **Validation des entrées** : Ajouter des validations avec les contraintes Javax et `@Valid` pour vérifier les entrées avant traitement.


## Ce que je n'ai pas accompli

* L'ajout d'une couche repository (Essentiel pour assurer une séparation claire des couches. Elle permet de structurer l'application de manière plus modulaire, d'améliorer la maintenabilité et de faciliter les tests unitaires).
* Utiliser des classes pour la partie modèle à la place des records en cas d'utilisation d'une couche repository.
* Aucun test n'a été inclus.


