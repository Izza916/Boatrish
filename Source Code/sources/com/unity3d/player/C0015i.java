package com.unity3d.player;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import com.google.android.play.core.assetpacks.AssetPackException;
import com.google.android.play.core.assetpacks.AssetPackLocation;
import com.google.android.play.core.assetpacks.AssetPackManager;
import com.google.android.play.core.assetpacks.AssetPackManagerFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/* renamed from: com.unity3d.player.i  reason: case insensitive filesystem */
class C0015i implements C0026t {
    /* access modifiers changed from: private */
    public static C0015i d;
    /* access modifiers changed from: private */
    public AssetPackManager a;
    /* access modifiers changed from: private */
    public HashSet b;
    /* access modifiers changed from: private */
    public Object c;

    private C0015i(Context context) {
        if (d == null) {
            this.a = AssetPackManagerFactory.getInstance(context);
            this.b = new HashSet();
            return;
        }
        throw new RuntimeException("AssetPackManagerWrapper should be created only once. Use getInstance() instead.");
    }

    /* access modifiers changed from: private */
    public static int a(Throwable th) {
        while (!(th instanceof AssetPackException)) {
            th = th.getCause();
            if (th == null) {
                return -100;
            }
        }
        return ((AssetPackException) th).getErrorCode();
    }

    public static C0026t a(Context context) {
        if (d == null) {
            d = new C0015i(context);
        }
        return d;
    }

    public Object a(IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
        C0009c cVar = new C0009c(this, iAssetPackManagerDownloadStatusCallback, Looper.myLooper());
        this.a.registerListener(cVar);
        return cVar;
    }

    public String a(String str) {
        AssetPackLocation packLocation = this.a.getPackLocation(str);
        return packLocation == null ? "" : packLocation.assetsPath();
    }

    public void a(Activity activity, IAssetPackManagerMobileDataConfirmationCallback iAssetPackManagerMobileDataConfirmationCallback) {
        this.a.showCellularDataConfirmation(activity).addOnSuccessListener(new C0011e(iAssetPackManagerMobileDataConfirmationCallback));
    }

    public void a(Object obj) {
        if (obj instanceof C0009c) {
            this.a.unregisterListener((C0009c) obj);
        }
    }

    public void a(String[] strArr) {
        this.a.cancel(Arrays.asList(strArr));
    }

    public void a(String[] strArr, IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
        for (String str : strArr) {
            this.a.getPackStates(Collections.singletonList(str)).addOnCompleteListener(new C0012f(iAssetPackManagerDownloadStatusCallback, str));
        }
    }

    public void a(String[] strArr, IAssetPackManagerStatusQueryCallback iAssetPackManagerStatusQueryCallback) {
        this.a.getPackStates(Arrays.asList(strArr)).addOnCompleteListener(new C0014h(iAssetPackManagerStatusQueryCallback, strArr));
    }

    public void b(String str) {
        this.a.removePack(str);
    }
}
