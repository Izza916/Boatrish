package com.unity3d.player;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Range;
import android.util.Size;
import android.util.SizeF;
import android.view.Surface;
import com.unity3d.player.a.a;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/* renamed from: com.unity3d.player.s  reason: case insensitive filesystem */
public class C0025s {
    private static CameraManager B;
    private static String[] C;
    /* access modifiers changed from: private */
    public static Semaphore D = new Semaphore(1);
    private final SurfaceTexture.OnFrameAvailableListener A = new C0023q(this);
    /* access modifiers changed from: private */
    public a a = null;
    /* access modifiers changed from: private */
    public CameraDevice b;
    private HandlerThread c;
    private Handler d;
    private Rect e;
    private Rect f;
    private int g;
    private int h;
    private float i = -1.0f;
    private float j = -1.0f;
    private int k;
    private int l;
    private boolean m = false;
    /* access modifiers changed from: private */
    public Range n;
    private ImageReader o = null;
    /* access modifiers changed from: private */
    public Image p;
    /* access modifiers changed from: private */
    public CaptureRequest.Builder q;
    /* access modifiers changed from: private */
    public CameraCaptureSession r = null;
    /* access modifiers changed from: private */
    public Object s = new Object();
    private int t;
    private SurfaceTexture u;
    /* access modifiers changed from: private */
    public Surface v = null;
    private C0024r w = C0024r.STOPPED;
    private CameraCaptureSession.CaptureCallback x = new C0019m(this);
    private final CameraDevice.StateCallback y = new C0021o(this);
    private final ImageReader.OnImageAvailableListener z = new C0022p(this);

    protected C0025s(a aVar) {
        this.a = aVar;
        d();
    }

    public static int a(Context context) {
        return b(context).length;
    }

    public static int a(Context context, int i2) {
        try {
            CameraCharacteristics cameraCharacteristics = c(context).getCameraCharacteristics(b(context)[i2]);
            float[] fArr = (float[]) cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
            SizeF sizeF = (SizeF) cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE);
            if (fArr.length > 0) {
                return (int) ((fArr[0] * 36.0f) / sizeF.getWidth());
            }
        } catch (CameraAccessException e2) {
            C0027u.Log(6, "Camera2: CameraAccessException " + e2);
        }
        return 0;
    }

    /* access modifiers changed from: private */
    public void a(CameraDevice cameraDevice) {
        synchronized (this.s) {
            this.r = null;
        }
        cameraDevice.close();
        this.b = null;
    }

    /* access modifiers changed from: private */
    public void a(Object obj) {
        if (obj == "Focus") {
            this.m = false;
            synchronized (this.s) {
                if (this.r != null) {
                    try {
                        this.q.set(CaptureRequest.CONTROL_AF_TRIGGER, 0);
                        this.q.setTag("Regular");
                        this.r.setRepeatingRequest(this.q.build(), this.x, this.d);
                    } catch (CameraAccessException e2) {
                        C0027u.Log(6, "Camera2: CameraAccessException " + e2);
                    }
                }
            }
        } else if (obj == "Cancel focus") {
            synchronized (this.s) {
                if (this.r != null) {
                    f();
                }
            }
        }
    }

    private static Size[] a(CameraCharacteristics cameraCharacteristics) {
        StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (streamConfigurationMap == null) {
            C0027u.Log(6, "Camera2: configuration map is not available.");
            return null;
        }
        Size[] outputSizes = streamConfigurationMap.getOutputSizes(35);
        if (outputSizes == null || outputSizes.length == 0) {
            return null;
        }
        return outputSizes;
    }

    public static int[] b(Context context, int i2) {
        try {
            Size[] a2 = a(c(context).getCameraCharacteristics(b(context)[i2]));
            if (a2 == null) {
                return null;
            }
            int[] iArr = new int[(a2.length * 2)];
            for (int i3 = 0; i3 < a2.length; i3++) {
                int i4 = i3 * 2;
                iArr[i4] = a2[i3].getWidth();
                iArr[i4 + 1] = a2[i3].getHeight();
            }
            return iArr;
        } catch (CameraAccessException e2) {
            C0027u.Log(6, "Camera2: CameraAccessException " + e2);
            return null;
        }
    }

    private static String[] b(Context context) {
        if (C == null) {
            try {
                C = c(context).getCameraIdList();
            } catch (CameraAccessException e2) {
                C0027u.Log(6, "Camera2: CameraAccessException " + e2);
                C = new String[0];
            }
        }
        return C;
    }

    public static int c(Context context, int i2) {
        try {
            return ((Integer) c(context).getCameraCharacteristics(b(context)[i2]).get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue();
        } catch (CameraAccessException e2) {
            C0027u.Log(6, "Camera2: CameraAccessException " + e2);
            return 0;
        }
    }

    private static CameraManager c(Context context) {
        if (B == null) {
            B = (CameraManager) context.getSystemService("camera");
        }
        return B;
    }

    private void d() {
        HandlerThread handlerThread = new HandlerThread("CameraBackground");
        this.c = handlerThread;
        handlerThread.start();
        this.d = new Handler(this.c.getLooper());
    }

    public static boolean d(Context context, int i2) {
        try {
            return ((Integer) c(context).getCameraCharacteristics(b(context)[i2]).get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF)).intValue() > 0;
        } catch (CameraAccessException e2) {
            C0027u.Log(6, "Camera2: CameraAccessException " + e2);
            return false;
        }
    }

    private void e() {
        try {
            CameraCaptureSession cameraCaptureSession = this.r;
            if (cameraCaptureSession != null) {
                cameraCaptureSession.stopRepeating();
                this.q.set(CaptureRequest.CONTROL_AF_TRIGGER, 2);
                this.q.set(CaptureRequest.CONTROL_AF_MODE, 0);
                this.q.setTag("Cancel focus");
                this.r.capture(this.q.build(), this.x, this.d);
            }
        } catch (CameraAccessException e2) {
            C0027u.Log(6, "Camera2: CameraAccessException " + e2);
        }
    }

    public static boolean e(Context context, int i2) {
        try {
            return ((Integer) c(context).getCameraCharacteristics(b(context)[i2]).get(CameraCharacteristics.LENS_FACING)).intValue() == 0;
        } catch (CameraAccessException e2) {
            C0027u.Log(6, "Camera2: CameraAccessException " + e2);
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void f() {
        try {
            if (this.h != 0) {
                float f2 = this.i;
                if (f2 >= 0.0f && f2 <= 1.0f) {
                    float f3 = this.j;
                    if (f3 >= 0.0f) {
                        if (f3 <= 1.0f) {
                            this.m = true;
                            int width = this.f.width();
                            int i2 = this.k;
                            int height = this.f.height();
                            int i3 = this.l;
                            int i4 = (int) ((((double) (height - (i3 * 2))) * (1.0d - ((double) this.j))) + ((double) i3));
                            int max = Math.max(this.g + 1, Math.min((int) ((((float) (width - (i2 * 2))) * this.i) + ((float) i2)), (this.f.width() - this.g) - 1));
                            int max2 = Math.max(this.g + 1, Math.min(i4, (this.f.height() - this.g) - 1));
                            CaptureRequest.Builder builder = this.q;
                            CaptureRequest.Key key = CaptureRequest.CONTROL_AF_REGIONS;
                            int i5 = this.g;
                            int i6 = i5 * 2;
                            builder.set(key, new MeteringRectangle[]{new MeteringRectangle(max - i5, max2 - i5, i6, i6, 999)});
                            this.q.set(CaptureRequest.CONTROL_AF_MODE, 1);
                            this.q.set(CaptureRequest.CONTROL_AF_TRIGGER, 1);
                            this.q.setTag("Focus");
                            this.r.capture(this.q.build(), this.x, this.d);
                            return;
                        }
                    }
                }
            }
            this.q.set(CaptureRequest.CONTROL_AF_MODE, 4);
            this.q.setTag("Regular");
            CameraCaptureSession cameraCaptureSession = this.r;
            if (cameraCaptureSession != null) {
                cameraCaptureSession.setRepeatingRequest(this.q.build(), this.x, this.d);
            }
        } catch (CameraAccessException e2) {
            C0027u.Log(6, "Camera2: CameraAccessException " + e2);
        }
    }

    public void a() {
        if (this.b != null) {
            h();
            try {
                Semaphore semaphore = D;
                TimeUnit timeUnit = TimeUnit.SECONDS;
                if (!semaphore.tryAcquire(4, timeUnit)) {
                    C0027u.Log(5, "Camera2: Timeout waiting to lock camera for closing.");
                } else {
                    this.b.close();
                    try {
                        if (!D.tryAcquire(4, timeUnit)) {
                            C0027u.Log(5, "Camera2: Timeout waiting to close camera.");
                        }
                    } catch (InterruptedException e2) {
                        C0027u.Log(6, "Camera2: Interrupted while waiting to close camera " + e2);
                    }
                    this.b = null;
                    D.release();
                }
            } catch (InterruptedException e3) {
                C0027u.Log(6, "Camera2: Interrupted while trying to lock camera for closing " + e3);
            }
            this.x = null;
            this.v = null;
            this.u = null;
            Image image = this.p;
            if (image != null) {
                image.close();
                this.p = null;
            }
            ImageReader imageReader = this.o;
            if (imageReader != null) {
                imageReader.close();
                this.o = null;
            }
        }
        this.c.quit();
        try {
            this.c.join(4000);
            this.c = null;
            this.d = null;
        } catch (InterruptedException e4) {
            this.c.interrupt();
            C0027u.Log(6, "Camera2: Interrupted while waiting for the background thread to finish " + e4);
        }
    }

    public boolean a(float f2, float f3) {
        if (this.h <= 0) {
            return false;
        }
        if (!this.m) {
            this.i = f2;
            this.j = f3;
            synchronized (this.s) {
                if (!(this.r == null || this.w == C0024r.PAUSED)) {
                    e();
                }
            }
            return true;
        }
        C0027u.Log(5, "Camera2: Setting manual focus point already started.");
        return false;
    }

    public Rect b() {
        return this.e;
    }

    public void c() {
        synchronized (this.s) {
            CameraCaptureSession cameraCaptureSession = this.r;
            if (cameraCaptureSession != null) {
                try {
                    cameraCaptureSession.stopRepeating();
                    this.w = C0024r.PAUSED;
                } catch (CameraAccessException e2) {
                    C0027u.Log(6, "Camera2: CameraAccessException " + e2);
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0065 A[Catch:{ CameraAccessException -> 0x0096 }] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0079 A[Catch:{ CameraAccessException -> 0x0096 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void g() {
        /*
            r4 = this;
            int r0 = r4.t
            if (r0 == 0) goto L_0x0033
            android.view.Surface r0 = r4.v
            if (r0 != 0) goto L_0x0061
            android.graphics.SurfaceTexture r0 = new android.graphics.SurfaceTexture
            int r1 = r4.t
            r0.<init>(r1)
            r4.u = r0
            android.graphics.Rect r1 = r4.e
            int r1 = r1.width()
            android.graphics.Rect r2 = r4.e
            int r2 = r2.height()
            r0.setDefaultBufferSize(r1, r2)
            android.graphics.SurfaceTexture r0 = r4.u
            android.graphics.SurfaceTexture$OnFrameAvailableListener r1 = r4.A
            android.os.Handler r2 = r4.d
            r0.setOnFrameAvailableListener(r1, r2)
            android.view.Surface r0 = new android.view.Surface
            android.graphics.SurfaceTexture r1 = r4.u
            r0.<init>(r1)
        L_0x0030:
            r4.v = r0
            goto L_0x0061
        L_0x0033:
            android.view.Surface r0 = r4.v
            if (r0 != 0) goto L_0x0061
            android.media.ImageReader r0 = r4.o
            if (r0 != 0) goto L_0x0061
            android.graphics.Rect r0 = r4.e
            int r0 = r0.width()
            android.graphics.Rect r1 = r4.e
            int r1 = r1.height()
            r2 = 35
            r3 = 2
            android.media.ImageReader r0 = android.media.ImageReader.newInstance(r0, r1, r2, r3)
            r4.o = r0
            android.media.ImageReader$OnImageAvailableListener r1 = r4.z
            android.os.Handler r2 = r4.d
            r0.setOnImageAvailableListener(r1, r2)
            r0 = 0
            r4.p = r0
            android.media.ImageReader r0 = r4.o
            android.view.Surface r0 = r0.getSurface()
            goto L_0x0030
        L_0x0061:
            android.hardware.camera2.CameraCaptureSession r0 = r4.r     // Catch:{ CameraAccessException -> 0x0096 }
            if (r0 == 0) goto L_0x0079
            com.unity3d.player.r r1 = r4.w     // Catch:{ CameraAccessException -> 0x0096 }
            com.unity3d.player.r r2 = com.unity3d.player.C0024r.PAUSED     // Catch:{ CameraAccessException -> 0x0096 }
            if (r1 != r2) goto L_0x0091
            android.hardware.camera2.CaptureRequest$Builder r1 = r4.q     // Catch:{ CameraAccessException -> 0x0096 }
            android.hardware.camera2.CaptureRequest r1 = r1.build()     // Catch:{ CameraAccessException -> 0x0096 }
            android.hardware.camera2.CameraCaptureSession$CaptureCallback r2 = r4.x     // Catch:{ CameraAccessException -> 0x0096 }
            android.os.Handler r3 = r4.d     // Catch:{ CameraAccessException -> 0x0096 }
            r0.setRepeatingRequest(r1, r2, r3)     // Catch:{ CameraAccessException -> 0x0096 }
            goto L_0x0091
        L_0x0079:
            android.hardware.camera2.CameraDevice r0 = r4.b     // Catch:{ CameraAccessException -> 0x0096 }
            r1 = 1
            android.view.Surface[] r1 = new android.view.Surface[r1]     // Catch:{ CameraAccessException -> 0x0096 }
            r2 = 0
            android.view.Surface r3 = r4.v     // Catch:{ CameraAccessException -> 0x0096 }
            r1[r2] = r3     // Catch:{ CameraAccessException -> 0x0096 }
            java.util.List r1 = java.util.Arrays.asList(r1)     // Catch:{ CameraAccessException -> 0x0096 }
            com.unity3d.player.n r2 = new com.unity3d.player.n     // Catch:{ CameraAccessException -> 0x0096 }
            r2.<init>(r4)     // Catch:{ CameraAccessException -> 0x0096 }
            android.os.Handler r3 = r4.d     // Catch:{ CameraAccessException -> 0x0096 }
            r0.createCaptureSession(r1, r2, r3)     // Catch:{ CameraAccessException -> 0x0096 }
        L_0x0091:
            com.unity3d.player.r r0 = com.unity3d.player.C0024r.STARTED     // Catch:{ CameraAccessException -> 0x0096 }
            r4.w = r0     // Catch:{ CameraAccessException -> 0x0096 }
            goto L_0x00ac
        L_0x0096:
            r0 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Camera2: CameraAccessException "
            r1.append(r2)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            r1 = 6
            com.unity3d.player.C0027u.Log(r1, r0)
        L_0x00ac:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.C0025s.g():void");
    }

    public void h() {
        synchronized (this.s) {
            CameraCaptureSession cameraCaptureSession = this.r;
            if (cameraCaptureSession != null) {
                try {
                    cameraCaptureSession.abortCaptures();
                } catch (CameraAccessException e2) {
                    C0027u.Log(6, "Camera2: CameraAccessException " + e2);
                }
                this.r.close();
                this.r = null;
                this.w = C0024r.STOPPED;
            }
        }
    }

    public boolean a(Context context, int i2, int i3, int i4, int i5, int i6, Surface surface) {
        int i7 = i5;
        try {
            CameraCharacteristics cameraCharacteristics = B.getCameraCharacteristics(b(context)[i2]);
            if (((Integer) cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)).intValue() == 2) {
                C0027u.Log(5, "Camera2: only LEGACY hardware level is supported.");
            }
            Size[] a2 = a(cameraCharacteristics);
            if (a2 == null || a2.length == 0) {
                return false;
            }
            double d2 = (double) i3;
            double d3 = (double) i4;
            int i8 = 0;
            int i9 = 0;
            int i10 = 0;
            double d4 = Double.MAX_VALUE;
            while (i8 < a2.length) {
                int width = a2[i8].getWidth();
                int height = a2[i8].getHeight();
                CameraCharacteristics cameraCharacteristics2 = cameraCharacteristics;
                double d5 = d2;
                double abs = Math.abs(Math.log(d2 / ((double) width))) + Math.abs(Math.log(d3 / ((double) height)));
                if (abs < d4) {
                    i9 = height;
                    d4 = abs;
                    i10 = width;
                }
                i8++;
                cameraCharacteristics = cameraCharacteristics2;
                d2 = d5;
            }
            this.e = new Rect(0, 0, i10, i9);
            CameraCharacteristics cameraCharacteristics3 = cameraCharacteristics;
            Range[] rangeArr = (Range[]) cameraCharacteristics3.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
            if (rangeArr == null || rangeArr.length == 0) {
                C0027u.Log(6, "Camera2: target FPS ranges are not avialable.");
                return false;
            }
            int i11 = -1;
            int i12 = 0;
            double d6 = Double.MAX_VALUE;
            while (true) {
                if (i12 < rangeArr.length) {
                    int intValue = ((Integer) rangeArr[i12].getLower()).intValue();
                    int intValue2 = ((Integer) rangeArr[i12].getUpper()).intValue();
                    float f2 = (float) i7;
                    if (f2 + 0.1f > ((float) intValue) && f2 - 0.1f < ((float) intValue2)) {
                        break;
                    }
                    double min = (double) ((float) Math.min(Math.abs(i7 - intValue), Math.abs(i7 - intValue2)));
                    if (min < d6) {
                        i11 = i12;
                        d6 = min;
                    }
                    i12++;
                } else {
                    i7 = ((Integer) (i7 > ((Integer) rangeArr[i11].getUpper()).intValue() ? rangeArr[i11].getUpper() : rangeArr[i11].getLower())).intValue();
                }
            }
            this.n = new Range(Integer.valueOf(i7), Integer.valueOf(i7));
            try {
                Semaphore semaphore = D;
                TimeUnit timeUnit = TimeUnit.SECONDS;
                if (!semaphore.tryAcquire(4, timeUnit)) {
                    C0027u.Log(5, "Camera2: Timeout waiting to lock camera for opening.");
                    return false;
                }
                try {
                    B.openCamera(b(context)[i2], this.y, this.d);
                    try {
                        if (!D.tryAcquire(4, timeUnit)) {
                            C0027u.Log(5, "Camera2: Timeout waiting to open camera.");
                            return false;
                        }
                        D.release();
                        this.t = i6;
                        this.v = surface;
                        int intValue3 = ((Integer) cameraCharacteristics3.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF)).intValue();
                        this.h = intValue3;
                        if (intValue3 > 0) {
                            Rect rect = (Rect) cameraCharacteristics3.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
                            this.f = rect;
                            float width2 = ((float) rect.width()) / ((float) this.f.height());
                            float width3 = ((float) this.e.width()) / ((float) this.e.height());
                            if (width3 > width2) {
                                this.k = 0;
                                this.l = (int) ((((float) this.f.height()) - (((float) this.f.width()) / width3)) / 2.0f);
                            } else {
                                this.l = 0;
                                this.k = (int) ((((float) this.f.width()) - (((float) this.f.height()) * width3)) / 2.0f);
                            }
                            this.g = Math.min(this.f.width(), this.f.height()) / 20;
                        }
                        return this.b != null;
                    } catch (InterruptedException e2) {
                        C0027u.Log(6, "Camera2: Interrupted while waiting to open camera " + e2);
                    }
                } catch (CameraAccessException e3) {
                    C0027u.Log(6, "Camera2: CameraAccessException " + e3);
                    D.release();
                    return false;
                }
            } catch (InterruptedException e4) {
                C0027u.Log(6, "Camera2: Interrupted while trying to lock camera for opening " + e4);
                return false;
            }
        } catch (CameraAccessException e5) {
            C0027u.Log(6, "Camera2: CameraAccessException " + e5);
            return false;
        }
    }
}
