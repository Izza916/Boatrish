package com.unity3d.player;

import android.hardware.camera2.CameraDevice;

/* renamed from: com.unity3d.player.o  reason: case insensitive filesystem */
class C0021o extends CameraDevice.StateCallback {
    final /* synthetic */ C0025s a;

    C0021o(C0025s sVar) {
        this.a = sVar;
    }

    public void onClosed(CameraDevice cameraDevice) {
        C0025s.D.release();
    }

    public void onDisconnected(CameraDevice cameraDevice) {
        C0027u.Log(5, "Camera2: CameraDevice disconnected.");
        this.a.a(cameraDevice);
        C0025s.D.release();
    }

    public void onError(CameraDevice cameraDevice, int i) {
        C0027u.Log(6, "Camera2: Error opeining CameraDevice " + i);
        this.a.a(cameraDevice);
        C0025s.D.release();
    }

    public void onOpened(CameraDevice cameraDevice) {
        this.a.b = cameraDevice;
        C0025s.D.release();
    }
}
