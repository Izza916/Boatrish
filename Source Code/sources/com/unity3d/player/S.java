package com.unity3d.player;

class S {
    private static boolean e = false;
    private boolean a = false;
    private boolean b = false;
    private boolean c = true;
    private boolean d = false;

    S() {
    }

    static boolean d() {
        return e;
    }

    static void e() {
        e = true;
    }

    static void f() {
        e = false;
    }

    /* access modifiers changed from: package-private */
    public boolean a() {
        return this.d;
    }

    /* access modifiers changed from: package-private */
    public boolean a(boolean z) {
        return e && (z || this.a) && !this.c && !this.b;
    }

    /* access modifiers changed from: package-private */
    public void b(boolean z) {
        this.a = z;
    }

    /* access modifiers changed from: package-private */
    public boolean b() {
        return this.c;
    }

    /* access modifiers changed from: package-private */
    public void c(boolean z) {
        this.b = z;
    }

    /* access modifiers changed from: package-private */
    public boolean c() {
        return this.b;
    }

    /* access modifiers changed from: package-private */
    public void d(boolean z) {
        this.d = z;
    }

    /* access modifiers changed from: package-private */
    public void e(boolean z) {
        this.c = z;
    }

    public String toString() {
        return super.toString();
    }
}
