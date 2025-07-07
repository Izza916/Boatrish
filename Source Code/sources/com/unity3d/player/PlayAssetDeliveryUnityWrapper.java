package com.unity3d.player;

import android.app.Activity;
import android.content.Context;

class PlayAssetDeliveryUnityWrapper {
    private static PlayAssetDeliveryUnityWrapper b;
    private C0026t a;

    private PlayAssetDeliveryUnityWrapper(Context context) {
        if (b == null) {
            try {
                Class.forName("com.google.android.play.core.assetpacks.AssetPackManager");
                this.a = a(context);
            } catch (ClassNotFoundException unused) {
                this.a = null;
            }
        } else {
            throw new RuntimeException("PlayAssetDeliveryUnityWrapper should be created only once. Use getInstance() instead.");
        }
    }

    private C0026t a(Context context) {
        return C0015i.a(context);
    }

    private void a() {
        if (playCoreApiMissing()) {
            throw new RuntimeException("AssetPackManager API is not available! Make sure your gradle project includes 'com.google.android.play:asset-delivery' dependency.");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0017, code lost:
        if (r1 == null) goto L_0x001b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0022, code lost:
        throw new java.lang.RuntimeException("PlayAssetDeliveryUnityWrapper is not yet initialised.");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized com.unity3d.player.PlayAssetDeliveryUnityWrapper getInstance() {
        /*
            java.lang.Class<com.unity3d.player.PlayAssetDeliveryUnityWrapper> r0 = com.unity3d.player.PlayAssetDeliveryUnityWrapper.class
            monitor-enter(r0)
        L_0x0003:
            com.unity3d.player.PlayAssetDeliveryUnityWrapper r1 = b     // Catch:{ all -> 0x0023 }
            if (r1 != 0) goto L_0x0017
            r1 = 3000(0xbb8, double:1.482E-320)
            r0.wait(r1)     // Catch:{ InterruptedException -> 0x000d }
            goto L_0x0003
        L_0x000d:
            r1 = move-exception
            java.lang.String r1 = r1.getMessage()     // Catch:{ all -> 0x0023 }
            r2 = 6
            com.unity3d.player.C0027u.Log(r2, r1)     // Catch:{ all -> 0x0023 }
            goto L_0x0003
        L_0x0017:
            if (r1 == 0) goto L_0x001b
            monitor-exit(r0)
            return r1
        L_0x001b:
            java.lang.RuntimeException r1 = new java.lang.RuntimeException     // Catch:{ all -> 0x0023 }
            java.lang.String r2 = "PlayAssetDeliveryUnityWrapper is not yet initialised."
            r1.<init>(r2)     // Catch:{ all -> 0x0023 }
            throw r1     // Catch:{ all -> 0x0023 }
        L_0x0023:
            r1 = move-exception
            monitor-exit(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.PlayAssetDeliveryUnityWrapper.getInstance():com.unity3d.player.PlayAssetDeliveryUnityWrapper");
    }

    public static synchronized PlayAssetDeliveryUnityWrapper init(Context context) {
        PlayAssetDeliveryUnityWrapper playAssetDeliveryUnityWrapper;
        Class<PlayAssetDeliveryUnityWrapper> cls = PlayAssetDeliveryUnityWrapper.class;
        synchronized (cls) {
            if (b == null) {
                b = new PlayAssetDeliveryUnityWrapper(context);
                cls.notifyAll();
                playAssetDeliveryUnityWrapper = b;
            } else {
                throw new RuntimeException("PlayAssetDeliveryUnityWrapper.init() should be called only once. Use getInstance() instead.");
            }
        }
        return playAssetDeliveryUnityWrapper;
    }

    public void cancelAssetPackDownload(String str) {
        cancelAssetPackDownloads(new String[]{str});
    }

    public void cancelAssetPackDownloads(String[] strArr) {
        a();
        ((C0015i) this.a).a(strArr);
    }

    public void downloadAssetPack(String str, IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
        downloadAssetPacks(new String[]{str}, iAssetPackManagerDownloadStatusCallback);
    }

    public void downloadAssetPacks(String[] strArr, IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
        a();
        ((C0015i) this.a).a(strArr, iAssetPackManagerDownloadStatusCallback);
    }

    public String getAssetPackPath(String str) {
        a();
        return ((C0015i) this.a).a(str);
    }

    public void getAssetPackState(String str, IAssetPackManagerStatusQueryCallback iAssetPackManagerStatusQueryCallback) {
        getAssetPackStates(new String[]{str}, iAssetPackManagerStatusQueryCallback);
    }

    public void getAssetPackStates(String[] strArr, IAssetPackManagerStatusQueryCallback iAssetPackManagerStatusQueryCallback) {
        a();
        ((C0015i) this.a).a(strArr, iAssetPackManagerStatusQueryCallback);
    }

    public boolean playCoreApiMissing() {
        return this.a == null;
    }

    public Object registerDownloadStatusListener(IAssetPackManagerDownloadStatusCallback iAssetPackManagerDownloadStatusCallback) {
        a();
        return ((C0015i) this.a).a(iAssetPackManagerDownloadStatusCallback);
    }

    public void removeAssetPack(String str) {
        a();
        ((C0015i) this.a).b(str);
    }

    public void requestToUseMobileData(Activity activity, IAssetPackManagerMobileDataConfirmationCallback iAssetPackManagerMobileDataConfirmationCallback) {
        a();
        ((C0015i) this.a).a(activity, iAssetPackManagerMobileDataConfirmationCallback);
    }

    public void unregisterDownloadStatusListener(Object obj) {
        a();
        ((C0015i) this.a).a(obj);
    }
}
