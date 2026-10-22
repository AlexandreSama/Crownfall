package fr.crownfall.api;

import java.util.Objects;

/** Position d'un bloc ; {@code dimension} est l'identifiant NeoForge, par exemple {@code minecraft:overworld}. */
public record BlockLocation(String dimension, int x, int y, int z) {

    public BlockLocation {
        Objects.requireNonNull(dimension);
    }
}
