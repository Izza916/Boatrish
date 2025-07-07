package com.unity3d.player;

import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.assetpacks.AssetPackState;
import com.google.android.play.core.assetpacks.AssetPackStates;
import java.util.Map;

/* renamed from: com.unity3d.player.h  reason: case insensitive filesystem */
class C0014h implements OnCompleteListener {
    private IAssetPackManagerStatusQueryCallback a;
    private Looper b = Looper.myLooper();
    private String[] c;

    public C0014h(IAssetPackManagerStatusQueryCallback iAssetPackManagerStatusQueryCallback, String[] strArr) {
        this.a = iAssetPackManagerStatusQueryCallback;
        this.c = strArr;
    }

    public void onComplete(Task task) {
        if (this.a != null) {
            int i = 0;
            try {
                AssetPackStates assetPackStates = (AssetPackStates) task.getResult();
                Map packStates = assetPackStates.packStates();
                int size = packStates.size();
                String[] strArr = new String[size];
                int[] iArr = new int[size];
                int[] iArr2 = new int[size];
                for (AssetPackState assetPackState : packStates.values()) {
                    strArr[i] = assetPackState.name();
                    iArr[i] = assetPackState.status();
                    iArr2[i] = assetPackState.errorCode();
                    i++;
                }
                new Handler(this.b).post(new C0013g(this.a, assetPackStates.totalBytes(), strArr, iArr, iArr2));
            } catch (RuntimeExecutionException e) {
                String message = e.getMessage();
                for (String str : this.c) {
                    if (message.contains(str)) {
                        new Handler(this.b).post(new C0013g(this.a, 0, new String[]{str}, new int[]{0}, new int[]{C0015i.a((Throwable) e)}));
                        return;
                    }
                }
                String[] strArr2 = this.c;
                int[] iArr3 = new int[strArr2.length];
                int[] iArr4 = new int[strArr2.length];
                for (int i2 = 0; i2 < this.c.length; i2++) {
                    iArr3[i2] = 0;
                    iArr4[i2] = C0015i.a((Throwable) e);
                }
                new Handler(this.b).post(new C0013g(this.a, 0, this.c, iArr3, iArr4));
            }
        }
    }
}
