package com.unity3d.player;

class a0 implements Runnable {
    final /* synthetic */ c0 a;

    a0(c0 c0Var) {
        this.a = c0Var;
    }

    public void run() {
        c0 c0Var = this.a;
        V r1 = c0Var.f;
        if (r1 != null) {
            c0Var.a.addViewToPlayer(r1, true);
            c0 c0Var2 = this.a;
            c0Var2.i = true;
            c0Var2.f.requestFocus();
        }
    }
}
