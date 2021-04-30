![GitHub Workflow Status](https://img.shields.io/github/workflow/status/ennes/lib-analyse-devops/CI) [![codecov](https://codecov.io/gh/EnneS/lib-analyse-devops/branch/master/graph/badge.svg?token=789XG0IDJT)](https://codecov.io/gh/EnneS/lib-analyse-devops) [![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/ennes/lib-analyse-devops?include_prereleases)
](https://github.com/EnneS/lib-analyse-devops/releases/tag/1.0)

<h1>Librairie d'analyse de Dataframe</h1>

<h2>Documentation</h2>
Retrouvez la documentation de notre librairie à cette adresse : 

[Documentation](https://ennes.github.io/lib-analyse-devops)

<h2>Fonctionnalitées:</h2>

**Création de dataframe**
- A partir d'une structure de données simple (Hashmap)
- A partir d'un fichier CSV

**Affichage d'un dataframe**
 - Afficher tout le dataframe
 - Afficher seulement les premières lignes
 - Afficher seulement les dernières lignes
 
**Sélection dans un dataframe**
- Sélection d'un sous-ensemble de lignes à partir de leur index
- Sélection d'un sous-ensemble de colonnes à partir de leur labels

**Statistiques sur un dataframe**
- Moyenne d'une colonne
- Minimum d'une colonne
- Maximum d'une colonne

<h2>Outils utilisés:</h2>

- Eclipse
- Maven
- Javadoc
- Surefire Report
- Codecov
- Github Pages

<h2>Git Workflow:</h2>
<h3>SetUp Workflow</h3>
Le workflow est constitué de plusieurs étapes :

1. Compilation du projet
2. Exécution des tests
3. Publication du report des tests
4. Publication du code coverage vers Codecov
5. Compilation de la documentation
6. Déploiement de la documentation vers Github Pages

<h3>Organisation des branches</h3>

Pour les branches le choix à été fait de faire 2 branches principales, la branch **master** et la branch **dev**. 
- A chaque **feature**, une nouvelle branche est créée depuis **dev**.
- Une fois la **feature** finie et les tests OK, on **rebase** la branche **feature** avec la branche **dev** afin de garder la branche **feature** à jour avant de merge.
- Si les tests passent toujours alors on crée une **Pull Request** pour merge la branche **feature** avec la branche **dev**.
- Finalement, si on souhaite mettre la branche **dev** en production, on crée à nouveau une **Pull Request** afin de merge **dev** vers **master**.

Une branche **gh-pages** est aussi présente et sert au déploiement de la documentation.

<h2>Feedback:</h2>
