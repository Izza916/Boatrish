package com.unity3d.player;

import android.content.Context;
import android.graphics.Rect;
import android.view.Surface;
import com.unity3d.player.a.a;

public class Camera2Wrapper implements a {
    private Context a;
    private C0025s b = null;

    public Camera2Wrapper(Context context) {
        this.a = context;
        initCamera2Jni();
    }

    private final native void deinitCamera2Jni();

    private final native void initCamera2Jni();

    private final native void nativeFrameReady(Object obj, Object obj2, Object obj3, int i, int i2, int i3);

    private final native void nativeSurfaceTextureReady(Object obj);

    public void a() {
        deinitCamera2Jni();
        closeCamera2();
    }

    public void a(Object obj) {
        nativeSurfaceTextureReady(obj);
    }

    public void a(Object obj, Object obj2, Object obj3, int i, int i2, int i3) {
        nativeFrameReady(obj, obj2, obj3, i, i2, i3);
    }

    /* access modifiers changed from: protected */
    public void closeCamera2() {
        C0025s sVar = this.b;
        if (sVar != null) {
            sVar.a();
        }
        this.b = null;
    }

    /* access modifiers changed from: protected */
    public int getCamera2Count() {
        return C0025s.a(this.a);
    }

    /* access modifiers changed from: protected */
    public int getCamera2FocalLengthEquivalent(int i) {
        return C0025s.a(this.a, i);
    }

    /* access modifiers changed from: protected */
    public int[] getCamera2Resolutions(int i) {
        return C0025s.b(this.a, i);
    }

    /* access modifiers changed from: protected */
    public int getCamera2SensorOrientation(int i) {
        return C0025s.c(this.a, i);
    }

    /* access modifiers changed from: protected */
    public Rect getFrameSizeCamera2() {
        C0025s sVar = this.b;
        return sVar != null ? sVar.b() : new Rect();
    }

    /* access modifiers changed from: protected */
    public boolean initializeCamera2(int i, int i2, int i3, int i4, int i5, Surface surface) {
        if (this.b != null || UnityPlayer.currentActivity == null) {
            return false;
        }
        C0025s sVar = new C0025s(this);
        this.b = sVar;
        return sVar.a(this.a, i, i2, i3, i4, i5, surface);
    }

    /* access modifiers changed from: protected */
    public boolean isCamera2AutoFocusPointSupported(int i) {
        return C0025s.d(this.a, i);
    }

    /* access modifiers changed from: protected */
    public boolean isCamera2FrontFacing(int i) {
        return C0025s.e(this.a, i);
    }

    /* access modifiers changed from: protected */
    public void pauseCamera2() {
        C0025s sVar = this.b;
        if (sVar != null) {
            sVar.c();
        }
    }

    /* access modifiers changed from: protected */
    public boolean setAutoFocusPoint(float f, float f2) {
        C0025s sVar = this.b;
        if (sVar != null) {
            return sVar.a(f, f2);
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void startCamera2() {
        C0025s sVar = this.b;
        if (sVar != null) {
            sVar.g();
        }
    }

    /* access modifiers changed from: protected */
    public void stopCamera2() {
        C0025s sVar = this.b;
        if (sVar != null) {
            sVar.h();
        }
    }
}
