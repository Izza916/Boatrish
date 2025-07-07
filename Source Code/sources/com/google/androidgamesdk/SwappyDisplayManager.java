package com.google.androidgamesdk;

import android.app.Activity;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SwappyDisplayManager implements DisplayManager.DisplayListener {
    private final boolean DEBUG = false;
    private final String LOG_TAG = "SwappyDisplayManager";
    private final long ONE_MS_IN_NS = 1000000;
    private final long ONE_S_IN_NS = 1000000000;
    /* access modifiers changed from: private */
    public Activity mActivity;
    private long mCookie;
    private Display.Mode mCurrentMode;
    private b mLooper;
    private WindowManager mWindowManager;

    class a implements Runnable {
        final /* synthetic */ int a;

        a(int i) {
            this.a = i;
        }

        public void run() {
            Window window = SwappyDisplayManager.this.mActivity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.preferredDisplayModeId = this.a;
            window.setAttributes(attributes);
        }
    }

    private class b extends Thread {
        public Handler a;
        private Lock b;
        private Condition c;

        private b(SwappyDisplayManager swappyDisplayManager) {
            ReentrantLock reentrantLock = new ReentrantLock();
            this.b = reentrantLock;
            this.c = reentrantLock.newCondition();
        }

        public void run() {
            Log.i("SwappyDisplayManager", "Starting looper thread");
            this.b.lock();
            Looper.prepare();
            this.a = new Handler();
            this.c.signal();
            this.b.unlock();
            Looper.loop();
            Log.i("SwappyDisplayManager", "Terminating looper thread");
        }

        public void start() {
            this.b.lock();
            super.start();
            try {
                this.c.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.b.unlock();
        }
    }

    public SwappyDisplayManager(long j, Activity activity) {
        String string;
        try {
            Bundle bundle = activity.getPackageManager().getActivityInfo(activity.getIntent().getComponent(), 128).metaData;
            if (!(bundle == null || (string = bundle.getString("android.app.lib_name")) == null)) {
                System.loadLibrary(string);
            }
        } catch (Throwable th) {
            Log.e("SwappyDisplayManager", th.getMessage());
        }
        this.mCookie = j;
        this.mActivity = activity;
        WindowManager windowManager = (WindowManager) activity.getSystemService(WindowManager.class);
        this.mWindowManager = windowManager;
        Display defaultDisplay = windowManager.getDefaultDisplay();
        this.mCurrentMode = defaultDisplay.getMode();
        updateSupportedRefreshRates(defaultDisplay);
        DisplayManager displayManager = (DisplayManager) this.mActivity.getSystemService(DisplayManager.class);
        synchronized (this) {
            b bVar = new b();
            this.mLooper = bVar;
            bVar.start();
            displayManager.registerDisplayListener(this, this.mLooper.a);
        }
    }

    private boolean modeMatchesCurrentResolution(Display.Mode mode) {
        return mode.getPhysicalHeight() == this.mCurrentMode.getPhysicalHeight() && mode.getPhysicalWidth() == this.mCurrentMode.getPhysicalWidth();
    }

    private native void nOnRefreshPeriodChanged(long j, long j2, long j3, long j4);

    private native void nSetSupportedRefreshPeriods(long j, long[] jArr, int[] iArr);

    private void updateSupportedRefreshRates(Display display) {
        Display.Mode[] supportedModes = display.getSupportedModes();
        int i = 0;
        for (Display.Mode modeMatchesCurrentResolution : supportedModes) {
            if (modeMatchesCurrentResolution(modeMatchesCurrentResolution)) {
                i++;
            }
        }
        long[] jArr = new long[i];
        int[] iArr = new int[i];
        int i2 = 0;
        for (int i3 = 0; i3 < supportedModes.length; i3++) {
            if (modeMatchesCurrentResolution(supportedModes[i3])) {
                jArr[i2] = (long) (1.0E9f / supportedModes[i3].getRefreshRate());
                iArr[i2] = supportedModes[i3].getModeId();
                i2++;
            }
        }
        nSetSupportedRefreshPeriods(this.mCookie, jArr, iArr);
    }

    public void onDisplayAdded(int i) {
    }

    public void onDisplayChanged(int i) {
        synchronized (this) {
            Display defaultDisplay = this.mWindowManager.getDefaultDisplay();
            float refreshRate = defaultDisplay.getRefreshRate();
            Display.Mode mode = defaultDisplay.getMode();
            boolean z = true;
            boolean z2 = (mode.getPhysicalWidth() != this.mCurrentMode.getPhysicalWidth()) | (mode.getPhysicalHeight() != this.mCurrentMode.getPhysicalHeight());
            if (refreshRate == this.mCurrentMode.getRefreshRate()) {
                z = false;
            }
            this.mCurrentMode = mode;
            if (z2) {
                updateSupportedRefreshRates(defaultDisplay);
            }
            if (z) {
                long j = (long) (1.0E9f / refreshRate);
                nOnRefreshPeriodChanged(this.mCookie, j, defaultDisplay.getAppVsyncOffsetNanos(), j - (this.mWindowManager.getDefaultDisplay().getPresentationDeadlineNanos() - 1000000));
            }
        }
    }

    public void onDisplayRemoved(int i) {
    }

    public void setPreferredDisplayModeId(int i) {
        this.mActivity.runOnUiThread(new a(i));
    }

    public void terminate() {
        this.mLooper.a.getLooper().quit();
    }
}
