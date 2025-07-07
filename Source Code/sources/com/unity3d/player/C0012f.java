package com.unity3d.player;

import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.assetpacks.AssetPackState;
import com.google.android.play.core.assetpacks.AssetPackStates;
import java.util.Collections;
import java.util.Map;

/* renamed from: com.unity3d.player.f  reason: case insensitive filesystem */
class C0012f implements OnCompleteListener {
    private IAssetPackManagerDownloadStatusCallback a;
    private Looper b = Looper.myLooper();
    private String c;

    public C0012f(IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback, String str) {
        this.a = iAssetPackManagerDownloadStatusCallback;
        this.c = str;
    }

    private void a(String str, int i, int i2, long j) {
        new Handler(this.b).post(new C0008b(Collections.singleton(this.a), str, i, j, i == 4 ? j : 0, 0, i2));
    }

    public void onComplete(Task task) {
        try {
            AssetPackStates assetPackStates = (AssetPackStates) task.getResult();
            Map packStates = assetPackStates.packStates();
            if (packStates.size() != 0) {
                for (AssetPackState assetPackState : packStates.values()) {
                    if (assetPackState.errorCode() != 0 || assetPackState.status() == 4 || assetPackState.status() == 5 || assetPackState.status() == 0) {
                        a(assetPackState.name(), assetPackState.status(), assetPackState.errorCode(), assetPackStates.totalBytes());
                    } else {
                        C0015i r2 = C0015i.d;
                        String name = assetPackState.name();
                        IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback = this.a;
                        Looper looper = this.b;
                        r2.getClass();
                        synchronized (C0015i.d) {
                            Object r6 = r2.c;
                            if (r6 == null) {
                                C0009c cVar = new C0009c(r2, iAssetPackManagerDownloadStatusCallback, looper);
                                r2.a.registerListener(cVar);
                                r2.c = cVar;
                            } else {
                                C0009c cVar2 = (C0009c) r6;
                                synchronized (cVar2) {
                                    cVar2.a.add(iAssetPackManagerDownloadStatusCallback);
                                }
                            }
                            r2.b.add(name);
                            r2.a.fetch(Collections.singletonList(name));
                        }
                    }
                }
            }
        } catch (RuntimeExecutionException e) {
            a(this.c, 0, C0015i.a((Throwable) e), 0);
        }
    }
}
