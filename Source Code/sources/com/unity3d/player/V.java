package com.unity3d.player;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.MediaController;

public class V extends FrameLayout implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback, MediaController.MediaPlayerControl {
    public static final /* synthetic */ int z = 0;
    private final Context a;
    private final SurfaceView b;
    private final SurfaceHolder c;
    private final String d;
    private final int e;
    private final int f;
    private final boolean g;
    private final long h;
    private final long i;
    private final FrameLayout j;
    private int k;
    private int l;
    private int m;
    private int n;
    private MediaPlayer o;
    private MediaController p;
    private boolean q = false;
    private boolean r = false;
    private int s = 0;
    private boolean t = false;
    private int u = 0;
    private boolean v = false;
    private T w;
    private U x;
    private volatile int y = 0;

    protected V(Context context, String str, int i2, int i3, int i4, boolean z2, long j2, long j3, T t2) {
        super(context);
        this.w = t2;
        this.a = context;
        this.j = this;
        SurfaceView surfaceView = new SurfaceView(context);
        this.b = surfaceView;
        SurfaceHolder holder = surfaceView.getHolder();
        this.c = holder;
        holder.addCallback(this);
        setBackgroundColor(i2);
        addView(surfaceView);
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        this.d = str;
        this.e = i3;
        this.f = i4;
        this.g = z2;
        this.h = j2;
        this.i = j3;
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    private void a(int i2) {
        this.y = i2;
        T t2 = this.w;
        if (t2 != null) {
            ((X) t2).a(this.y);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean a() {
        return this.t;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    public void cancelOnPrepare() {
        a(2);
    }

    /* access modifiers changed from: protected */
    public void destroyPlayer() {
        if (!this.t) {
            pause();
        }
        doCleanUp();
    }

    /* access modifiers changed from: protected */
    public void doCleanUp() {
        U u2 = this.x;
        if (u2 != null) {
            u2.b = true;
            this.x = null;
        }
        MediaPlayer mediaPlayer = this.o;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            this.o.release();
            this.o = null;
        }
        this.m = 0;
        this.n = 0;
        this.r = false;
        this.q = false;
    }

    public boolean exitOnKeypress(int i2, KeyEvent keyEvent) {
        if (i2 != 4 && (this.e != 2 || i2 == 0 || keyEvent.isSystem())) {
            return false;
        }
        destroyPlayer();
        a(3);
        return true;
    }

    public int getAudioSessionId() {
        MediaPlayer mediaPlayer = this.o;
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getAudioSessionId();
    }

    public int getBufferPercentage() {
        if (this.g) {
            return this.s;
        }
        return 100;
    }

    public int getCurrentPosition() {
        MediaPlayer mediaPlayer = this.o;
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        MediaPlayer mediaPlayer = this.o;
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getDuration();
    }

    public boolean isPlaying() {
        boolean z2 = this.r && this.q;
        MediaPlayer mediaPlayer = this.o;
        return mediaPlayer == null ? !z2 : mediaPlayer.isPlaying() || !z2;
    }

    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i2) {
        this.s = i2;
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        destroyPlayer();
        a(3);
    }

    public boolean onKeyDown(int i2, KeyEvent keyEvent) {
        if (exitOnKeypress(i2, keyEvent)) {
            return true;
        }
        MediaController mediaController = this.p;
        return mediaController != null ? mediaController.onKeyDown(i2, keyEvent) : super.onKeyDown(i2, keyEvent);
    }

    public boolean onKeyUp(int i2, KeyEvent keyEvent) {
        if (exitOnKeypress(i2, keyEvent)) {
            return true;
        }
        MediaController mediaController = this.p;
        return mediaController != null ? mediaController.onKeyUp(i2, keyEvent) : super.onKeyUp(i2, keyEvent);
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        U u2 = this.x;
        if (u2 != null) {
            u2.b = true;
            this.x = null;
        }
        int i2 = this.e;
        if (i2 == 0 || i2 == 1) {
            MediaController mediaController = new MediaController(this.a);
            this.p = mediaController;
            mediaController.setMediaPlayer(this);
            this.p.setAnchorView(this);
            this.p.setEnabled(true);
            Context context = this.a;
            if (context instanceof Activity) {
                this.p.setSystemUiVisibility(((Activity) context).getWindow().getDecorView().getSystemUiVisibility());
            }
            this.p.show();
        }
        this.r = true;
        if (this.q && !isPlaying()) {
            a(1);
            updateVideoLayout();
            if (!this.t) {
                start();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (this.e == 2 && action == 0) {
            destroyPlayer();
            a(3);
            return true;
        }
        MediaController mediaController = this.p;
        return mediaController != null ? mediaController.onTouchEvent(motionEvent) : super.onTouchEvent(motionEvent);
    }

    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i2, int i3) {
        if (i2 != 0 && i3 != 0) {
            this.q = true;
            this.m = i2;
            this.n = i3;
            if (this.r && !isPlaying()) {
                a(1);
                updateVideoLayout();
                if (!this.t) {
                    start();
                }
            }
        }
    }

    public void pause() {
        MediaPlayer mediaPlayer = this.o;
        if (mediaPlayer != null) {
            if (this.v) {
                this.u = mediaPlayer.getCurrentPosition();
                this.o.pause();
            }
            this.t = true;
        }
    }

    public void seekTo(int i2) {
        MediaPlayer mediaPlayer = this.o;
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(i2);
        }
    }

    public void start() {
        MediaPlayer mediaPlayer = this.o;
        if (mediaPlayer != null) {
            if (this.v) {
                int i2 = this.u;
                if (i2 > 0) {
                    mediaPlayer.seekTo(i2);
                }
                this.o.start();
                this.u = 0;
            }
            this.t = false;
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i2, int i3, int i4) {
        if (this.k != i3 || this.l != i4) {
            this.k = i3;
            this.l = i4;
            if (this.v) {
                updateVideoLayout();
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:23|24|25|26|27) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:26:0x009a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void surfaceCreated(android.view.SurfaceHolder r9) {
        /*
            r8 = this;
            r9 = 1
            r8.v = r9
            boolean r0 = r8.t
            if (r0 == 0) goto L_0x001d
            android.media.MediaPlayer r9 = r8.o
            if (r9 == 0) goto L_0x00ff
            int r0 = r8.u
            if (r0 <= 0) goto L_0x00ff
            android.view.SurfaceHolder r0 = r8.c
            r9.setDisplay(r0)
            android.media.MediaPlayer r9 = r8.o
            int r0 = r8.u
            r9.seekTo(r0)
            goto L_0x00ff
        L_0x001d:
            android.media.MediaPlayer r0 = r8.o
            r1 = 0
            if (r0 == 0) goto L_0x003d
            android.view.SurfaceHolder r9 = r8.c
            r0.setDisplay(r9)
            boolean r9 = r8.t
            if (r9 != 0) goto L_0x00ff
            int r9 = r8.u
            if (r9 <= 0) goto L_0x0034
            android.media.MediaPlayer r0 = r8.o
            r0.seekTo(r9)
        L_0x0034:
            android.media.MediaPlayer r9 = r8.o
            r9.start()
            r8.u = r1
            goto L_0x00ff
        L_0x003d:
            r8.a(r1)
            r8.doCleanUp()
            android.media.MediaPlayer r0 = new android.media.MediaPlayer     // Catch:{ Exception -> 0x00fb }
            r0.<init>()     // Catch:{ Exception -> 0x00fb }
            r8.o = r0     // Catch:{ Exception -> 0x00fb }
            boolean r1 = r8.g     // Catch:{ Exception -> 0x00fb }
            if (r1 == 0) goto L_0x005a
            android.content.Context r1 = r8.a     // Catch:{ Exception -> 0x00fb }
            java.lang.String r2 = r8.d     // Catch:{ Exception -> 0x00fb }
            android.net.Uri r2 = android.net.Uri.parse(r2)     // Catch:{ Exception -> 0x00fb }
            r0.setDataSource(r1, r2)     // Catch:{ Exception -> 0x00fb }
            goto L_0x00ad
        L_0x005a:
            long r0 = r8.i     // Catch:{ Exception -> 0x00fb }
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 == 0) goto L_0x0077
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00fb }
            java.lang.String r1 = r8.d     // Catch:{ Exception -> 0x00fb }
            r0.<init>(r1)     // Catch:{ Exception -> 0x00fb }
            android.media.MediaPlayer r2 = r8.o     // Catch:{ Exception -> 0x00fb }
            java.io.FileDescriptor r3 = r0.getFD()     // Catch:{ Exception -> 0x00fb }
            long r4 = r8.h     // Catch:{ Exception -> 0x00fb }
            long r6 = r8.i     // Catch:{ Exception -> 0x00fb }
            r2.setDataSource(r3, r4, r6)     // Catch:{ Exception -> 0x00fb }
            goto L_0x00aa
        L_0x0077:
            android.content.res.Resources r0 = r8.getResources()     // Catch:{ Exception -> 0x00fb }
            android.content.res.AssetManager r0 = r0.getAssets()     // Catch:{ Exception -> 0x00fb }
            java.lang.String r1 = r8.d     // Catch:{ IOException -> 0x009a }
            android.content.res.AssetFileDescriptor r0 = r0.openFd(r1)     // Catch:{ IOException -> 0x009a }
            android.media.MediaPlayer r1 = r8.o     // Catch:{ IOException -> 0x009a }
            java.io.FileDescriptor r2 = r0.getFileDescriptor()     // Catch:{ IOException -> 0x009a }
            long r3 = r0.getStartOffset()     // Catch:{ IOException -> 0x009a }
            long r5 = r0.getLength()     // Catch:{ IOException -> 0x009a }
            r1.setDataSource(r2, r3, r5)     // Catch:{ IOException -> 0x009a }
            r0.close()     // Catch:{ IOException -> 0x009a }
            goto L_0x00ad
        L_0x009a:
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00fb }
            java.lang.String r1 = r8.d     // Catch:{ Exception -> 0x00fb }
            r0.<init>(r1)     // Catch:{ Exception -> 0x00fb }
            android.media.MediaPlayer r1 = r8.o     // Catch:{ Exception -> 0x00fb }
            java.io.FileDescriptor r2 = r0.getFD()     // Catch:{ Exception -> 0x00fb }
            r1.setDataSource(r2)     // Catch:{ Exception -> 0x00fb }
        L_0x00aa:
            r0.close()     // Catch:{ Exception -> 0x00fb }
        L_0x00ad:
            android.media.MediaPlayer r0 = r8.o     // Catch:{ Exception -> 0x00fb }
            android.view.SurfaceHolder r1 = r8.c     // Catch:{ Exception -> 0x00fb }
            r0.setDisplay(r1)     // Catch:{ Exception -> 0x00fb }
            android.media.MediaPlayer r0 = r8.o     // Catch:{ Exception -> 0x00fb }
            r0.setScreenOnWhilePlaying(r9)     // Catch:{ Exception -> 0x00fb }
            android.media.MediaPlayer r0 = r8.o     // Catch:{ Exception -> 0x00fb }
            r0.setOnBufferingUpdateListener(r8)     // Catch:{ Exception -> 0x00fb }
            android.media.MediaPlayer r0 = r8.o     // Catch:{ Exception -> 0x00fb }
            r0.setOnCompletionListener(r8)     // Catch:{ Exception -> 0x00fb }
            android.media.MediaPlayer r0 = r8.o     // Catch:{ Exception -> 0x00fb }
            r0.setOnPreparedListener(r8)     // Catch:{ Exception -> 0x00fb }
            android.media.MediaPlayer r0 = r8.o     // Catch:{ Exception -> 0x00fb }
            r0.setOnVideoSizeChangedListener(r8)     // Catch:{ Exception -> 0x00fb }
            android.media.MediaPlayer r0 = r8.o     // Catch:{ Exception -> 0x00fb }
            android.media.AudioAttributes$Builder r1 = new android.media.AudioAttributes$Builder     // Catch:{ Exception -> 0x00fb }
            r1.<init>()     // Catch:{ Exception -> 0x00fb }
            android.media.AudioAttributes$Builder r9 = r1.setUsage(r9)     // Catch:{ Exception -> 0x00fb }
            r1 = 3
            android.media.AudioAttributes$Builder r9 = r9.setContentType(r1)     // Catch:{ Exception -> 0x00fb }
            android.media.AudioAttributes r9 = r9.build()     // Catch:{ Exception -> 0x00fb }
            r0.setAudioAttributes(r9)     // Catch:{ Exception -> 0x00fb }
            android.media.MediaPlayer r9 = r8.o     // Catch:{ Exception -> 0x00fb }
            r9.prepareAsync()     // Catch:{ Exception -> 0x00fb }
            com.unity3d.player.U r9 = new com.unity3d.player.U     // Catch:{ Exception -> 0x00fb }
            r9.<init>(r8, r8)     // Catch:{ Exception -> 0x00fb }
            r8.x = r9     // Catch:{ Exception -> 0x00fb }
            java.lang.Thread r9 = new java.lang.Thread     // Catch:{ Exception -> 0x00fb }
            com.unity3d.player.U r0 = r8.x     // Catch:{ Exception -> 0x00fb }
            r9.<init>(r0)     // Catch:{ Exception -> 0x00fb }
            r9.start()     // Catch:{ Exception -> 0x00fb }
            goto L_0x00ff
        L_0x00fb:
            r9 = 2
            r8.a(r9)
        L_0x00ff:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.V.surfaceCreated(android.view.SurfaceHolder):void");
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.v = false;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0045, code lost:
        if (r7 <= r3) goto L_0x004f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004d, code lost:
        if (r7 >= r3) goto L_0x004f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0053, code lost:
        r2 = (int) (r6 * r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0056, code lost:
        if (r8 == 0) goto L_0x005b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateVideoLayout() {
        /*
            r10 = this;
            android.media.MediaPlayer r0 = r10.o
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            int r0 = r10.k
            if (r0 == 0) goto L_0x000d
            int r0 = r10.l
            if (r0 != 0) goto L_0x002b
        L_0x000d:
            android.content.Context r0 = r10.a
            java.lang.String r1 = "window"
            java.lang.Object r0 = r0.getSystemService(r1)
            android.view.WindowManager r0 = (android.view.WindowManager) r0
            android.util.DisplayMetrics r1 = new android.util.DisplayMetrics
            r1.<init>()
            android.view.Display r0 = r0.getDefaultDisplay()
            r0.getMetrics(r1)
            int r0 = r1.widthPixels
            r10.k = r0
            int r0 = r1.heightPixels
            r10.l = r0
        L_0x002b:
            int r0 = r10.k
            int r1 = r10.l
            boolean r2 = r10.q
            if (r2 == 0) goto L_0x0059
            int r2 = r10.m
            float r3 = (float) r2
            int r4 = r10.n
            float r5 = (float) r4
            float r3 = r3 / r5
            float r5 = (float) r0
            float r6 = (float) r1
            float r7 = r5 / r6
            int r8 = r10.f
            r9 = 1
            if (r8 != r9) goto L_0x0048
            int r2 = (r7 > r3 ? 1 : (r7 == r3 ? 0 : -1))
            if (r2 > 0) goto L_0x0053
            goto L_0x004f
        L_0x0048:
            r9 = 2
            if (r8 != r9) goto L_0x0056
            int r2 = (r7 > r3 ? 1 : (r7 == r3 ? 0 : -1))
            if (r2 < 0) goto L_0x0053
        L_0x004f:
            float r5 = r5 / r3
            int r4 = (int) r5
            r2 = r0
            goto L_0x005b
        L_0x0053:
            float r6 = r6 * r3
            int r2 = (int) r6
            goto L_0x005a
        L_0x0056:
            if (r8 != 0) goto L_0x0059
            goto L_0x005b
        L_0x0059:
            r2 = r0
        L_0x005a:
            r4 = r1
        L_0x005b:
            if (r0 != r2) goto L_0x005f
            if (r1 == r4) goto L_0x006d
        L_0x005f:
            android.widget.FrameLayout$LayoutParams r0 = new android.widget.FrameLayout$LayoutParams
            r1 = 17
            r0.<init>(r2, r4, r1)
            android.widget.FrameLayout r1 = r10.j
            android.view.SurfaceView r2 = r10.b
            r1.updateViewLayout(r2, r0)
        L_0x006d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.V.updateVideoLayout():void");
    }
}
