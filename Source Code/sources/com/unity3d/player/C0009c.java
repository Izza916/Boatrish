package com.unity3d.player;

import android.os.Handler;
import android.os.Looper;
import com.google.android.play.core.assetpacks.AssetPackState;
import com.google.android.play.core.assetpacks.AssetPackStateUpdateListener;
import java.util.HashSet;
import java.util.Set;

/* renamed from: com.unity3d.player.c  reason: case insensitive filesystem */
class C0009c implements AssetPackStateUpdateListener {
    /* access modifiers changed from: private */
    public HashSet a;
    private Looper b;
    final /* synthetic */ C0015i c;

    public C0009c(C0015i iVar, IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback, Looper looper) {
        this.c = iVar;
        HashSet hashSet = new HashSet();
        this.a = hashSet;
        hashSet.add(iAssetPackManagerDownloadStatusCallback);
        this.b = looper;
    }

    public void onStateUpdate(Object obj) {
        AssetPackState assetPackState = (AssetPackState) obj;
        synchronized (this) {
            if (assetPackState.status() == 4 || assetPackState.status() == 5 || assetPackState.status() == 0) {
                synchronized (C0015i.d) {
                    this.c.b.remove(assetPackState.name());
                    if (this.c.b.isEmpty()) {
                        C0015i iVar = this.c;
                        Object r2 = iVar.c;
                        if (r2 instanceof C0009c) {
                            iVar.a.unregisterListener((C0009c) r2);
                        }
                        this.c.c = null;
                    }
                }
            }
            if (this.a.size() != 0) {
                new Handler(this.b).post(new C0008b((Set) this.a.clone(), assetPackState.name(), assetPackState.status(), assetPackState.totalBytesToDownload(), assetPackState.bytesDownloaded(), assetPackState.transferProgressPercentage(), assetPackState.errorCode()));
            }
        }
    }
}
