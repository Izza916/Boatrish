package com.unity3d.player;

public class U implements Runnable {
    private V a;
    /* access modifiers changed from: private */
    public boolean b = false;

    public U(V v, V v2) {
        this.a = v2;
    }

    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
        }
        if (!this.b) {
            int i = V.z;
            this.a.cancelOnPrepare();
        }
    }
}
