# SAE Java 2026 S2

Ce projet Java est une application graphique développée avec Swing. Il peut être compilé, exécuté et testé via Apache Ant.
> Note : la sélection des sommet avec le clique ne fonctionne pas sous linux.

## Prérequis

Avant de lancer le programme, assurez-vous d'avoir :

- Java JDK installé (version compatible avec le projet)
- Apache Ant installé et disponible dans votre PATH

## Structure du projet

- `src/` : code source Java
- `build/` : fichiers compilés générés par Ant
- `dist/` : artefacts Java générés (JAR)
- `lib/` : dépendances externes
- `build.xml` : configuration Apache Ant

## Exécution avec Apache Ant

Depuis la racine du projet, ouvrez un terminal et exécutez :

```bash
ant
```

Cette commande compile le projet par défaut.

### Lancer l'application

```bash
ant run
```

Cette commande :

1. compile les sources,
2. crée le JAR dans `dist/`,
3. lance l'application.

### Compiler uniquement

```bash
ant compile
```

### Nettoyer les fichiers générés

```bash
ant clean
```

### Exécuter les tests

```bash
ant test
```

Vous pouvez aussi lancer un test particulier :

```bash
ant test -Dtest=CarteTest
```

## Exécution directe avec Java

Si vous préférez lancer le JAR généré manuellement :

```bash
java -jar dist/SAE_JAVA_2026S2.jar
```

## Débogage

Pour démarrer l'application en mode débogage :

```bash
ant debug
```

Cela lance l'application avec un port de débogage configuré dans `build.xml`.

## Générer la Javadoc

```bash
ant docs
```

> Note : la cible nommée vide dans le fichier Ant génère la documentation Javadoc dans le dossier `docs/`. Pour lire la documentation ouvrer le fichier `docs/index.html`.
> Note : Ne prèter par attention au 100 warning.

## Notes importantes

- Si vous êtes sous Windows, assurez-vous que `java` et `ant` sont bien ajoutés à la variable d'environnement `PATH`.
- Si un problème survient pendant l’exécution, essayez d’abord :

```bash
ant clean
ant run
```
