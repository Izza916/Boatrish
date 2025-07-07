package com.unity3d.player;

class b0 implements Runnable {
    final /* synthetic */ c0 a;

    b0(c0 c0Var) {
        this.a = c0Var;
    }

    public void run() {
        this.a.a();
        this.a.a.onResume();
    }
}
