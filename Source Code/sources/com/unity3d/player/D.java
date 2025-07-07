package com.unity3d.player;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.EditText;

class D extends EditText {
    final /* synthetic */ C0032z a;
    final /* synthetic */ E b;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    D(E e, Context context, C0032z zVar) {
        super(context);
        this.b = e;
        this.a = zVar;
    }

    public void onEditorAction(int i) {
        if (i == 6) {
            C0032z zVar = this.a;
            zVar.a(zVar.a(), false);
        }
    }

    public boolean onKeyPreIme(int i, KeyEvent keyEvent) {
        if (i == 4) {
            E e = this.b;
            e.a(e.a(), true);
            return true;
        } else if (i == 84) {
            return true;
        } else {
            if (i != 66 || keyEvent.getAction() != 0 || (getInputType() & 131072) != 0) {
                return super.onKeyPreIme(i, keyEvent);
            }
            C0032z zVar = this.a;
            zVar.a(zVar.a(), false);
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void onSelectionChanged(int i, int i2) {
        super.onSelectionChanged(i, i2);
        this.a.b.reportSoftInputSelection(i, i2 - i);
    }
}
