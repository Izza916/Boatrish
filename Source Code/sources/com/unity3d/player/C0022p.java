package com.unity3d.player;

import android.media.Image;
import android.media.ImageReader;

/* renamed from: com.unity3d.player.p  reason: case insensitive filesystem */
class C0022p implements ImageReader.OnImageAvailableListener {
    final /* synthetic */ C0025s a;

    C0022p(C0025s sVar) {
        this.a = sVar;
    }

    public void onImageAvailable(ImageReader imageReader) {
        if (C0025s.D.tryAcquire()) {
            Image acquireNextImage = imageReader.acquireNextImage();
            if (acquireNextImage != null) {
                Image.Plane[] planes = acquireNextImage.getPlanes();
                if (acquireNextImage.getFormat() == 35 && planes != null && planes.length == 3) {
                    ((Camera2Wrapper) this.a.a).a(planes[0].getBuffer(), planes[1].getBuffer(), planes[2].getBuffer(), planes[0].getRowStride(), planes[1].getRowStride(), planes[1].getPixelStride());
                } else {
                    C0027u.Log(6, "Camera2: Wrong image format.");
                }
                Image r0 = this.a.p;
                if (r0 != null) {
                    r0.close();
                }
                this.a.p = acquireNextImage;
            }
            C0025s.D.release();
        }
    }
}
