package com.unity3d.player;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;

/* renamed from: com.unity3d.player.m  reason: case insensitive filesystem */
class C0019m extends CameraCaptureSession.CaptureCallback {
    final /* synthetic */ C0025s a;

    C0019m(C0025s sVar) {
        this.a = sVar;
    }

    public void onCaptureCompleted(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, TotalCaptureResult totalCaptureResult) {
        this.a.a(captureRequest.getTag());
    }

    public void onCaptureFailed(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, CaptureFailure captureFailure) {
        C0027u.Log(5, "Camera2: Capture session failed " + captureRequest.getTag() + " reason " + captureFailure.getReason());
        this.a.a(captureRequest.getTag());
    }

    public void onCaptureSequenceAborted(CameraCaptureSession cameraCaptureSession, int i) {
    }

    public void onCaptureSequenceCompleted(CameraCaptureSession cameraCaptureSession, int i, long j) {
    }
}
