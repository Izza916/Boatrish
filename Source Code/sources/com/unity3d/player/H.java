package com.unity3d.player;

import android.content.DialogInterface;

class H implements DialogInterface.OnCancelListener {
    final /* synthetic */ J a;

    H(J j) {
        this.a = j;
    }

    public void onCancel(DialogInterface dialogInterface) {
        A a2 = this.a.f;
        if (a2 != null) {
            a2.a();
        }
    }
}
