package com.unity3d.player;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;

/* renamed from: com.unity3d.player.n  reason: case insensitive filesystem */
class C0020n extends CameraCaptureSession.StateCallback {
    final /* synthetic */ C0025s a;

    C0020n(C0025s sVar) {
        this.a = sVar;
    }

    public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
        C0027u.Log(6, "Camera2: CaptureSession configuration failed.");
    }

    public void onConfigured(CameraCaptureSession cameraCaptureSession) {
        StringBuilder sb;
        C0025s sVar = this.a;
        if (sVar.b != null) {
            synchronized (sVar.s) {
                C0025s sVar2 = this.a;
                sVar2.r = cameraCaptureSession;
                try {
                    sVar2.q = sVar2.b.createCaptureRequest(1);
                    C0025s sVar3 = this.a;
                    sVar3.q.addTarget(sVar3.v);
                    C0025s sVar4 = this.a;
                    sVar4.q.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, sVar4.n);
                    this.a.f();
                } catch (CameraAccessException e) {
                    sb = new StringBuilder();
                    sb.append("Camera2: CameraAccessException ");
                    sb.append(e);
                } catch (IllegalStateException e2) {
                    sb = new StringBuilder();
                    sb.append("Camera2: IllegalStateException ");
                    sb.append(e2);
                }
            }
        }
        return;
        C0027u.Log(6, sb.toString());
    }
}
