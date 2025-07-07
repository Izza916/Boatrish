package com.unity3d.player;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

class B extends Dialog implements View.OnClickListener {
    protected Context a = null;
    protected UnityPlayer b = null;
    protected C0030x c = null;
    protected C0032z d = null;

    public B(Context context, UnityPlayer unityPlayer) {
        super(context);
        this.a = context;
        this.b = unityPlayer;
    }

    public void a(boolean z) {
        C0030x xVar = this.c;
        if (z) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) xVar.b.getLayoutParams();
            layoutParams.height = 1;
            xVar.b.setLayoutParams(layoutParams);
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) xVar.a.getLayoutParams();
            layoutParams2.height = 1;
            xVar.a.setLayoutParams(layoutParams2);
            Rect rect = xVar.e;
            xVar.setPadding(rect.left, rect.top, rect.right, rect.bottom);
            xVar.setVisibility(4);
        } else {
            xVar.setVisibility(0);
            Rect rect2 = xVar.d;
            xVar.setPadding(rect2.left, rect2.top, rect2.right, rect2.bottom);
            RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) xVar.b.getLayoutParams();
            layoutParams3.height = -2;
            xVar.b.setLayoutParams(layoutParams3);
            RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) xVar.a.getLayoutParams();
            layoutParams4.height = -2;
            xVar.a.setLayoutParams(layoutParams4);
        }
        xVar.invalidate();
        xVar.requestLayout();
    }

    /* access modifiers changed from: protected */
    public C0030x createSoftInputView(EditText editText) {
        C0030x xVar = new C0030x(this.a, editText);
        xVar.a.setOnClickListener(this);
        setContentView(xVar);
        return xVar;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.d.c() || (motionEvent.getAction() != 4 && !this.d.d)) {
            return super.dispatchTouchEvent(motionEvent);
        }
        return true;
    }

    public void onClick(View view) {
        C0032z zVar = this.d;
        zVar.a(zVar.a(), false);
    }

    public void onStop() {
        super.onStop();
    }
}
