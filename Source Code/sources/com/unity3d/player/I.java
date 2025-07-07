package com.unity3d.player;

import android.content.Context;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

class I extends EditText {
    final /* synthetic */ C0032z a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    I(J j, Context context, C0032z zVar) {
        super(context);
        this.a = zVar;
    }

    public boolean onKeyPreIme(int i, KeyEvent keyEvent) {
        if (i == 4) {
            C0032z zVar = this.a;
            zVar.a(zVar.a(), true);
            return true;
        } else if (i == 84) {
            return true;
        } else {
            if (i == 66 && keyEvent.getAction() == 0 && (getInputType() & 131072) == 0) {
                C0032z zVar2 = this.a;
                zVar2.a(zVar2.a(), false);
                return true;
            } else if (i != 111 || keyEvent.getAction() != 0) {
                return super.onKeyPreIme(i, keyEvent);
            } else {
                C0032z zVar3 = this.a;
                zVar3.a(zVar3.a(), true);
                return true;
            }
        }
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z) {
            requestFocus();
            C0032z zVar = this.a;
            ((InputMethodManager) zVar.a.getSystemService("input_method")).showSoftInput(zVar.c, 0);
        }
    }
}
