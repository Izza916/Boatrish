package com.unity3d.player;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

class J extends C0032z {
    B g;

    public J(Context context, UnityPlayer unityPlayer) {
        super(context, unityPlayer);
    }

    public void a(boolean z) {
        this.d = z;
        this.g.a(z);
    }

    public void b() {
        this.g.dismiss();
    }

    /* access modifiers changed from: protected */
    public EditText createEditText(C0032z zVar) {
        return new I(this, this.a, zVar);
    }

    public void d() {
        this.g.show();
    }

    /* access modifiers changed from: protected */
    public void reportSoftInputArea() {
        if (this.g.isShowing()) {
            B b = this.g;
            b.getClass();
            Rect rect = new Rect();
            b.b.getWindowVisibleDisplayFrame(rect);
            int[] iArr = new int[2];
            b.b.getLocationOnScreen(iArr);
            Point point = new Point(rect.left - iArr[0], rect.height() - b.c.getHeight());
            Point point2 = new Point();
            b.getWindow().getWindowManager().getDefaultDisplay().getSize(point2);
            int height = b.b.getHeight();
            int i = height - point2.y;
            int i2 = height - point.y;
            if (i2 != i + b.c.getHeight()) {
                b.b.reportSoftInputIsVisible(true);
            } else {
                b.b.reportSoftInputIsVisible(false);
            }
            this.b.reportSoftInputArea(new Rect(point.x, point.y, b.c.getWidth(), i2));
        }
    }

    public void a(String str, int i, boolean z, boolean z2, boolean z3, boolean z4, String str2, int i2, boolean z5, boolean z6) {
        B b = new B(this.a, this.b);
        this.g = b;
        b.d = this;
        Window window = b.getWindow();
        window.requestFeature(1);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = 80;
        attributes.x = 0;
        attributes.y = 0;
        window.setAttributes(attributes);
        window.setBackgroundDrawable(new ColorDrawable(0));
        b.c = b.createSoftInputView(b.d.c);
        window.setLayout(-1, -2);
        window.clearFlags(2);
        window.clearFlags(134217728);
        window.clearFlags(67108864);
        if (!z6) {
            window.addFlags(32);
            window.addFlags(262144);
        }
        b.a(z5);
        b.getWindow().setSoftInputMode(5);
        this.e = z6;
        setupTextInput(str, i, z, z2, z3, z4, str2, i2);
        this.d = z5;
        this.g.a(z5);
        this.b.getViewTreeObserver().addOnGlobalLayoutListener(new G(this));
        this.c.requestFocus();
        this.g.setOnCancelListener(new H(this));
    }
}
