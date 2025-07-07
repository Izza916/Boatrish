package com.unity3d.player;

abstract class SoftInputProvider {
    public static F a() {
        int nativeGetSoftInputType = nativeGetSoftInputType();
        for (F f : (F[]) F.c.clone()) {
            if (f.a == nativeGetSoftInputType) {
                return f;
            }
        }
        return F.Undefined;
    }

    private static final native int nativeGetSoftInputType();
}
