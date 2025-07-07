package com.unity3d.player;

/* renamed from: com.unity3d.player.r  reason: case insensitive filesystem */
enum C0024r {
    STARTED,
    PAUSED,
    STOPPED;

    static {
        STARTED = new C0024r("STARTED", 0);
        PAUSED = new C0024r("PAUSED", 1);
        STOPPED = new C0024r("STOPPED", 2);
    }
}
