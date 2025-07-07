package com.unity3d.player;

import android.view.ViewTreeObserver;

class G implements ViewTreeObserver.OnGlobalLayoutListener {
    final /* synthetic */ J a;

    G(J j) {
        this.a = j;
    }

    public void onGlobalLayout() {
        this.a.reportSoftInputArea();
    }
}
