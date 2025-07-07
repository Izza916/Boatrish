package com.unity3d.player;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.unity3d.player.K;
import com.unity3d.player.UnityPermissions;
import com.unity3d.player.c0;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class UnityPlayer extends FrameLayout implements IUnityPlayerLifecycleEvents {
    private static final String ARCORE_ENABLE_METADATA_NAME = "unity.arcore-enable";
    private static final String AUTO_REPORT_FULLY_DRAWN_ENABLE_METADATA_NAME = "unity.auto-report-fully-drawn";
    private static final String LAUNCH_FULLSCREEN = "unity.launch-fullscreen";
    private static final int RUN_STATE_CHANGED_MSG_CODE = 2269;
    private static final String SPLASH_ENABLE_METADATA_NAME = "unity.splash-enable";
    private static final String SPLASH_MODE_METADATA_NAME = "unity.splash-mode";
    public static Activity currentActivity;
    public static Context currentContext;
    /* access modifiers changed from: private */
    public Activity mActivity;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public Q mGlView;
    Handler mHandler;
    /* access modifiers changed from: private */
    public int mInitialScreenOrientation;
    private boolean mIsFullscreen;
    /* access modifiers changed from: private */
    public boolean mMainDisplayOverride;
    /* access modifiers changed from: private */
    public int mNaturalOrientation;
    private OrientationEventListener mOrientationListener;
    private boolean mProcessKillRequested;
    /* access modifiers changed from: private */
    public boolean mQuitting;
    C0032z mSoftInput;
    private S mState;
    /* access modifiers changed from: private */
    public c0 mVideoPlayerProxy;
    private GoogleARCoreApi m_ARCoreApi;
    private boolean m_AddPhoneCallListener;
    private AudioVolumeHandler m_AudioVolumeHandler;
    private Camera2Wrapper m_Camera2Wrapper;
    private ClipboardManager m_ClipboardManager;
    private final ConcurrentLinkedQueue m_Events;
    private B m_FakeListener;
    private HFPStatus m_HFPStatus;
    private int m_IsNoWindowMode;
    F m_MainThread;
    private NetworkConnectivity m_NetworkConnectivity;
    private OrientationLockListener m_OrientationLockListener;
    private D m_PhoneCallListener;
    /* access modifiers changed from: private */
    public K m_SplashScreen;
    private TelephonyManager m_TelephonyManager;
    /* access modifiers changed from: private */
    public IUnityPlayerLifecycleEvents m_UnityPlayerLifecycleEvents;
    Window m_Window;
    private Uri m_launchUri;
    private Configuration prevConfig;

    class A implements Runnable {
        A() {
        }

        public void run() {
            UnityPlayer.this.nativeLowMemory();
        }
    }

    class B implements SensorEventListener {
        B(UnityPlayer unityPlayer) {
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
        }
    }

    enum C {
        GAINED,
        LOST,
        DEFERRED;

        static {
            GAINED = new C("GAINED", 0);
            LOST = new C("LOST", 1);
            DEFERRED = new C("DEFERRED", 2);
        }
    }

    private class D extends PhoneStateListener {
        private D() {
        }

        public void onCallStateChanged(int i, String str) {
            UnityPlayer unityPlayer = UnityPlayer.this;
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            unityPlayer.nativeMuteMasterAudio(z);
        }
    }

    enum E {
        PAUSE,
        RESUME,
        QUIT,
        SURFACE_LOST,
        SURFACE_ACQUIRED,
        FOCUS_LOST,
        FOCUS_GAINED,
        NEXT_FRAME,
        URL_ACTIVATED,
        ORIENTATION_ANGLE_CHANGE;

        static {
            PAUSE = new E("PAUSE", 0);
            RESUME = new E("RESUME", 1);
            QUIT = new E("QUIT", 2);
            SURFACE_LOST = new E("SURFACE_LOST", 3);
            SURFACE_ACQUIRED = new E("SURFACE_ACQUIRED", 4);
            FOCUS_LOST = new E("FOCUS_LOST", 5);
            FOCUS_GAINED = new E("FOCUS_GAINED", 6);
            NEXT_FRAME = new E("NEXT_FRAME", 7);
            URL_ACTIVATED = new E("URL_ACTIVATED", 8);
            ORIENTATION_ANGLE_CHANGE = new E("ORIENTATION_ANGLE_CHANGE", 9);
        }
    }

    private class F extends Thread {
        Handler a;
        boolean b;
        boolean c;
        C d;
        int e;
        int f;
        int g;
        int h;

        class a implements Handler.Callback {
            a() {
            }

            private void a() {
                F f = F.this;
                if (f.d == C.DEFERRED && f.c) {
                    UnityPlayer.this.nativeFocusChanged(true);
                    F.this.d = C.GAINED;
                }
            }

            public boolean handleMessage(Message message) {
                if (message.what != UnityPlayer.RUN_STATE_CHANGED_MSG_CODE) {
                    return false;
                }
                E e = (E) message.obj;
                E e2 = E.NEXT_FRAME;
                if (e == e2) {
                    F f = F.this;
                    f.e--;
                    UnityPlayer.this.executeGLThreadJobs();
                    F f2 = F.this;
                    if (!f2.b) {
                        return true;
                    }
                    if (UnityPlayer.this.getHaveAndroidWindowSupport() && !F.this.c) {
                        return true;
                    }
                    F f3 = F.this;
                    int i = f3.h;
                    if (i >= 0) {
                        if (i == 0) {
                            if (UnityPlayer.this.getSplashEnabled()) {
                                UnityPlayer.this.DisableStaticSplashScreen();
                            }
                            UnityPlayer unityPlayer = UnityPlayer.this;
                            if (unityPlayer.mActivity != null && unityPlayer.getAutoReportFullyDrawnEnabled()) {
                                UnityPlayer.this.mActivity.reportFullyDrawn();
                            }
                        }
                        F.this.h--;
                    }
                    if (!UnityPlayer.this.isFinishing() && !UnityPlayer.this.nativeRender()) {
                        UnityPlayer.this.finish();
                    }
                } else if (e == E.QUIT) {
                    Looper.myLooper().quit();
                } else if (e == E.RESUME) {
                    F.this.b = true;
                } else if (e == E.PAUSE) {
                    F.this.b = false;
                } else if (e == E.SURFACE_LOST) {
                    F.this.c = false;
                } else {
                    if (e == E.SURFACE_ACQUIRED) {
                        F.this.c = true;
                    } else if (e == E.FOCUS_LOST) {
                        F f4 = F.this;
                        if (f4.d == C.GAINED) {
                            UnityPlayer.this.nativeFocusChanged(false);
                        }
                        F.this.d = C.LOST;
                    } else if (e == E.FOCUS_GAINED) {
                        F.this.d = C.DEFERRED;
                    } else if (e == E.URL_ACTIVATED) {
                        UnityPlayer unityPlayer2 = UnityPlayer.this;
                        unityPlayer2.nativeSetLaunchURL(unityPlayer2.getLaunchURL());
                    } else if (e == E.ORIENTATION_ANGLE_CHANGE) {
                        F f5 = F.this;
                        UnityPlayer.this.nativeOrientationChanged(f5.f, f5.g);
                    }
                    a();
                }
                F f6 = F.this;
                if (f6.b && f6.e <= 0) {
                    Message.obtain(f6.a, UnityPlayer.RUN_STATE_CHANGED_MSG_CODE, e2).sendToTarget();
                    F.this.e++;
                }
                return true;
            }
        }

        private F() {
            this.b = false;
            this.c = false;
            this.d = C.LOST;
            this.e = 0;
            this.h = 5;
        }

        public void run() {
            setName("UnityMain");
            Looper.prepare();
            this.a = new Handler(Looper.myLooper(), new a());
            Looper.loop();
        }
    }

    private abstract class G implements Runnable {
        private G() {
        }

        public abstract void a();

        public final void run() {
            if (!UnityPlayer.this.isFinishing()) {
                a();
            }
        }
    }

    public enum SynchronizationTimeout {
        Pause(0),
        SurfaceDetach(1),
        Destroy(2);
        
        private int m_TimeoutMilliseconds;
        final int value;

        private SynchronizationTimeout(int i) {
            this.value = i;
            this.m_TimeoutMilliseconds = 2000;
        }

        public static void setTimeoutForAll(int i) {
            for (SynchronizationTimeout timeout : (SynchronizationTimeout[]) SynchronizationTimeout.class.getEnumConstants()) {
                timeout.setTimeout(i);
            }
        }

        public int getTimeout() {
            return this.m_TimeoutMilliseconds;
        }

        public void setTimeout(int i) {
            this.m_TimeoutMilliseconds = i;
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer$a  reason: case insensitive filesystem */
    class C0000a implements Runnable {
        C0000a() {
        }

        public void run() {
            UnityPlayer.this.nativeResume();
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer$b  reason: case insensitive filesystem */
    class C0001b implements Runnable {
        final /* synthetic */ UnityPlayer a;
        final /* synthetic */ String b;
        final /* synthetic */ int c;
        final /* synthetic */ boolean d;
        final /* synthetic */ boolean e;
        final /* synthetic */ boolean f;
        final /* synthetic */ boolean g;
        final /* synthetic */ String h;
        final /* synthetic */ int i;
        final /* synthetic */ boolean j;
        final /* synthetic */ boolean k;

        /* renamed from: com.unity3d.player.UnityPlayer$b$a */
        class a extends A {
            a() {
            }

            public void a() {
                UnityPlayer.this.nativeSoftInputLostFocus();
                UnityPlayer.this.reportSoftInputStr((String) null, 1, false);
            }
        }

        C0001b(UnityPlayer unityPlayer, String str, int i2, boolean z, boolean z2, boolean z3, boolean z4, String str2, int i3, boolean z5, boolean z6) {
            this.a = unityPlayer;
            this.b = str;
            this.c = i2;
            this.d = z;
            this.e = z2;
            this.f = z3;
            this.g = z4;
            this.h = str2;
            this.i = i3;
            this.j = z5;
            this.k = z6;
        }

        public void run() {
            UnityPlayer unityPlayer = UnityPlayer.this;
            F a2 = SoftInputProvider.a();
            Context r2 = UnityPlayer.this.mContext;
            UnityPlayer unityPlayer2 = this.a;
            String str = this.b;
            int i2 = this.c;
            boolean z = this.d;
            boolean z2 = this.e;
            boolean z3 = this.f;
            boolean z4 = this.g;
            String str2 = this.h;
            int i3 = this.i;
            boolean z5 = this.j;
            boolean z6 = this.k;
            C0032z j2 = a2.ordinal() != 2 ? new J(r2, unityPlayer2) : new E(r2, unityPlayer2);
            j2.a(str, i2, z, z2, z3, z4, str2, i3, z5, z6);
            unityPlayer.mSoftInput = j2;
            C0032z zVar = UnityPlayer.this.mSoftInput;
            zVar.f = new a();
            zVar.d();
            UnityPlayer.this.nativeReportKeyboardConfigChanged();
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer$c  reason: case insensitive filesystem */
    class C0002c implements Runnable {
        C0002c() {
        }

        public void run() {
            Q r0 = UnityPlayer.this.mGlView;
            if (r0 != null) {
                r0.b();
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer$d  reason: case insensitive filesystem */
    class C0003d implements Runnable {
        C0003d() {
        }

        public void run() {
            UnityPlayer.this.reportSoftInputArea(new Rect());
            UnityPlayer.this.reportSoftInputIsVisible(false);
            C0032z zVar = UnityPlayer.this.mSoftInput;
            if (zVar != null) {
                zVar.b();
                UnityPlayer unityPlayer = UnityPlayer.this;
                unityPlayer.mSoftInput = null;
                unityPlayer.nativeReportKeyboardConfigChanged();
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer$e  reason: case insensitive filesystem */
    class C0004e implements Runnable {
        final /* synthetic */ String a;

        C0004e(String str) {
            this.a = str;
        }

        public void run() {
            String str;
            EditText editText;
            C0032z zVar = UnityPlayer.this.mSoftInput;
            if (zVar != null && (str = this.a) != null && (editText = zVar.c) != null) {
                editText.setText(str);
                zVar.c.setSelection(str.length());
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer$f  reason: case insensitive filesystem */
    class C0005f implements Runnable {
        final /* synthetic */ int a;

        C0005f(int i) {
            this.a = i;
        }

        public void run() {
            C0032z zVar = UnityPlayer.this.mSoftInput;
            if (zVar != null) {
                int i = this.a;
                EditText editText = zVar.c;
                if (editText == null) {
                    return;
                }
                if (i > 0) {
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(i)});
                    return;
                }
                editText.setFilters(new InputFilter[0]);
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer$g  reason: case insensitive filesystem */
    class C0006g implements Runnable {
        final /* synthetic */ boolean a;

        C0006g(boolean z) {
            this.a = z;
        }

        public void run() {
            C0032z zVar = UnityPlayer.this.mSoftInput;
            if (zVar != null) {
                zVar.a(this.a);
            }
        }
    }

    class h implements Runnable {
        final /* synthetic */ int a;
        final /* synthetic */ int b;

        h(int i, int i2) {
            this.a = i;
            this.b = i2;
        }

        public void run() {
            int i;
            C0032z zVar = UnityPlayer.this.mSoftInput;
            if (zVar != null) {
                int i2 = this.a;
                int i3 = this.b;
                EditText editText = zVar.c;
                if (editText != null && editText.getText().length() >= (i = i3 + i2)) {
                    zVar.c.setSelection(i2, i);
                }
            }
        }
    }

    class i extends G {
        final /* synthetic */ boolean b;
        final /* synthetic */ String c;
        final /* synthetic */ int d;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        i(boolean z, String str, int i) {
            super();
            this.b = z;
            this.c = str;
            this.d = i;
        }

        public void a() {
            if (this.b) {
                UnityPlayer.this.nativeSoftInputCanceled();
            } else {
                String str = this.c;
                if (str != null) {
                    UnityPlayer.this.nativeSetInputString(str);
                }
            }
            if (this.d == 1) {
                UnityPlayer.this.nativeSoftInputClosed();
            }
        }
    }

    class j extends G {
        final /* synthetic */ int b;
        final /* synthetic */ int c;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        j(int i, int i2) {
            super();
            this.b = i;
            this.c = i2;
        }

        public void a() {
            UnityPlayer.this.nativeSetInputSelection(this.b, this.c);
        }
    }

    class k implements DialogInterface.OnClickListener {
        k() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            UnityPlayer.this.finish();
        }
    }

    class l extends G {
        final /* synthetic */ Rect b;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        l(Rect rect) {
            super();
            this.b = rect;
        }

        public void a() {
            UnityPlayer unityPlayer = UnityPlayer.this;
            Rect rect = this.b;
            unityPlayer.nativeSetInputArea(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    class m extends G {
        final /* synthetic */ boolean b;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        m(boolean z) {
            super();
            this.b = z;
        }

        public void a() {
            UnityPlayer.this.nativeSetKeyboardIsVisible(this.b);
        }
    }

    class n implements c0.a {
        n() {
        }

        public void a() {
            UnityPlayer.this.mVideoPlayerProxy = null;
        }
    }

    class o implements Runnable {
        o() {
        }

        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0008, code lost:
            r0 = r2.a;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r2 = this;
                com.unity3d.player.UnityPlayer r0 = com.unity3d.player.UnityPlayer.this
                boolean r0 = r0.nativeIsAutorotationOn()
                if (r0 == 0) goto L_0x0017
                com.unity3d.player.UnityPlayer r0 = com.unity3d.player.UnityPlayer.this
                android.app.Activity r1 = r0.mActivity
                if (r1 == 0) goto L_0x0017
                int r0 = r0.mInitialScreenOrientation
                r1.setRequestedOrientation(r0)
            L_0x0017:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.UnityPlayer.o.run():void");
        }
    }

    class p implements Runnable {
        p() {
        }

        public void run() {
            UnityPlayer.this.setupUnityToBePaused();
            UnityPlayer.this.windowFocusChanged(false);
            UnityPlayer.this.m_UnityPlayerLifecycleEvents.onUnityPlayerUnloaded();
        }
    }

    class q extends OrientationEventListener {
        q(Context context, int i) {
            super(context, i);
        }

        public void onOrientationChanged(int i) {
            UnityPlayer unityPlayer = UnityPlayer.this;
            F f = unityPlayer.m_MainThread;
            f.f = unityPlayer.mNaturalOrientation;
            f.g = i;
            E e = E.ORIENTATION_ANGLE_CHANGE;
            Handler handler = f.a;
            if (handler != null) {
                Message.obtain(handler, UnityPlayer.RUN_STATE_CHANGED_MSG_CODE, e).sendToTarget();
            }
        }
    }

    class r implements Runnable {
        final /* synthetic */ float a;

        r(float f) {
            this.a = f;
        }

        public void run() {
            UnityPlayer.this.mGlView.a(this.a);
        }
    }

    class s implements Runnable {
        final /* synthetic */ float a;

        s(float f) {
            this.a = f;
        }

        public void run() {
            WindowManager.LayoutParams attributes = UnityPlayer.this.m_Window.getAttributes();
            attributes.screenBrightness = this.a;
            UnityPlayer.this.m_Window.setAttributes(attributes);
        }
    }

    class t implements Runnable {
        t() {
        }

        public void run() {
            UnityPlayer unityPlayer = UnityPlayer.this;
            unityPlayer.removeView(unityPlayer.m_SplashScreen);
            UnityPlayer.this.m_SplashScreen = null;
        }
    }

    class u implements Runnable {
        u() {
        }

        public void run() {
            UnityPlayer.this.nativeSendSurfaceChangedEvent();
        }
    }

    class v implements Runnable {
        final /* synthetic */ int a;
        final /* synthetic */ Surface b;
        final /* synthetic */ Semaphore c;

        v(int i, Surface surface, Semaphore semaphore) {
            this.a = i;
            this.b = surface;
            this.c = semaphore;
        }

        public void run() {
            UnityPlayer.this.nativeRecreateGfxState(this.a, this.b);
            this.c.release();
        }
    }

    class w implements Runnable {
        w() {
        }

        public void run() {
            UnityPlayer unityPlayer = UnityPlayer.this;
            if (unityPlayer.mMainDisplayOverride) {
                unityPlayer.removeView(unityPlayer.mGlView);
            } else if (unityPlayer.mGlView.getParent() == null) {
                UnityPlayer unityPlayer2 = UnityPlayer.this;
                unityPlayer2.addView(unityPlayer2.mGlView);
            } else {
                C0027u.Log(5, "Couldn't add view, because it's already assigned to another parent");
            }
        }
    }

    class x implements Runnable {
        x() {
        }

        public void run() {
            UnityPlayer.this.destroy();
        }
    }

    class y implements Runnable {
        final /* synthetic */ Semaphore a;

        y(Semaphore semaphore) {
            this.a = semaphore;
        }

        public void run() {
            UnityPlayer.this.shutdown();
            this.a.release();
        }
    }

    class z implements Runnable {
        final /* synthetic */ Semaphore a;

        z(Semaphore semaphore) {
            this.a = semaphore;
        }

        public void run() {
            if (UnityPlayer.this.nativePause()) {
                UnityPlayer unityPlayer = UnityPlayer.this;
                unityPlayer.mQuitting = true;
                unityPlayer.shutdown();
                UnityPlayer.this.queueDestroy();
            }
            this.a.release();
        }
    }

    static {
        new O().a();
    }

    public UnityPlayer(Context context) {
        this(context, (IUnityPlayerLifecycleEvents) null);
    }

    public UnityPlayer(Context context, IUnityPlayerLifecycleEvents iUnityPlayerLifecycleEvents) {
        super(context);
        this.mHandler = new Handler();
        this.mInitialScreenOrientation = -1;
        this.mMainDisplayOverride = false;
        this.mIsFullscreen = true;
        this.mState = new S();
        this.m_Events = new ConcurrentLinkedQueue();
        this.mOrientationListener = null;
        this.m_MainThread = new F();
        this.m_AddPhoneCallListener = false;
        this.m_PhoneCallListener = new D();
        this.m_ARCoreApi = null;
        this.m_FakeListener = new B(this);
        this.m_Camera2Wrapper = null;
        this.m_HFPStatus = null;
        this.m_AudioVolumeHandler = null;
        this.m_OrientationLockListener = null;
        this.m_launchUri = null;
        this.m_NetworkConnectivity = null;
        this.m_UnityPlayerLifecycleEvents = null;
        this.m_IsNoWindowMode = -1;
        this.mProcessKillRequested = true;
        this.mSoftInput = null;
        this.m_UnityPlayerLifecycleEvents = iUnityPlayerLifecycleEvents == null ? this : iUnityPlayerLifecycleEvents;
        O.a(getUnityNativeLibraryPath(context));
        currentContext = context;
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            this.mActivity = activity;
            currentActivity = activity;
            this.mInitialScreenOrientation = activity.getRequestedOrientation();
            this.m_launchUri = this.mActivity.getIntent().getData();
        }
        this.mContext = context;
        EarlyEnableFullScreenIfEnabled();
        Configuration configuration = getResources().getConfiguration();
        this.prevConfig = configuration;
        this.mNaturalOrientation = getNaturalOrientation(configuration.orientation);
        if (this.mActivity != null && getSplashEnabled()) {
            K k2 = new K(this.mContext, K.a.a()[getSplashMode()]);
            this.m_SplashScreen = k2;
            addView(k2);
        }
        preloadJavaPlugins();
        String loadNative = loadNative(getUnityNativeLibraryPath(this.mContext));
        if (!S.d()) {
            C0027u.Log(6, "Your hardware does not support this application.");
            AlertDialog.Builder positiveButton = new AlertDialog.Builder(this.mContext).setTitle("Failure to initialize!").setPositiveButton("OK", new k());
            AlertDialog create = positiveButton.setMessage("Your hardware does not support this application." + "\n\n" + loadNative + "\n\n Press OK to quit.").create();
            create.setCancelable(false);
            create.show();
            return;
        }
        initJni(context);
        this.mState.d(true);
        Q q2 = new Q(context, this);
        this.mGlView = q2;
        addView(q2);
        bringChildToFront(this.m_SplashScreen);
        this.mQuitting = false;
        Activity activity2 = this.mActivity;
        if (activity2 != null) {
            this.m_Window = activity2.getWindow();
        }
        hideStatusBar();
        this.m_TelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.m_ClipboardManager = (ClipboardManager) this.mContext.getSystemService("clipboard");
        this.m_Camera2Wrapper = new Camera2Wrapper(this.mContext);
        this.m_HFPStatus = new HFPStatus(this.mContext);
        this.m_MainThread.start();
    }

    /* access modifiers changed from: private */
    public void DisableStaticSplashScreen() {
        runOnUiThread(new t());
    }

    private void EarlyEnableFullScreenIfEnabled() {
        View decorView;
        Activity activity = this.mActivity;
        boolean z2 = false;
        if (!(activity == null || activity.getWindow() == null || ((!getLaunchFullscreen() && !this.mActivity.getIntent().getBooleanExtra("android.intent.extra.VR_LAUNCH", false)) || (decorView = this.mActivity.getWindow().getDecorView()) == null))) {
            decorView.setSystemUiVisibility(7);
        }
        Activity activity2 = this.mActivity;
        if (activity2 != null && activity2.getWindow() != null && PlatformSupport.PIE_SUPPORT) {
            WindowManager.LayoutParams attributes = activity2.getWindow().getAttributes();
            try {
                ApplicationInfo applicationInfo = activity2.getPackageManager().getApplicationInfo(activity2.getPackageName(), 128);
                if (applicationInfo != null) {
                    z2 = applicationInfo.metaData.getBoolean("unity.render-outside-safearea");
                }
            } catch (Exception unused) {
            }
            attributes.layoutInDisplayCutoutMode = z2 ? 1 : 0;
        }
    }

    private String GetGlViewContentDescription(Context context) {
        return context.getResources().getString(context.getResources().getIdentifier("game_view_content_description", "string", context.getPackageName()));
    }

    private boolean IsWindowTranslucent() {
        Activity activity = this.mActivity;
        if (activity == null) {
            return false;
        }
        TypedArray obtainStyledAttributes = activity.getTheme().obtainStyledAttributes(new int[]{16842840});
        boolean z2 = obtainStyledAttributes.getBoolean(0, false);
        obtainStyledAttributes.recycle();
        return z2;
    }

    public static void UnitySendMessage(String str, String str2, String str3) {
        if (!S.d()) {
            C0027u.Log(5, "Native libraries not loaded - dropping message for " + str + "." + str2);
            return;
        }
        try {
            nativeUnitySendMessage(str, str2, str3.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException unused) {
        }
    }

    private void checkResumePlayer() {
        Activity activity = this.mActivity;
        if (this.mState.a(activity != null ? MultiWindowSupport.isInMultiWindowMode(activity) : false)) {
            this.mState.c(true);
            queueGLThreadEvent((Runnable) new C0000a());
            F f = this.m_MainThread;
            E e = E.RESUME;
            Handler handler = f.a;
            if (handler != null) {
                Message.obtain(handler, RUN_STATE_CHANGED_MSG_CODE, e).sendToTarget();
            }
        }
    }

    /* access modifiers changed from: private */
    public void finish() {
        Activity activity = this.mActivity;
        if (activity != null && !activity.isFinishing()) {
            this.mActivity.finish();
        }
    }

    private boolean getARCoreEnabled() {
        try {
            return getApplicationInfo().metaData.getBoolean(ARCORE_ENABLE_METADATA_NAME);
        } catch (Exception unused) {
            return false;
        }
    }

    private ActivityInfo getActivityInfo() {
        return this.mActivity.getPackageManager().getActivityInfo(this.mActivity.getComponentName(), 128);
    }

    private ApplicationInfo getApplicationInfo() {
        return this.mContext.getPackageManager().getApplicationInfo(this.mContext.getPackageName(), 128);
    }

    /* access modifiers changed from: private */
    public boolean getAutoReportFullyDrawnEnabled() {
        try {
            return getApplicationInfo().metaData.getBoolean(AUTO_REPORT_FULLY_DRAWN_ENABLE_METADATA_NAME);
        } catch (Exception unused) {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public boolean getHaveAndroidWindowSupport() {
        if (this.m_IsNoWindowMode == -1) {
            this.m_IsNoWindowMode = nativeGetNoWindowMode() ? 1 : 0;
        }
        return this.m_IsNoWindowMode == 1;
    }

    private boolean getLaunchFullscreen() {
        try {
            return getApplicationInfo().metaData.getBoolean(LAUNCH_FULLSCREEN);
        } catch (Exception unused) {
            return false;
        }
    }

    private int getNaturalOrientation(int i2) {
        int rotation = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getRotation();
        if ((rotation == 0 || rotation == 2) && i2 == 2) {
            return 0;
        }
        return ((rotation == 1 || rotation == 3) && i2 == 1) ? 0 : 1;
    }

    private String getProcessName() {
        int myPid = Process.myPid();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) this.mContext.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo next : runningAppProcesses) {
            if (next.pid == myPid) {
                return next.processName;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public boolean getSplashEnabled() {
        try {
            return getApplicationInfo().metaData.getBoolean(SPLASH_ENABLE_METADATA_NAME);
        } catch (Exception unused) {
            return false;
        }
    }

    private static String getUnityNativeLibraryPath(Context context) {
        return context.getApplicationInfo().nativeLibraryDir;
    }

    private void hidePreservedContent() {
        runOnUiThread(new C0002c());
    }

    private void hideStatusBar() {
        Activity activity = this.mActivity;
        if (activity != null) {
            activity.getWindow().setFlags(1024, 1024);
        }
    }

    private final native void initJni(Context context);

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0037, code lost:
        return logLoadLibMainError(r0, r2.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0015, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
        java.lang.System.loadLibrary("main");
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:3:0x0015 A[ExcHandler: SecurityException (r2v7 'e' java.lang.SecurityException A[CUSTOM_DECLARE]), Splitter:B:1:0x0011] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String loadNative(java.lang.String r2) {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r2)
            java.lang.String r1 = "/libmain.so"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.System.load(r0)     // Catch:{ UnsatisfiedLinkError -> 0x0017, SecurityException -> 0x0015 }
            goto L_0x001c
        L_0x0015:
            r2 = move-exception
            goto L_0x002f
        L_0x0017:
            java.lang.String r1 = "main"
            java.lang.System.loadLibrary(r1)     // Catch:{ UnsatisfiedLinkError -> 0x0038, SecurityException -> 0x0015 }
        L_0x001c:
            boolean r2 = com.unity3d.player.NativeLoader.load(r2)
            if (r2 == 0) goto L_0x0028
            com.unity3d.player.S.e()
            java.lang.String r2 = ""
            return r2
        L_0x0028:
            r2 = 6
            java.lang.String r0 = "NativeLoader.load failure, Unity libraries were not loaded."
            com.unity3d.player.C0027u.Log(r2, r0)
            return r0
        L_0x002f:
            java.lang.String r2 = r2.toString()
            java.lang.String r2 = logLoadLibMainError(r0, r2)
            return r2
        L_0x0038:
            r2 = move-exception
            java.lang.String r2 = r2.toString()
            java.lang.String r2 = logLoadLibMainError(r0, r2)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.UnityPlayer.loadNative(java.lang.String):java.lang.String");
    }

    private static String logLoadLibMainError(String str, String str2) {
        String str3 = "Failed to load 'libmain.so'\n\n" + str2;
        C0027u.Log(6, str3);
        return str3;
    }

    private final native void nativeApplicationUnload();

    private final native boolean nativeDone();

    /* access modifiers changed from: private */
    public final native void nativeFocusChanged(boolean z2);

    private final native boolean nativeGetNoWindowMode();

    private final native void nativeHidePreservedContent();

    private final native boolean nativeInjectEvent(InputEvent inputEvent);

    /* access modifiers changed from: private */
    public final native boolean nativeIsAutorotationOn();

    /* access modifiers changed from: private */
    public final native void nativeLowMemory();

    /* access modifiers changed from: private */
    public final native void nativeMuteMasterAudio(boolean z2);

    /* access modifiers changed from: private */
    public final native void nativeOrientationChanged(int i2, int i3);

    /* access modifiers changed from: private */
    public final native boolean nativePause();

    /* access modifiers changed from: private */
    public final native void nativeRecreateGfxState(int i2, Surface surface);

    /* access modifiers changed from: private */
    public final native boolean nativeRender();

    /* access modifiers changed from: private */
    public final native void nativeReportKeyboardConfigChanged();

    private final native void nativeRestartActivityIndicator();

    /* access modifiers changed from: private */
    public final native void nativeResume();

    /* access modifiers changed from: private */
    public final native void nativeSendSurfaceChangedEvent();

    /* access modifiers changed from: private */
    public final native void nativeSetInputArea(int i2, int i3, int i4, int i5);

    /* access modifiers changed from: private */
    public final native void nativeSetInputSelection(int i2, int i3);

    /* access modifiers changed from: private */
    public final native void nativeSetInputString(String str);

    /* access modifiers changed from: private */
    public final native void nativeSetKeyboardIsVisible(boolean z2);

    /* access modifiers changed from: private */
    public final native void nativeSetLaunchURL(String str);

    /* access modifiers changed from: private */
    public final native void nativeSoftInputCanceled();

    /* access modifiers changed from: private */
    public final native void nativeSoftInputClosed();

    /* access modifiers changed from: private */
    public final native void nativeSoftInputLostFocus();

    private static native void nativeUnitySendMessage(String str, String str2, byte[] bArr);

    private void pauseUnity() {
        reportSoftInputStr((String) null, 1, true);
        if (this.mState.c() && !this.mState.b()) {
            if (S.d()) {
                Semaphore semaphore = new Semaphore(0);
                Runnable yVar = isFinishing() ? new y(semaphore) : new z(semaphore);
                F f = this.m_MainThread;
                Handler handler = f.a;
                if (handler != null) {
                    Message.obtain(handler, RUN_STATE_CHANGED_MSG_CODE, E.PAUSE).sendToTarget();
                    Message.obtain(f.a, yVar).sendToTarget();
                }
                try {
                    SynchronizationTimeout synchronizationTimeout = SynchronizationTimeout.Pause;
                    if (!semaphore.tryAcquire((long) synchronizationTimeout.getTimeout(), TimeUnit.MILLISECONDS)) {
                        C0027u.Log(5, "Timeout (" + synchronizationTimeout.getTimeout() + " ms) while trying to pause the Unity Engine.");
                    }
                } catch (InterruptedException unused) {
                    C0027u.Log(5, "UI thread got interrupted while trying to pause the Unity Engine.");
                }
            }
            this.mState.c(false);
            this.mState.e(true);
            if (this.m_AddPhoneCallListener) {
                this.m_TelephonyManager.listen(this.m_PhoneCallListener, 0);
            }
        }
    }

    private static void preloadJavaPlugins() {
        try {
            Class.forName("com.unity3d.JavaPluginPreloader");
        } catch (ClassNotFoundException unused) {
        } catch (LinkageError e) {
            C0027u.Log(6, "Java class preloading failed: " + e.getMessage());
        }
    }

    /* access modifiers changed from: private */
    public void queueDestroy() {
        C0027u.Log(4, "Queue Destroy");
        runOnUiThread(new x());
    }

    private void queueGLThreadEvent(G g) {
        if (!isFinishing()) {
            queueGLThreadEvent((Runnable) g);
        }
    }

    /* access modifiers changed from: private */
    public void shutdown() {
        this.mProcessKillRequested = nativeDone();
        this.mState.d(false);
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [android.view.ViewParent] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void swapViews(android.view.View r5, android.view.View r6) {
        /*
            r4 = this;
            com.unity3d.player.S r0 = r4.mState
            boolean r0 = r0.b()
            r1 = 0
            if (r0 != 0) goto L_0x000e
            r4.setupUnityToBePaused()
            r0 = 1
            goto L_0x000f
        L_0x000e:
            r0 = r1
        L_0x000f:
            if (r5 == 0) goto L_0x0030
            android.view.ViewParent r2 = r5.getParent()
            boolean r3 = r2 instanceof com.unity3d.player.UnityPlayer
            if (r3 == 0) goto L_0x001e
            r3 = r2
            com.unity3d.player.UnityPlayer r3 = (com.unity3d.player.UnityPlayer) r3
            if (r3 == r4) goto L_0x0030
        L_0x001e:
            boolean r3 = r2 instanceof android.view.ViewGroup
            if (r3 == 0) goto L_0x0027
            android.view.ViewGroup r2 = (android.view.ViewGroup) r2
            r2.removeView(r5)
        L_0x0027:
            r4.addView(r5)
            r4.bringChildToFront(r5)
            r5.setVisibility(r1)
        L_0x0030:
            if (r6 == 0) goto L_0x0040
            android.view.ViewParent r5 = r6.getParent()
            if (r5 != r4) goto L_0x0040
            r5 = 8
            r6.setVisibility(r5)
            r4.removeView(r6)
        L_0x0040:
            if (r0 == 0) goto L_0x0045
            r4.setupUnityToBeResumed()
        L_0x0045:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.UnityPlayer.swapViews(android.view.View, android.view.View):void");
    }

    private static void unloadNative() {
        if (S.d()) {
            if (NativeLoader.unload()) {
                S.f();
                return;
            }
            throw new UnsatisfiedLinkError("Unable to unload libraries from libmain.so");
        }
    }

    private boolean updateDisplayInternal(int i2, Surface surface) {
        if (!S.d() || !this.mState.a()) {
            return false;
        }
        Semaphore semaphore = new Semaphore(0);
        v vVar = new v(i2, surface, semaphore);
        if (i2 != 0) {
            vVar.run();
        } else if (surface == null) {
            F f = this.m_MainThread;
            Handler handler = f.a;
            if (handler != null) {
                Message.obtain(handler, RUN_STATE_CHANGED_MSG_CODE, E.SURFACE_LOST).sendToTarget();
                Message.obtain(f.a, vVar).sendToTarget();
            }
        } else {
            F f2 = this.m_MainThread;
            Handler handler2 = f2.a;
            if (handler2 != null) {
                Message.obtain(handler2, vVar).sendToTarget();
                E e = E.SURFACE_ACQUIRED;
                Handler handler3 = f2.a;
                if (handler3 != null) {
                    Message.obtain(handler3, RUN_STATE_CHANGED_MSG_CODE, e).sendToTarget();
                }
            }
        }
        if (surface != null || i2 != 0) {
            return true;
        }
        try {
            SynchronizationTimeout synchronizationTimeout = SynchronizationTimeout.SurfaceDetach;
            if (semaphore.tryAcquire((long) synchronizationTimeout.getTimeout(), TimeUnit.MILLISECONDS)) {
                return true;
            }
            C0027u.Log(5, "Timeout (" + synchronizationTimeout.getTimeout() + " ms) while trying detaching primary window.");
            return true;
        } catch (InterruptedException unused) {
            C0027u.Log(5, "UI thread got interrupted while trying to detach the primary window from the Unity Engine.");
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void addPhoneCallListener() {
        this.m_AddPhoneCallListener = true;
        this.m_TelephonyManager.listen(this.m_PhoneCallListener, 32);
    }

    public boolean addViewToPlayer(View view, boolean z2) {
        swapViews(view, z2 ? this.mGlView : null);
        boolean z3 = true;
        boolean z4 = view.getParent() == this;
        boolean z5 = z2 && this.mGlView.getParent() == null;
        boolean z6 = this.mGlView.getParent() == this;
        if (!z4 || (!z5 && !z6)) {
            z3 = false;
        }
        if (!z3) {
            if (!z4) {
                C0027u.Log(6, "addViewToPlayer: Failure adding view to hierarchy");
            }
            if (!z5 && !z6) {
                C0027u.Log(6, "addViewToPlayer: Failure removing old view from hierarchy");
            }
        }
        return z3;
    }

    public void configurationChanged(Configuration configuration) {
        int diff = this.prevConfig.diff(configuration);
        if (!((diff & 256) == 0 && (diff & 1024) == 0 && (diff & 2048) == 0 && (diff & 128) == 0)) {
            nativeHidePreservedContent();
        }
        this.prevConfig = new Configuration(configuration);
        c0 c0Var = this.mVideoPlayerProxy;
        if (c0Var != null) {
            c0Var.b();
        }
    }

    public void destroy() {
        Camera2Wrapper camera2Wrapper = this.m_Camera2Wrapper;
        if (camera2Wrapper != null) {
            camera2Wrapper.a();
            this.m_Camera2Wrapper = null;
        }
        HFPStatus hFPStatus = this.m_HFPStatus;
        if (hFPStatus != null) {
            hFPStatus.a();
            this.m_HFPStatus = null;
        }
        NetworkConnectivity networkConnectivity = this.m_NetworkConnectivity;
        if (networkConnectivity != null) {
            networkConnectivity.a();
            this.m_NetworkConnectivity = null;
        }
        this.mQuitting = true;
        if (!this.mState.b()) {
            setupUnityToBePaused();
        }
        F f = this.m_MainThread;
        E e = E.QUIT;
        Handler handler = f.a;
        if (handler != null) {
            Message.obtain(handler, RUN_STATE_CHANGED_MSG_CODE, e).sendToTarget();
        }
        try {
            this.m_MainThread.join((long) SynchronizationTimeout.Destroy.getTimeout());
        } catch (InterruptedException unused) {
            this.m_MainThread.interrupt();
        }
        if (S.d()) {
            removeAllViews();
        }
        if (this.mProcessKillRequested) {
            this.m_UnityPlayerLifecycleEvents.onUnityPlayerQuitted();
            kill();
        }
        unloadNative();
    }

    /* access modifiers changed from: protected */
    public void disableLogger() {
        C0027u.a = true;
    }

    public boolean displayChanged(int i2, Surface surface) {
        if (i2 == 0) {
            this.mMainDisplayOverride = surface != null;
            runOnUiThread(new w());
        }
        return updateDisplayInternal(i2, surface);
    }

    /* access modifiers changed from: protected */
    public void executeGLThreadJobs() {
        while (true) {
            Runnable runnable = (Runnable) this.m_Events.poll();
            if (runnable != null) {
                runnable.run();
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public String getClipboardText() {
        ClipData primaryClip = this.m_ClipboardManager.getPrimaryClip();
        return primaryClip != null ? primaryClip.getItemAt(0).coerceToText(this.mContext).toString() : "";
    }

    /* access modifiers changed from: protected */
    public String getKeyboardLayout() {
        InputMethodSubtype currentInputMethodSubtype;
        C0032z zVar = this.mSoftInput;
        if (zVar == null || (currentInputMethodSubtype = ((InputMethodManager) zVar.a.getSystemService("input_method")).getCurrentInputMethodSubtype()) == null) {
            return null;
        }
        String locale = currentInputMethodSubtype.getLocale();
        if (locale != null && !locale.equals("")) {
            return locale;
        }
        String mode = currentInputMethodSubtype.getMode();
        String extraValue = currentInputMethodSubtype.getExtraValue();
        return mode + " " + extraValue;
    }

    /* access modifiers changed from: protected */
    public String getLaunchURL() {
        Uri uri = this.m_launchUri;
        if (uri != null) {
            return uri.toString();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public int getNetworkConnectivity() {
        NetworkConnectivity networkConnectivity = this.m_NetworkConnectivity;
        if (networkConnectivity != null) {
            return networkConnectivity.b();
        }
        this.m_NetworkConnectivity = PlatformSupport.NOUGAT_SUPPORT ? new NetworkConnectivityNougat(this.mContext) : new NetworkConnectivity(this.mContext);
        return this.m_NetworkConnectivity.b();
    }

    public String getNetworkProxySettings(String str) {
        String str2;
        String str3;
        if (str.startsWith("http:")) {
            str2 = "http.proxyHost";
            str3 = "http.proxyPort";
        } else {
            if (str.startsWith("https:")) {
                str2 = "https.proxyHost";
                str3 = "https.proxyPort";
            }
            return null;
        }
        String property = System.getProperties().getProperty(str2);
        if (property != null && !"".equals(property)) {
            StringBuilder sb = new StringBuilder(property);
            String property2 = System.getProperties().getProperty(str3);
            if (property2 != null && !"".equals(property2)) {
                sb.append(":");
                sb.append(property2);
            }
            String property3 = System.getProperties().getProperty("http.nonProxyHosts");
            if (property3 != null && !"".equals(property3)) {
                sb.append(10);
                sb.append(property3);
            }
            return sb.toString();
        }
        return null;
    }

    public float getScreenBrightness() {
        Window window = this.m_Window;
        if (window == null) {
            return 1.0f;
        }
        float f = window.getAttributes().screenBrightness;
        if (f >= 0.0f) {
            return f;
        }
        int i2 = Settings.System.getInt(getContext().getContentResolver(), "screen_brightness", 255);
        return PlatformSupport.PIE_SUPPORT ? (float) Math.max(0.0d, Math.min(1.0d, ((Math.log((double) i2) * 19.811d) - 9.411d) / 100.0d)) : ((float) i2) / 255.0f;
    }

    public Bundle getSettings() {
        return Bundle.EMPTY;
    }

    /* access modifiers changed from: protected */
    public int getSplashMode() {
        try {
            return getApplicationInfo().metaData.getInt(SPLASH_MODE_METADATA_NAME);
        } catch (Exception unused) {
            return 0;
        }
    }

    /* access modifiers changed from: protected */
    public int getUaaLLaunchProcessType() {
        String processName = getProcessName();
        return (processName == null || processName.equals(this.mContext.getPackageName())) ? 0 : 1;
    }

    public View getView() {
        return this;
    }

    /* access modifiers changed from: protected */
    public void hideSoftInput() {
        postOnUiThread(new C0003d());
    }

    public void init(int i2, boolean z2) {
    }

    /* access modifiers changed from: protected */
    public boolean initializeGoogleAr() {
        if (this.m_ARCoreApi != null || this.mActivity == null || !getARCoreEnabled()) {
            return false;
        }
        GoogleARCoreApi googleARCoreApi = new GoogleARCoreApi();
        this.m_ARCoreApi = googleARCoreApi;
        googleARCoreApi.initializeARCore(this.mActivity);
        if (this.mState.b()) {
            return false;
        }
        this.m_ARCoreApi.resumeARCore();
        return false;
    }

    public boolean injectEvent(InputEvent inputEvent) {
        if (!S.d()) {
            return false;
        }
        return nativeInjectEvent(inputEvent);
    }

    /* access modifiers changed from: protected */
    public boolean isFinishing() {
        if (this.mQuitting) {
            return true;
        }
        Activity activity = this.mActivity;
        if (activity != null) {
            this.mQuitting = activity.isFinishing();
        }
        return this.mQuitting;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0005, code lost:
        r0 = r0.getCallingPackage();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isUaaLUseCase() {
        /*
            r3 = this;
            android.app.Activity r0 = r3.mActivity
            r1 = 0
            if (r0 == 0) goto L_0x0018
            java.lang.String r0 = r0.getCallingPackage()
            if (r0 == 0) goto L_0x0018
            android.content.Context r2 = r3.mContext
            java.lang.String r2 = r2.getPackageName()
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x0018
            r1 = 1
        L_0x0018:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.UnityPlayer.isUaaLUseCase():boolean");
    }

    /* access modifiers changed from: protected */
    public void kill() {
        Process.killProcess(Process.myPid());
    }

    /* access modifiers changed from: protected */
    public boolean loadLibrary(String str) {
        try {
            System.loadLibrary(str);
            return true;
        } catch (Exception | UnsatisfiedLinkError unused) {
            return false;
        }
    }

    public void lowMemory() {
        if (S.d()) {
            queueGLThreadEvent((Runnable) new A());
        }
    }

    public void newIntent(Intent intent) {
        this.m_launchUri = intent.getData();
        F f = this.m_MainThread;
        E e = E.URL_ACTIVATED;
        Handler handler = f.a;
        if (handler != null) {
            Message.obtain(handler, RUN_STATE_CHANGED_MSG_CODE, e).sendToTarget();
        }
    }

    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        if (!this.mGlView.c()) {
            return injectEvent(motionEvent);
        }
        return false;
    }

    public boolean onKeyDown(int i2, KeyEvent keyEvent) {
        return injectEvent(keyEvent);
    }

    public boolean onKeyLongPress(int i2, KeyEvent keyEvent) {
        return injectEvent(keyEvent);
    }

    public boolean onKeyMultiple(int i2, int i3, KeyEvent keyEvent) {
        return injectEvent(keyEvent);
    }

    public boolean onKeyUp(int i2, KeyEvent keyEvent) {
        return injectEvent(keyEvent);
    }

    public void onPause() {
        MultiWindowSupport.saveMultiWindowMode(this.mActivity);
        if (!MultiWindowSupport.isInMultiWindowMode(this.mActivity)) {
            setupUnityToBePaused();
        }
    }

    public void onResume() {
        if (!MultiWindowSupport.isInMultiWindowMode(this.mActivity) || MultiWindowSupport.isMultiWindowModeChangedToTrue(this.mActivity)) {
            setupUnityToBeResumed();
        }
    }

    public void onStart() {
        if (MultiWindowSupport.isInMultiWindowMode(this.mActivity)) {
            setupUnityToBeResumed();
        }
    }

    public void onStop() {
        if (MultiWindowSupport.isInMultiWindowMode(this.mActivity)) {
            setupUnityToBePaused();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mGlView.c()) {
            return injectEvent(motionEvent);
        }
        return false;
    }

    public void onUnityPlayerQuitted() {
    }

    public void onUnityPlayerUnloaded() {
    }

    public void pause() {
        setupUnityToBePaused();
    }

    /* access modifiers changed from: protected */
    public void pauseJavaAndCallUnloadCallback() {
        runOnUiThread(new p());
    }

    /* access modifiers changed from: package-private */
    public void postOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    /* access modifiers changed from: package-private */
    public void queueGLThreadEvent(Runnable runnable) {
        if (S.d()) {
            if (Thread.currentThread() == this.m_MainThread) {
                runnable.run();
            } else {
                this.m_Events.add(runnable);
            }
        }
    }

    public void quit() {
        destroy();
    }

    public void removeViewFromPlayer(View view) {
        swapViews(this.mGlView, view);
        boolean z2 = true;
        boolean z3 = view.getParent() == null;
        boolean z4 = this.mGlView.getParent() == this;
        if (!z3 || !z4) {
            z2 = false;
        }
        if (!z2) {
            if (!z3) {
                C0027u.Log(6, "removeViewFromPlayer: Failure removing view from hierarchy");
            }
            if (!z4) {
                C0027u.Log(6, "removeVireFromPlayer: Failure agging old view to hierarchy");
            }
        }
    }

    public void reportError(String str, String str2) {
        C0027u.Log(6, str + ": " + str2);
    }

    /* access modifiers changed from: protected */
    public void reportSoftInputArea(Rect rect) {
        queueGLThreadEvent((G) new l(rect));
    }

    /* access modifiers changed from: protected */
    public void reportSoftInputIsVisible(boolean z2) {
        queueGLThreadEvent((G) new m(z2));
    }

    /* access modifiers changed from: protected */
    public void reportSoftInputSelection(int i2, int i3) {
        queueGLThreadEvent((G) new j(i2, i3));
    }

    /* access modifiers changed from: protected */
    public void reportSoftInputStr(String str, int i2, boolean z2) {
        if (i2 == 1) {
            hideSoftInput();
        }
        queueGLThreadEvent((G) new i(z2, str, i2));
    }

    /* access modifiers changed from: protected */
    public void requestUserAuthorization(String str) {
        if (str != null && !str.isEmpty() && this.mActivity != null) {
            UnityPermissions.ModalWaitForPermissionResponse modalWaitForPermissionResponse = new UnityPermissions.ModalWaitForPermissionResponse();
            UnityPermissions.requestUserPermissions(this.mActivity, new String[]{str}, modalWaitForPermissionResponse);
            modalWaitForPermissionResponse.waitForResponse();
        }
    }

    public void resume() {
        setupUnityToBeResumed();
    }

    /* access modifiers changed from: package-private */
    public void runOnAnonymousThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    /* access modifiers changed from: package-private */
    public void runOnUiThread(Runnable runnable) {
        Activity activity = this.mActivity;
        if (activity != null) {
            activity.runOnUiThread(runnable);
        } else if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            this.mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    /* access modifiers changed from: package-private */
    public void sendSurfaceChangedEvent() {
        if (S.d() && this.mState.a()) {
            u uVar = new u();
            Handler handler = this.m_MainThread.a;
            if (handler != null) {
                Message.obtain(handler, uVar).sendToTarget();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setCharacterLimit(int i2) {
        runOnUiThread(new C0005f(i2));
    }

    /* access modifiers changed from: protected */
    public void setClipboardText(String str) {
        this.m_ClipboardManager.setPrimaryClip(ClipData.newPlainText("Text", str));
    }

    /* access modifiers changed from: protected */
    public void setHideInputField(boolean z2) {
        runOnUiThread(new C0006g(z2));
    }

    public void setMainSurfaceViewAspectRatio(float f) {
        if (this.mGlView != null) {
            runOnUiThread(new r(f));
        }
    }

    public void setScreenBrightness(float f) {
        float max = Math.max(0.04f, f);
        if (this.m_Window != null && getScreenBrightness() != max) {
            runOnUiThread(new s(max));
        }
    }

    /* access modifiers changed from: protected */
    public void setSelection(int i2, int i3) {
        runOnUiThread(new h(i2, i3));
    }

    /* access modifiers changed from: protected */
    public void setSoftInputStr(String str) {
        runOnUiThread(new C0004e(str));
    }

    /* access modifiers changed from: protected */
    public void setupUnityToBePaused() {
        GoogleARCoreApi googleARCoreApi = this.m_ARCoreApi;
        if (googleARCoreApi != null) {
            googleARCoreApi.pauseARCore();
        }
        c0 c0Var = this.mVideoPlayerProxy;
        if (c0Var != null) {
            c0Var.c();
        }
        AudioVolumeHandler audioVolumeHandler = this.m_AudioVolumeHandler;
        if (audioVolumeHandler != null) {
            audioVolumeHandler.a();
            this.m_AudioVolumeHandler = null;
        }
        OrientationLockListener orientationLockListener = this.m_OrientationLockListener;
        if (orientationLockListener != null) {
            orientationLockListener.a();
            this.m_OrientationLockListener = null;
        }
        pauseUnity();
    }

    /* access modifiers changed from: protected */
    public void setupUnityToBeResumed() {
        GoogleARCoreApi googleARCoreApi = this.m_ARCoreApi;
        if (googleARCoreApi != null) {
            googleARCoreApi.resumeARCore();
        }
        this.mState.e(false);
        c0 c0Var = this.mVideoPlayerProxy;
        if (c0Var != null) {
            c0Var.d();
        }
        checkResumePlayer();
        if (S.d()) {
            nativeRestartActivityIndicator();
        }
        if (this.m_AudioVolumeHandler == null) {
            this.m_AudioVolumeHandler = new AudioVolumeHandler(this.mContext);
        }
        if (this.m_OrientationLockListener == null && S.d()) {
            this.m_OrientationLockListener = new OrientationLockListener(this.mContext);
        }
        this.prevConfig = getResources().getConfiguration();
    }

    /* access modifiers changed from: protected */
    public void showSoftInput(String str, int i2, boolean z2, boolean z3, boolean z4, boolean z5, String str2, int i3, boolean z6, boolean z7) {
        postOnUiThread(new C0001b(this, str, i2, z2, z3, z4, z5, str2, i3, z6, z7));
    }

    /* access modifiers changed from: protected */
    public boolean showVideoPlayer(String str, int i2, int i3, int i4, boolean z2, int i5, int i6) {
        if (this.mVideoPlayerProxy == null) {
            this.mVideoPlayerProxy = new c0(this);
        }
        boolean a = this.mVideoPlayerProxy.a(this.mContext, str, i2, i3, i4, z2, (long) i5, (long) i6, new n());
        if (a) {
            runOnUiThread(new o());
        }
        return a;
    }

    /* access modifiers changed from: protected */
    public boolean skipPermissionsDialog() {
        Activity activity = this.mActivity;
        if (activity != null) {
            return UnityPermissions.skipPermissionsDialog(activity);
        }
        return false;
    }

    public boolean startOrientationListener(int i2) {
        String str;
        if (this.mOrientationListener != null) {
            str = "Orientation Listener already started.";
        } else {
            q qVar = new q(this.mContext, i2);
            this.mOrientationListener = qVar;
            if (qVar.canDetectOrientation()) {
                this.mOrientationListener.enable();
                return true;
            }
            str = "Orientation Listener cannot detect orientation.";
        }
        C0027u.Log(5, str);
        return false;
    }

    public boolean stopOrientationListener() {
        OrientationEventListener orientationEventListener = this.mOrientationListener;
        if (orientationEventListener == null) {
            C0027u.Log(5, "Orientation Listener was not started.");
            return false;
        }
        orientationEventListener.disable();
        this.mOrientationListener = null;
        return true;
    }

    /* access modifiers changed from: protected */
    public void toggleGyroscopeSensor(boolean z2) {
        SensorManager sensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        Sensor defaultSensor = sensorManager.getDefaultSensor(11);
        if (z2) {
            sensorManager.registerListener(this.m_FakeListener, defaultSensor, 1);
        } else {
            sensorManager.unregisterListener(this.m_FakeListener);
        }
    }

    public void unload() {
        nativeApplicationUnload();
    }

    /* access modifiers changed from: package-private */
    public void updateGLDisplay(int i2, Surface surface) {
        if (!this.mMainDisplayOverride) {
            updateDisplayInternal(i2, surface);
        }
    }

    public void windowFocusChanged(boolean z2) {
        this.mState.b(z2);
        if (this.mState.a()) {
            C0032z zVar = this.mSoftInput;
            if (zVar == null || zVar.c()) {
                if (z2) {
                    F f = this.m_MainThread;
                    E e = E.FOCUS_GAINED;
                    Handler handler = f.a;
                    if (handler != null) {
                        Message.obtain(handler, RUN_STATE_CHANGED_MSG_CODE, e).sendToTarget();
                    }
                } else {
                    F f2 = this.m_MainThread;
                    E e2 = E.FOCUS_LOST;
                    Handler handler2 = f2.a;
                    if (handler2 != null) {
                        Message.obtain(handler2, RUN_STATE_CHANGED_MSG_CODE, e2).sendToTarget();
                    }
                }
                checkResumePlayer();
            }
        }
    }
}
