package com.unity3d.player;

import android.database.ContentObserver;
import android.os.Handler;

class M extends ContentObserver {
    private L a;

    public M(N n, Handler handler, L l) {
        super(handler);
        this.a = l;
    }

    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    public void onChange(boolean z) {
        L l = this.a;
        if (l != null) {
            ((OrientationLockListener) l).a(z);
        }
    }
}
