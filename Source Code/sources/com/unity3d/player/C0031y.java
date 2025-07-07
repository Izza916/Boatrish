package com.unity3d.player;

import android.view.KeyEvent;
import android.widget.TextView;

/* renamed from: com.unity3d.player.y  reason: case insensitive filesystem */
class C0031y implements TextView.OnEditorActionListener {
    final /* synthetic */ C0032z a;

    C0031y(C0032z zVar) {
        this.a = zVar;
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6) {
            C0032z zVar = this.a;
            zVar.a(zVar.a(), false);
        }
        return false;
    }
}
