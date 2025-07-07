package com.unity3d.player;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

class N {
    private Context a;
    private M b;

    public N(Context context) {
        this.a = context;
    }

    public void a() {
        if (this.b != null) {
            this.a.getContentResolver().unregisterContentObserver(this.b);
            this.b = null;
        }
    }

    public void a(L l, String str) {
        this.b = new M(this, new Handler(Looper.getMainLooper()), l);
        this.a.getContentResolver().registerContentObserver(Settings.System.getUriFor(str), true, this.b);
    }
}
