package com.unity3d.player;

public abstract class NativeLoader {
    static final native boolean load(String str);

    static final native boolean unload();
}
