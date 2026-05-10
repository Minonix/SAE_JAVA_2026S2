# Guide de Débogage - SAE_JAVA_2026S2

## Configuration requise
- VS Code avec l'extension "Extension Pack for Java" installée
- Java 26+ avec support JDWP
- Port 5005 disponible

## Comment déboguer

### Étape 1 : Lancer le JVM en mode débogage
Ouvrez un terminal et exécutez :

```bash
java -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005 -jar dist/SAE_JAVA_2026S2.jar
```

Vous devez voir :
```
Java HotSpot(TM) 64-Bit Server VM warning: Option -Xdebug was deprecated in JDK 22 and will likely be removed in a future release.
Listening for transport dt_socket at address: 5005
```

**⚠️  IMPORTANT** : Le JVM est maintenant en attente de connexion du débogueur. Il n'exécutera rien tant que VS Code ne s'est pas connecté.

### Étape 2 : Connecter VS Code au JVM

1. Dans VS Code, ouvrez le panneau **Run and Debug** (Ctrl+Shift+D)
2. Sélectionnez la configuration **"Debug SAE_JAVA_2026S2"**
3. Cliquez sur le bouton **Play** (ou appuyez sur F5)

VS Code devrait se connecter au JVM et le code s'arrêtera à la première ligne de `main()`.

### Étape 3 : Déboguer
Une fois connecté, vous pouvez :
- **Ajouter des breakpoints** : Cliquez à gauche du numéro de ligne
- **Pas à pas** : F10 (Step Over), F11 (Step Into)
- **Continuer** : F5 ou cliquez sur le bouton "Continue"
- **Variables** : Regardez le panneau "Variables" pour inspecter les valeurs

### Alternative avec ant
Pour utiliser Ant directement :

```bash
ant debug
```

Cela compile, empaquète et lance le JAR en mode débogage.

## Notes importantes

- **suspend=y** : Le JVM attend la connexion du débogueur avant de démarrer
- **Port 5005** : Port par défaut pour JDWP (configurable dans `build.xml`)
- **Timeout** : 120 secondes pour se connecter (configurable dans `build.xml`)
- **Main.java** : Le code attend une entrée utilisateur après les logs de débogage pour laisser du temps

## Troubleshooting

### "Address already in use"
Le port 5005 est occupé. Tuez tous les processus Java :
```bash
Get-Process java | Stop-Process -Force  # Windows PowerShell
killall java                             # Linux/macOS
```

### VS Code ne se connecte pas
- Vérifiez que le port est correct (5005)
- Vérifiez que `localhost` est accessible
- Vérifiez que `.vscode/launch.json` contient la configuration `attach`

### Le code s'exécute sans s'arrêter
Assurez-vous d'utiliser `-Xdebug` ou de bien avoir `suspend=y` dans le JDWP.
