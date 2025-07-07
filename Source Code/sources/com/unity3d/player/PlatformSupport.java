package com.unity3d.player;

import android.os.Build;

public class PlatformSupport {
    static final boolean MARSHMALLOW_SUPPORT;
    static final boolean NOUGAT_SUPPORT;
    static final boolean PIE_SUPPORT;

    static {
        int i = Build.VERSION.SDK_INT;
        boolean z = true;
        MARSHMALLOW_SUPPORT = i >= 23;
        NOUGAT_SUPPORT = i >= 24;
        if (i < 28) {
            z = false;
        }
        PIE_SUPPORT = z;
    }
}
