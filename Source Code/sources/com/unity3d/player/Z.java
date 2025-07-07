package com.unity3d.player;

class Z implements Runnable {
    final /* synthetic */ c0 a;

    Z(c0 c0Var) {
        this.a = c0Var;
    }

    public void run() {
        this.a.a.onPause();
    }
}
