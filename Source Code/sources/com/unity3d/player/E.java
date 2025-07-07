package com.unity3d.player;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;

class E extends C0032z {
    private boolean g = false;
    private Handler h;
    private Runnable i;

    public E(Context context, UnityPlayer unityPlayer) {
        super(context, unityPlayer);
    }

    public void a(boolean z) {
        EditText editText;
        int i2;
        this.d = z;
        if (z) {
            editText = this.c;
            i2 = 4;
        } else {
            editText = this.c;
            i2 = 0;
        }
        editText.setVisibility(i2);
        this.c.invalidate();
        this.c.requestLayout();
    }

    public void b() {
        Runnable runnable;
        Handler handler = this.h;
        if (!(handler == null || (runnable = this.i) == null)) {
            handler.removeCallbacks(runnable);
        }
        this.b.removeView(this.c);
        this.g = false;
    }

    public boolean c() {
        return false;
    }

    /* access modifiers changed from: protected */
    public EditText createEditText(C0032z zVar) {
        return new D(this, this.a, zVar);
    }

    public void d() {
        if (!this.g) {
            this.b.addView(this.c);
            this.b.bringChildToFront(this.c);
            this.c.setVisibility(0);
            this.c.requestFocus();
            this.i = new C(this);
            Handler handler = new Handler(Looper.getMainLooper());
            this.h = handler;
            handler.postDelayed(this.i, 400);
            this.g = true;
        }
    }
}
