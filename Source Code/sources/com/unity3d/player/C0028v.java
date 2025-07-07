package com.unity3d.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.PixelCopy;
import android.view.View;

/* renamed from: com.unity3d.player.v  reason: case insensitive filesystem */
class C0028v extends View implements PixelCopy.OnPixelCopyFinishedListener {
    Bitmap a;

    C0028v(C0029w wVar, Context context) {
        super(context);
    }

    public void onPixelCopyFinished(int i) {
        if (i == 0) {
            setBackground(new LayerDrawable(new Drawable[]{new ColorDrawable(-16777216), new BitmapDrawable(getResources(), this.a)}));
        }
    }
}
