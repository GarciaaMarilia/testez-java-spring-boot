# Yoga App !


## Lancer l'application

Assurez-vous d’avoir **Java 8+** et **Maven** installés.  
Pour démarrer l'application :

> mvn spring-boot:run

## Lancer les tests

Le projet est configuré avec :

**JUnit 5** – pour les tests unitaires

**Mockito** – pour mocker les dépendances

**Spring Boot Test** – pour les tests d’intégration

**Spring Security Test** – pour tester les endpoints sécurisés

**JaCoCo** – pour la couverture de code

Pour exécuter tous les tests :

> mvn test

## Rapport de couverture (JaCoCo)

For launch and generate the jacoco code coverage:
> mvn clean test

Ce rapport vous montre :

La couverture des classes, méthodes et lignes

Les parties non testées du code


