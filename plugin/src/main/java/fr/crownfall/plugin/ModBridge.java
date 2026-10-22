package fr.crownfall.plugin;

import fr.crownfall.api.BlockLocation;
import fr.crownfall.api.Crownfall;
import fr.crownfall.api.event.CapturePointPlacedEvent;
import fr.crownfall.api.event.CapturePointRemovedEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Seule classe du plugin qui touche à l'API du mod : si ses classes sont invisibles, l'erreur se
 * limite à la construction de cet objet.
 */
final class ModBridge {

    private final CrownfallPlugin plugin;
    private final List<AutoCloseable> subscriptions;
    private final AtomicInteger eventsReceived = new AtomicInteger();

    ModBridge(CrownfallPlugin plugin) {
        this.plugin = plugin;
        this.subscriptions = List.of(
                Crownfall.bridge().subscribe(CapturePointPlacedEvent.class, this::onPlaced),
                Crownfall.bridge().subscribe(CapturePointRemovedEvent.class, this::onRemoved));
    }

    int apiVersion() {
        return Crownfall.API_VERSION;
    }

    int eventsReceived() {
        return eventsReceived.get();
    }

    void close() {
        for (AutoCloseable subscription : subscriptions) {
            try {
                subscription.close();
            } catch (Exception e) {
                plugin.getLogger().warning("Désabonnement impossible : " + e.getMessage());
            }
        }
    }

    private void onPlaced(CapturePointPlacedEvent event) {
        eventsReceived.incrementAndGet();
        OfflinePlayer player = Bukkit.getOfflinePlayer(event.playerId());
        announce(player.getName() + " a posé un point de contrôle en " + format(event.location()));
    }

    private void onRemoved(CapturePointRemovedEvent event) {
        eventsReceived.incrementAndGet();
        announce("Un point de contrôle a disparu en " + format(event.location()));
    }

    private void announce(String message) {
        // L'événement vient du code du mod : on repasse par l'ordonnanceur Bukkit pour rester sur le thread principal.
        Bukkit.getScheduler().runTask(plugin, () -> {
            plugin.getLogger().info(message);
            Bukkit.broadcastMessage("[Crownfall] " + message);
        });
    }

    private static String format(BlockLocation location) {
        return location.x() + " " + location.y() + " " + location.z() + " (" + location.dimension() + ")";
    }
}
