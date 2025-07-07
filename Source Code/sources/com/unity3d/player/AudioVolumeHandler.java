package com.unity3d.player;

import android.content.Context;

public class AudioVolumeHandler implements C0017k {
    private C0018l a;

    AudioVolumeHandler(Context context) {
        C0018l lVar = new C0018l(context);
        this.a = lVar;
        lVar.a(3, this);
    }

    public void a() {
        this.a.a();
        this.a = null;
    }

    public final native void onAudioVolumeChanged(int i);
}
