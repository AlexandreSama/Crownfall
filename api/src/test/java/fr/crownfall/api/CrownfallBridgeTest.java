package fr.crownfall.api;

import fr.crownfall.api.event.CapturePointPlacedEvent;
import fr.crownfall.api.event.CapturePointRemovedEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CrownfallBridgeTest {

    private static final BlockLocation HERE = new BlockLocation("minecraft:overworld", 10, 64, -3);

    @Test
    void deliversOnlyMatchingEvents() {
        CrownfallBridge bridge = new CrownfallBridge();
        List<CapturePointPlacedEvent> placed = new ArrayList<>();
        bridge.subscribe(CapturePointPlacedEvent.class, placed::add);

        CapturePointPlacedEvent event = new CapturePointPlacedEvent(HERE, UUID.randomUUID());
        bridge.publish(event);
        bridge.publish(new CapturePointRemovedEvent(HERE));

        assertEquals(1, placed.size());
        assertSame(event, placed.get(0));
    }

    @Test
    void failingListenerDoesNotBlockOthers() {
        CrownfallBridge bridge = new CrownfallBridge();
        List<CapturePointRemovedEvent> removed = new ArrayList<>();
        bridge.subscribe(CapturePointRemovedEvent.class, e -> {
            throw new IllegalStateException("boum");
        });
        bridge.subscribe(CapturePointRemovedEvent.class, removed::add);

        bridge.publish(new CapturePointRemovedEvent(HERE));

        assertEquals(1, removed.size());
    }

    @Test
    void closingTheTokenUnsubscribes() throws Exception {
        CrownfallBridge bridge = new CrownfallBridge();
        List<CapturePointRemovedEvent> removed = new ArrayList<>();
        AutoCloseable token = bridge.subscribe(CapturePointRemovedEvent.class, removed::add);

        token.close();
        bridge.publish(new CapturePointRemovedEvent(HERE));

        assertEquals(0, removed.size());
        assertEquals(0, bridge.subscriberCount());
    }
}
