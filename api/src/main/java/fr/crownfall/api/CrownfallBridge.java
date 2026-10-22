package fr.crownfall.api;

import fr.crownfall.api.event.CrownfallEvent;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Bus d'événements minimal : le mod publie, le plugin s'abonne (et inversement). */
public final class CrownfallBridge {

    private static final Logger LOGGER = Logger.getLogger("Crownfall");

    private final List<Subscription<?>> subscriptions = new CopyOnWriteArrayList<>();

    CrownfallBridge() {
    }

    /** Abonne {@code listener} aux événements de type {@code type} ; renvoie un jeton pour se désabonner. */
    public <E extends CrownfallEvent> AutoCloseable subscribe(Class<E> type, Consumer<? super E> listener) {
        Subscription<E> subscription = new Subscription<>(Objects.requireNonNull(type), Objects.requireNonNull(listener));
        subscriptions.add(subscription);
        return () -> subscriptions.remove(subscription);
    }

    /** Transmet {@code event} à chaque abonné ; un abonné qui lève une exception n'empêche pas les autres. */
    public void publish(CrownfallEvent event) {
        Objects.requireNonNull(event);
        for (Subscription<?> subscription : subscriptions) {
            try {
                subscription.deliver(event);
            } catch (RuntimeException e) {
                LOGGER.log(Level.SEVERE, "Un abonné a échoué sur " + event.getClass().getSimpleName(), e);
            }
        }
    }

    public int subscriberCount() {
        return subscriptions.size();
    }

    private record Subscription<E extends CrownfallEvent>(Class<E> type, Consumer<? super E> listener) {
        void deliver(CrownfallEvent event) {
            if (type.isInstance(event)) {
                listener.accept(type.cast(event));
            }
        }
    }
}
