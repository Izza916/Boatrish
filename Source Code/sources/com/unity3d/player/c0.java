package com.unity3d.player;

import android.app.Activity;
import android.content.Context;
import com.unity3d.player.UnityPlayer;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class c0 {
    /* access modifiers changed from: private */
    public UnityPlayer a = null;
    /* access modifiers changed from: private */
    public Context b = null;
    private a c;
    /* access modifiers changed from: private */
    public final Semaphore d = new Semaphore(0);
    /* access modifiers changed from: private */
    public final Lock e = new ReentrantLock();
    /* access modifiers changed from: private */
    public V f = null;
    /* access modifiers changed from: private */
    public int g = 2;
    private boolean h = false;
    /* access modifiers changed from: private */
    public boolean i = false;

    public interface a {
    }

    c0(UnityPlayer unityPlayer) {
        this.a = unityPlayer;
    }

    /* access modifiers changed from: private */
    public void a() {
        V v = this.f;
        if (v != null) {
            this.a.removeViewFromPlayer(v);
            this.i = false;
            this.f.destroyPlayer();
            this.f = null;
            a aVar = this.c;
            if (aVar != null) {
                ((UnityPlayer.n) aVar).a();
            }
        }
    }

    public boolean a(Context context, String str, int i2, int i3, int i4, boolean z, long j, long j2, a aVar) {
        this.e.lock();
        this.c = aVar;
        this.b = context;
        this.d.drainPermits();
        this.g = 2;
        runOnUiThread(new Y(this, str, i2, i3, i4, z, j, j2));
        boolean z2 = false;
        try {
            this.e.unlock();
            this.d.acquire();
            this.e.lock();
            if (this.g != 2) {
                z2 = true;
            }
        } catch (InterruptedException unused) {
        }
        runOnUiThread(new Z(this));
        runOnUiThread((!z2 || this.g == 3) ? new b0(this) : new a0(this));
        this.e.unlock();
        return z2;
    }

    public void b() {
        this.e.lock();
        V v = this.f;
        if (v != null) {
            v.updateVideoLayout();
        }
        this.e.unlock();
    }

    public void c() {
        this.e.lock();
        V v = this.f;
        if (v != null) {
            if (this.g == 0) {
                v.cancelOnPrepare();
            } else if (this.i) {
                boolean a2 = v.a();
                this.h = a2;
                if (!a2) {
                    this.f.pause();
                }
            }
        }
        this.e.unlock();
    }

    public void d() {
        this.e.lock();
        V v = this.f;
        if (v != null && this.i && !this.h) {
            v.start();
        }
        this.e.unlock();
    }

    /* access modifiers changed from: protected */
    public void runOnUiThread(Runnable runnable) {
        Context context = this.b;
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(runnable);
        } else {
            C0027u.Log(5, "Not running from an Activity; Ignoring execution request...");
        }
    }
}
