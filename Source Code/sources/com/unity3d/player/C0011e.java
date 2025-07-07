package com.unity3d.player;

import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.tasks.OnSuccessListener;

/* renamed from: com.unity3d.player.e  reason: case insensitive filesystem */
class C0011e implements OnSuccessListener {
    private IAssetPackManagerMobileDataConfirmationCallback a;
    private Looper b = Looper.myLooper();

    public C0011e(IAssetPackManagerMobileDataConfirmationCallback iAssetPackManagerMobileDataConfirmationCallback) {
        this.a = iAssetPackManagerMobileDataConfirmationCallback;
    }

    public void onSuccess(Object obj) {
        Integer num = (Integer) obj;
        if (this.a != null) {
            new Handler(this.b).post(new C0010d(this.a, num.intValue() == -1));
        }
    }
}
