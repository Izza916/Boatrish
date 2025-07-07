package com.unity3d.player;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.unity3d.player.UnityPermissions;

public class PermissionFragment extends Fragment {
    public static final String PERMISSION_NAMES = "PermissionNames";
    public static final int UNITY_PERMISSION_REQUEST_CODE = 96489;
    private final Activity m_Activity;
    private final Looper m_Looper;
    private final IPermissionRequestCallbacks m_ResultCallbacks;

    class a implements Runnable {
        final /* synthetic */ String[] a;

        a(String[] strArr) {
            this.a = strArr;
        }

        public void run() {
            PermissionFragment.this.reportAllDenied(this.a);
        }
    }

    class b implements Runnable {
        private IPermissionRequestCallbacks a;
        private String b;
        private int c;
        private boolean d;

        b(PermissionFragment permissionFragment, IPermissionRequestCallbacks iPermissionRequestCallbacks, String str, int i, boolean z) {
            this.a = iPermissionRequestCallbacks;
            this.b = str;
            this.c = i;
            this.d = z;
        }

        public void run() {
            int i = this.c;
            if (i == -1) {
                if (Build.VERSION.SDK_INT >= 30 || this.d) {
                    this.a.onPermissionDenied(this.b);
                } else {
                    this.a.onPermissionDeniedAndDontAskAgain(this.b);
                }
            } else if (i == 0) {
                this.a.onPermissionGranted(this.b);
            }
        }
    }

    public PermissionFragment() {
        this.m_ResultCallbacks = null;
        this.m_Activity = null;
        this.m_Looper = null;
    }

    public PermissionFragment(Activity activity, IPermissionRequestCallbacks iPermissionRequestCallbacks) {
        this.m_ResultCallbacks = iPermissionRequestCallbacks;
        this.m_Activity = activity;
        this.m_Looper = Looper.myLooper();
    }

    /* access modifiers changed from: private */
    public void reportAllDenied(String[] strArr) {
        for (String onPermissionDenied : strArr) {
            this.m_ResultCallbacks.onPermissionDenied(onPermissionDenied);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestPermissions(getArguments().getStringArray(PERMISSION_NAMES), UNITY_PERMISSION_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 96489) {
            if (strArr.length != 0) {
                int i2 = 0;
                while (i2 < strArr.length && i2 < iArr.length) {
                    int i3 = iArr[i2];
                    IPermissionRequestCallbacks iPermissionRequestCallbacks = this.m_ResultCallbacks;
                    if (!(iPermissionRequestCallbacks == null || this.m_Activity == null || this.m_Looper == null)) {
                        if (iPermissionRequestCallbacks instanceof UnityPermissions.ModalWaitForPermissionResponse) {
                            iPermissionRequestCallbacks.onPermissionGranted(strArr[i2]);
                        } else {
                            String str = strArr[i2] == null ? "<null>" : strArr[i2];
                            new Handler(this.m_Looper).post(new b(this, this.m_ResultCallbacks, str, iArr[i2], this.m_Activity.shouldShowRequestPermissionRationale(str)));
                        }
                    }
                    i2++;
                }
            } else if (!(this.m_ResultCallbacks == null || this.m_Activity == null || this.m_Looper == null)) {
                String[] stringArray = getArguments().getStringArray(PERMISSION_NAMES);
                if (this.m_ResultCallbacks instanceof UnityPermissions.ModalWaitForPermissionResponse) {
                    reportAllDenied(stringArray);
                } else {
                    new Handler(this.m_Looper).post(new a(stringArray));
                }
            }
            FragmentTransaction beginTransaction = getActivity().getFragmentManager().beginTransaction();
            beginTransaction.remove(this);
            beginTransaction.commit();
        }
    }
}
