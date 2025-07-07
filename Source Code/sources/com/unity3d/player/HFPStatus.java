package com.unity3d.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

public class HFPStatus {
    private Context a;
    private BroadcastReceiver b = null;
    /* access modifiers changed from: private */
    public boolean c = false;
    /* access modifiers changed from: private */
    public AudioManager d = null;
    /* access modifiers changed from: private */
    public boolean e = false;
    /* access modifiers changed from: private */
    public a f = a.DISCONNECTED;

    enum a {
        DISCONNECTED,
        CONNECTED;

        static {
            DISCONNECTED = new a("DISCONNECTED", 0);
            CONNECTED = new a("CONNECTED", 1);
        }
    }

    public HFPStatus(Context context) {
        this.a = context;
        this.d = (AudioManager) context.getSystemService("audio");
        initHFPStatusJni();
    }

    private final native void deinitHFPStatusJni();

    private final native void initHFPStatusJni();

    public void a() {
        clearHFPStat();
        deinitHFPStatusJni();
    }

    /* access modifiers changed from: protected */
    public void clearHFPStat() {
        BroadcastReceiver broadcastReceiver = this.b;
        if (broadcastReceiver != null) {
            this.a.unregisterReceiver(broadcastReceiver);
            this.b = null;
        }
        this.f = a.DISCONNECTED;
        if (this.e) {
            this.e = false;
            this.d.stopBluetoothSco();
        }
    }

    /* access modifiers changed from: protected */
    public boolean getHFPStat() {
        return this.f == a.CONNECTED;
    }

    /* access modifiers changed from: protected */
    public void requestHFPStat() {
        clearHFPStat();
        AnonymousClass1 r0 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getIntExtra("android.media.extra.SCO_AUDIO_STATE", -1) == 1) {
                    HFPStatus hFPStatus = HFPStatus.this;
                    hFPStatus.f = a.CONNECTED;
                    if (hFPStatus.e) {
                        hFPStatus.e = false;
                        hFPStatus.d.stopBluetoothSco();
                    }
                    HFPStatus hFPStatus2 = HFPStatus.this;
                    if (hFPStatus2.c) {
                        hFPStatus2.d.setMode(3);
                    }
                }
            }
        };
        this.b = r0;
        this.a.registerReceiver(r0, new IntentFilter("android.media.ACTION_SCO_AUDIO_STATE_UPDATED"));
        try {
            this.e = true;
            this.d.startBluetoothSco();
        } catch (NullPointerException unused) {
            C0027u.Log(5, "startBluetoothSco() failed. no bluetooth device connected.");
        }
    }

    /* access modifiers changed from: protected */
    public void setHFPRecordingStat(boolean z) {
        this.c = z;
        if (!z) {
            this.d.setMode(0);
        }
    }
}
