package com.unity3d.player;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

/* renamed from: com.unity3d.player.l  reason: case insensitive filesystem */
class C0018l {
    private final Context a;
    private final AudioManager b;
    private C0016j c;

    public C0018l(Context context) {
        this.a = context;
        this.b = (AudioManager) context.getSystemService("audio");
    }

    public void a() {
        if (this.c != null) {
            this.a.getContentResolver().unregisterContentObserver(this.c);
            this.c = null;
        }
    }

    public void a(int i, C0017k kVar) {
        this.c = new C0016j(this, new Handler(Looper.getMainLooper()), this.b, i, kVar);
        this.a.getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, this.c);
    }
}
