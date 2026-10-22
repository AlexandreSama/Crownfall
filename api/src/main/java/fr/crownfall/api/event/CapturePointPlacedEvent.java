package fr.crownfall.api.event;

import fr.crownfall.api.BlockLocation;

import java.util.Objects;
import java.util.UUID;

/** Un joueur a posé un bloc de point de contrôle. */
public record CapturePointPlacedEvent(BlockLocation location, UUID playerId) implements CrownfallEvent {

    public CapturePointPlacedEvent {
        Objects.requireNonNull(location);
        Objects.requireNonNull(playerId);
    }
}
