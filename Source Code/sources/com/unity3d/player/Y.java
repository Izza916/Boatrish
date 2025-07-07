package com.unity3d.player;

class Y implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ int b;
    final /* synthetic */ int c;
    final /* synthetic */ int d;
    final /* synthetic */ boolean e;
    final /* synthetic */ long f;
    final /* synthetic */ long g;
    final /* synthetic */ c0 h;

    Y(c0 c0Var, String str, int i, int i2, int i3, boolean z, long j, long j2) {
        this.h = c0Var;
        this.a = str;
        this.b = i;
        this.c = i2;
        this.d = i3;
        this.e = z;
        this.f = j;
        this.g = j2;
    }

    public void run() {
        c0 c0Var = this.h;
        if (c0Var.f != null) {
            C0027u.Log(5, "Video already playing");
            c0 c0Var2 = this.h;
            c0Var2.g = 2;
            c0Var2.d.release();
            return;
        }
        c0Var.f = new V(this.h.b, this.a, this.b, this.c, this.d, this.e, this.f, this.g, new X(this));
        c0 c0Var3 = this.h;
        if (c0Var3.f != null) {
            c0Var3.a.bringToFront();
            c0 c0Var4 = this.h;
            c0Var4.a.addView(c0Var4.f);
        }
    }
}
