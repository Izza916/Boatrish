package com.unity3d.player;

import android.view.inputmethod.InputMethodManager;

class C implements Runnable {
    final /* synthetic */ E a;

    C(E e) {
        this.a = e;
    }

    public void run() {
        this.a.c.requestFocus();
        E e = this.a;
        ((InputMethodManager) e.a.getSystemService("input_method")).showSoftInput(e.c, 0);
    }
}
