package com.unity3d.player;

import java.lang.Thread;

class O implements Thread.UncaughtExceptionHandler {
    private volatile Thread.UncaughtExceptionHandler a;
    private String b;

    O() {
    }

    static void a(String str) {
        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultUncaughtExceptionHandler instanceof O) {
            O o = (O) defaultUncaughtExceptionHandler;
            int i = -1;
            int i2 = -1;
            while (true) {
                int indexOf = str.indexOf(47, i + 1);
                if (indexOf == -1) {
                    break;
                }
                i2 = i;
                i = indexOf;
            }
            o.b = i2 < 0 ? "Unknown" : str.substring(i2 + 1);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean a() {
        boolean z;
        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultUncaughtExceptionHandler == this) {
            z = false;
        } else {
            this.a = defaultUncaughtExceptionHandler;
            this.b = "Unknown";
            Thread.setDefaultUncaughtExceptionHandler(this);
            z = true;
        }
        return z;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x00b1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void uncaughtException(java.lang.Thread r8, java.lang.Throwable r9) {
        /*
            r7 = this;
            monitor-enter(r7)
            java.lang.Error r0 = new java.lang.Error     // Catch:{ all -> 0x00b1 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x00b1 }
            r1.<init>()     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = "FATAL EXCEPTION [%s]\n"
            r3 = 1
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x00b1 }
            java.lang.String r5 = r8.getName()     // Catch:{ all -> 0x00b1 }
            r6 = 0
            r4[r6] = r5     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = java.lang.String.format(r2, r4)     // Catch:{ all -> 0x00b1 }
            r1.append(r2)     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = "Unity version     : %s\n"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x00b1 }
            java.lang.String r5 = "2022.3.35f1"
            r4[r6] = r5     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = java.lang.String.format(r2, r4)     // Catch:{ all -> 0x00b1 }
            r1.append(r2)     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = "Device model      : %s %s\n"
            r4 = 2
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x00b1 }
            java.lang.String r5 = android.os.Build.MANUFACTURER     // Catch:{ all -> 0x00b1 }
            r4[r6] = r5     // Catch:{ all -> 0x00b1 }
            java.lang.String r5 = android.os.Build.MODEL     // Catch:{ all -> 0x00b1 }
            r4[r3] = r5     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = java.lang.String.format(r2, r4)     // Catch:{ all -> 0x00b1 }
            r1.append(r2)     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = "Device fingerprint: %s\n"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x00b1 }
            java.lang.String r5 = android.os.Build.FINGERPRINT     // Catch:{ all -> 0x00b1 }
            r4[r6] = r5     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = java.lang.String.format(r2, r4)     // Catch:{ all -> 0x00b1 }
            r1.append(r2)     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = "CPU supported ABI : %s\n"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x00b1 }
            java.lang.String[] r5 = android.os.Build.SUPPORTED_ABIS     // Catch:{ all -> 0x00b1 }
            java.lang.String r5 = java.util.Arrays.toString(r5)     // Catch:{ all -> 0x00b1 }
            r4[r6] = r5     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = java.lang.String.format(r2, r4)     // Catch:{ all -> 0x00b1 }
            r1.append(r2)     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = "Build Type        : %s\n"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x00b1 }
            java.lang.String r5 = "Release"
            r4[r6] = r5     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = java.lang.String.format(r2, r4)     // Catch:{ all -> 0x00b1 }
            r1.append(r2)     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = "Scripting Backend : %s\n"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x00b1 }
            java.lang.String r5 = "IL2CPP"
            r4[r6] = r5     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = java.lang.String.format(r2, r4)     // Catch:{ all -> 0x00b1 }
            r1.append(r2)     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = "Libs loaded from  : %s\n"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x00b1 }
            java.lang.String r5 = r7.b     // Catch:{ all -> 0x00b1 }
            r4[r6] = r5     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = java.lang.String.format(r2, r4)     // Catch:{ all -> 0x00b1 }
            r1.append(r2)     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = "Strip Engine Code : %s\n"
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x00b1 }
            java.lang.Boolean r4 = java.lang.Boolean.TRUE     // Catch:{ all -> 0x00b1 }
            r3[r6] = r4     // Catch:{ all -> 0x00b1 }
            java.lang.String r2 = java.lang.String.format(r2, r3)     // Catch:{ all -> 0x00b1 }
            r1.append(r2)     // Catch:{ all -> 0x00b1 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x00b1 }
            r0.<init>(r1)     // Catch:{ all -> 0x00b1 }
            java.lang.StackTraceElement[] r1 = new java.lang.StackTraceElement[r6]     // Catch:{ all -> 0x00b1 }
            r0.setStackTrace(r1)     // Catch:{ all -> 0x00b1 }
            r0.initCause(r9)     // Catch:{ all -> 0x00b1 }
            java.lang.Thread$UncaughtExceptionHandler r1 = r7.a     // Catch:{ all -> 0x00b1 }
            r1.uncaughtException(r8, r0)     // Catch:{ all -> 0x00b1 }
            goto L_0x00b6
        L_0x00b1:
            java.lang.Thread$UncaughtExceptionHandler r0 = r7.a     // Catch:{ all -> 0x00b8 }
            r0.uncaughtException(r8, r9)     // Catch:{ all -> 0x00b8 }
        L_0x00b6:
            monitor-exit(r7)
            return
        L_0x00b8:
            r8 = move-exception
            monitor-exit(r7)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.O.uncaughtException(java.lang.Thread, java.lang.Throwable):void");
    }
}
