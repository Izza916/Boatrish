package com.unity3d.player;

/* renamed from: com.unity3d.player.g  reason: case insensitive filesystem */
class C0013g implements Runnable {
    private IAssetPackManagerStatusQueryCallback a;
    private long b;
    private String[] c;
    private int[] d;
    private int[] e;

    C0013g(IAssetPackManagerStatusQueryCallback iAssetPackManagerStatusQueryCallback, long j, String[] strArr, int[] iArr, int[] iArr2) {
        this.a = iAssetPackManagerStatusQueryCallback;
        this.b = j;
        this.c = strArr;
        this.d = iArr;
        this.e = iArr2;
    }

    public void run() {
        this.a.onStatusResult(this.b, this.c, this.d, this.e);
    }
}
