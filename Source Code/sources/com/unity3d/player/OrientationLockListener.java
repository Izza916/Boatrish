package com.unity3d.player;

import android.content.Context;
import android.provider.Settings;

public class OrientationLockListener implements L {
    private N a;
    private Context b;

    OrientationLockListener(Context context) {
        this.b = context;
        this.a = new N(context);
        nativeUpdateOrientationLockState(Settings.System.getInt(context.getContentResolver(), "accelerometer_rotation", 0));
        this.a.a(this, "accelerometer_rotation");
    }

    public void a() {
        this.a.a();
        this.a = null;
    }

    public void a(boolean z) {
        nativeUpdateOrientationLockState(Settings.System.getInt(this.b.getContentResolver(), "accelerometer_rotation", 0));
    }

    public final native void nativeUpdateOrientationLockState(int i);
}
