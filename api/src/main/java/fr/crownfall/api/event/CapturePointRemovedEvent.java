package fr.crownfall.api.event;

import fr.crownfall.api.BlockLocation;

import java.util.Objects;

/** Un bloc de point de contrôle a disparu (cassé, explosé ou remplacé). */
public record CapturePointRemovedEvent(BlockLocation location) implements CrownfallEvent {

    public CapturePointRemovedEvent {
        Objects.requireNonNull(location);
    }
}
