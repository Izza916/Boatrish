package com.unity3d.player;

class X implements T {
    final /* synthetic */ Y a;

    X(Y y) {
        this.a = y;
    }

    public void a(int i) {
        this.a.h.e.lock();
        c0 c0Var = this.a.h;
        c0Var.g = i;
        if (i == 3 && c0Var.i) {
            c0Var.runOnUiThread(new W(this));
        }
        if (i != 0) {
            this.a.h.d.release();
        }
        this.a.h.e.unlock();
    }
}
