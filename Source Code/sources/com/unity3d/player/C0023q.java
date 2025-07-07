package com.unity3d.player;

import android.graphics.SurfaceTexture;

/* renamed from: com.unity3d.player.q  reason: case insensitive filesystem */
class C0023q implements SurfaceTexture.OnFrameAvailableListener {
    final /* synthetic */ C0025s a;

    C0023q(C0025s sVar) {
        this.a = sVar;
    }

    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        ((Camera2Wrapper) this.a.a).a(surfaceTexture);
    }
}
