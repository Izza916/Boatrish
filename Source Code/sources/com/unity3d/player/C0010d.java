package com.unity3d.player;

/* renamed from: com.unity3d.player.d  reason: case insensitive filesystem */
class C0010d implements Runnable {
    private IAssetPackManagerMobileDataConfirmationCallback a;
    private boolean b;

    C0010d(IAssetPackManagerMobileDataConfirmationCallback iAssetPackManagerMobileDataConfirmationCallback, boolean z) {
        this.a = iAssetPackManagerMobileDataConfirmationCallback;
        this.b = z;
    }

    public void run() {
        this.a.onMobileDataConfirmationResult(this.b);
    }
}
