package com.unity3d.player;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.widget.FrameLayout;

class Q extends FrameLayout {
    /* access modifiers changed from: private */
    public C0007a a;
    /* access modifiers changed from: private */
    public UnityPlayer b;
    /* access modifiers changed from: private */
    public C0029w c;

    public Q(Context context, UnityPlayer unityPlayer) {
        super(context);
        int i;
        this.c = new C0029w(context);
        this.b = unityPlayer;
        C0007a aVar = new C0007a(context, unityPlayer);
        this.a = aVar;
        aVar.setId(context.getResources().getIdentifier("unitySurfaceView", "id", context.getPackageName()));
        if (a()) {
            this.a.getHolder().setFormat(-3);
            this.a.setZOrderOnTop(true);
            i = 0;
        } else {
            this.a.getHolder().setFormat(-1);
            i = -16777216;
        }
        setBackgroundColor(i);
        this.a.getHolder().addCallback(new P(this));
        this.a.setFocusable(true);
        this.a.setFocusableInTouchMode(true);
        this.a.setContentDescription(a(context));
        addView(this.a, new FrameLayout.LayoutParams(-1, -1, 17));
    }

    private String a(Context context) {
        return context.getResources().getString(context.getResources().getIdentifier("game_view_content_description", "string", context.getPackageName()));
    }

    private boolean a() {
        Activity activity = UnityPlayer.currentActivity;
        if (activity == null) {
            return false;
        }
        TypedArray obtainStyledAttributes = activity.getTheme().obtainStyledAttributes(new int[]{16842840});
        boolean z = obtainStyledAttributes.getBoolean(0, false);
        obtainStyledAttributes.recycle();
        return z;
    }

    /* access modifiers changed from: package-private */
    public void a(float f) {
        this.a.a(f);
    }

    public void b() {
        C0029w wVar = this.c;
        UnityPlayer unityPlayer = this.b;
        C0028v vVar = wVar.b;
        if (!(vVar == null || vVar.getParent() == null)) {
            unityPlayer.removeView(wVar.b);
        }
        this.c.b = null;
    }

    public boolean c() {
        C0007a aVar = this.a;
        return aVar != null && aVar.a();
    }
}
