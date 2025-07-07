package org.fmod;

import android.media.AudioRecord;
import android.util.Log;
import java.nio.ByteBuffer;

class a implements Runnable {
    private final FMODAudioDevice a;
    private final ByteBuffer b;
    private final int c;
    private final int d;
    private volatile Thread e;
    private volatile boolean f;
    private AudioRecord g;
    private boolean h;

    a(FMODAudioDevice fMODAudioDevice, int i, int i2, int i3) {
        this.a = fMODAudioDevice;
        this.c = i;
        this.d = i2;
        this.b = ByteBuffer.allocateDirect(AudioRecord.getMinBufferSize(i, i2, i3));
    }

    private void b() {
        AudioRecord audioRecord = this.g;
        if (audioRecord != null) {
            if (audioRecord.getState() == 1) {
                this.g.stop();
            }
            this.g.release();
            this.g = null;
        }
        this.b.position(0);
        this.h = false;
    }

    public int a() {
        return this.b.capacity();
    }

    public void c() {
        if (this.e != null) {
            d();
        }
        this.f = true;
        this.e = new Thread(this);
        this.e.start();
    }

    public void d() {
        while (this.e != null) {
            this.f = false;
            try {
                this.e.join();
                this.e = null;
            } catch (InterruptedException unused) {
            }
        }
    }

    public void run() {
        int i = 3;
        while (this.f) {
            if (!this.h && i > 0) {
                b();
                AudioRecord audioRecord = new AudioRecord(1, this.c, this.d, 2, this.b.capacity());
                this.g = audioRecord;
                int state = audioRecord.getState();
                boolean z = true;
                if (state != 1) {
                    z = false;
                }
                this.h = z;
                if (z) {
                    this.b.position(0);
                    this.g.startRecording();
                    i = 3;
                } else {
                    Log.e("FMOD", "AudioRecord failed to initialize (status " + this.g.getState() + ")");
                    i += -1;
                    b();
                }
            }
            if (this.h && this.g.getRecordingState() == 3) {
                AudioRecord audioRecord2 = this.g;
                ByteBuffer byteBuffer = this.b;
                this.a.fmodProcessMicData(this.b, audioRecord2.read(byteBuffer, byteBuffer.capacity()));
                this.b.position(0);
            }
        }
        b();
    }
}
