package com.errorC003C004.simply_optimized.networking;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class HandshakeTracker {
    private HandshakeTracker() {}

    // tick when they joined (server tick counter value)
    public static final Map<UUID, Long> JOIN_TICK = new ConcurrentHashMap<>();

    // true if packet received OR already processed timeout (prevents repeat messages)
    public static final Map<UUID, Boolean> DONE = new ConcurrentHashMap<>();
}