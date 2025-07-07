package com.unity3d.player;

import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;

/* renamed from: com.unity3d.player.j  reason: case insensitive filesystem */
class C0016j extends ContentObserver {
    private final C0017k a;
    private final AudioManager b;
    private final int c;
    private int d;

    public C0016j(C0018l lVar, Handler handler, AudioManager audioManager, int i, C0017k kVar) {
        super(handler);
        this.b = audioManager;
        this.c = i;
        this.a = kVar;
        this.d = audioManager.getStreamVolume(i);
    }

    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    public void onChange(boolean z, Uri uri) {
        int streamVolume;
        AudioManager audioManager = this.b;
        if (audioManager != null && this.a != null && (streamVolume = audioManager.getStreamVolume(this.c)) != this.d) {
            this.d = streamVolume;
            ((AudioVolumeHandler) this.a).onAudioVolumeChanged(streamVolume);
        }
    }
}
