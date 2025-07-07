package com.unity3d.player;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.PixelCopy;
import android.view.Surface;
import android.view.SurfaceHolder;

class P implements SurfaceHolder.Callback {
    final /* synthetic */ Q a;

    P(Q q) {
        this.a = q;
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        this.a.b.updateGLDisplay(0, surfaceHolder.getSurface());
        this.a.b.sendSurfaceChangedEvent();
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.a.b.updateGLDisplay(0, surfaceHolder.getSurface());
        Q q = this.a;
        C0029w r0 = q.c;
        UnityPlayer r3 = q.b;
        C0028v vVar = r0.b;
        if (vVar != null && vVar.getParent() == null) {
            r3.addView(r0.b);
            r3.bringChildToFront(r0.b);
        }
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Q q = this.a;
        C0029w r0 = q.c;
        C0007a r5 = q.a;
        r0.getClass();
        if (PlatformSupport.NOUGAT_SUPPORT && r0.a != null) {
            if (r0.b == null) {
                r0.b = new C0028v(r0, r0.a);
            }
            C0028v vVar = r0.b;
            vVar.getClass();
            Bitmap createBitmap = Bitmap.createBitmap(r5.getWidth(), r5.getHeight(), Bitmap.Config.ARGB_8888);
            vVar.a = createBitmap;
            PixelCopy.request(r5, createBitmap, vVar, new Handler(Looper.getMainLooper()));
        }
        this.a.b.updateGLDisplay(0, (Surface) null);
    }
}
