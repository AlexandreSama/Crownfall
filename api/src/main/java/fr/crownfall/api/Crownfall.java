package fr.crownfall.api;

/**
 * Point d'entrée de l'API partagée entre le mod et le plugin.
 *
 * <p>Les classes de ce module ne sont livrées qu'une fois, dans le jar du mod. Le mod et le plugin
 * voient donc la même instance de {@link CrownfallBridge}, à condition que Youer expose les classes
 * du mod aux plugins.
 */
public final class Crownfall {

    /** Incrémentée à chaque changement incompatible de l'API. */
    public static final int API_VERSION = 1;

    private static final CrownfallBridge BRIDGE = new CrownfallBridge();

    private Crownfall() {
    }

    public static CrownfallBridge bridge() {
        return BRIDGE;
    }
}
