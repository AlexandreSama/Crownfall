# Crownfall

Serveur Minecraft Royaume VS Royaume : un mod NeoForge et un plugin Spigot qui tournent ensemble sur un serveur hybride Youer (Minecraft 1.21.1).

## Organisation

| Module | Rôle |
| --- | --- |
| `api` | Événements et types partagés entre le mod et le plugin. Livré dans le jar du mod. |
| `mod` | Mod NeoForge : blocs, items, entités, interface. Installé sur le serveur et chez chaque joueur. |
| `plugin` | Plugin Spigot : règles du jeu (royaumes, territoires, guerres, économie). |

Le mod publie des événements sur `Crownfall.bridge()` et le plugin s'y abonne. Le plugin compile l'API en `compileOnly` : à l'exécution il utilise la copie fournie par le mod, ce qui suppose que Youer rende les classes des mods visibles aux plugins.

## Compiler

Java 21 requis.

```
./gradlew build
```

Les jars se trouvent dans `mod/build/libs` et `plugin/build/libs`.

## Tester le pont sur Youer

1. Copier `crownfall-mod-*.jar` dans `mods/` et `crownfall-plugin-*.jar` dans `plugins/`.
2. Au démarrage, la console doit afficher `Pont avec le mod Crownfall établi`.
3. En jeu, poser un « Point de contrôle » (onglet Blocs fonctionnels) : un message est diffusé dans le chat. Le casser en diffuse un autre.
4. `/crownfall` affiche l'état du pont et le nombre d'événements reçus.
