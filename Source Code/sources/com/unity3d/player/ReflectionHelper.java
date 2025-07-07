package com.unity3d.player;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;

final class ReflectionHelper {
    protected static boolean LOG = false;
    protected static final boolean LOGV = false;
    private static b[] a = new b[4096];
    /* access modifiers changed from: private */
    public static long b = 0;
    private static long c = 0;
    private static boolean d = false;

    class a implements d {
        private Runnable a;
        private UnityPlayer b;
        private long c;
        /* access modifiers changed from: private */
        public long d;
        /* access modifiers changed from: private */
        public boolean e;
        final /* synthetic */ long f;

        a(long j, UnityPlayer unityPlayer, Class[] clsArr) {
            this.f = j;
            long r0 = ReflectionHelper.b;
            this.a = new c(r0, j);
            this.b = unityPlayer;
            this.c = r0;
        }

        private Object a(Object obj, Method method, Object[] objArr) {
            if (objArr == null) {
                try {
                    objArr = new Object[0];
                } catch (NoClassDefFoundError unused) {
                    C0027u.Log(6, String.format("Java interface default methods are only supported since Android Oreo", new Object[0]));
                    ReflectionHelper.nativeProxyLogJNIInvokeException(this.d);
                    return null;
                }
            }
            Class<?> declaringClass = method.getDeclaringClass();
            Constructor<MethodHandles.Lookup> declaredConstructor = MethodHandles.Lookup.class.getDeclaredConstructor(new Class[]{Class.class, Integer.TYPE});
            declaredConstructor.setAccessible(true);
            return declaredConstructor.newInstance(new Object[]{declaringClass, 2}).in(declaringClass).unreflectSpecial(method, declaringClass).bindTo(obj).invokeWithArguments(objArr);
        }

        /* access modifiers changed from: protected */
        public void finalize() {
            this.b.queueGLThreadEvent(this.a);
            super.finalize();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:18:0x003f, code lost:
            if (r6 != 0) goto L_0x0037;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Object invoke(java.lang.Object r6, java.lang.reflect.Method r7, java.lang.Object[] r8) {
            /*
                r5 = this;
                long r0 = r5.c
                boolean r0 = com.unity3d.player.ReflectionHelper.beginProxyCall(r0)
                if (r0 != 0) goto L_0x0010
                r6 = 6
                java.lang.String r7 = "Scripting proxy object was destroyed, because Unity player was unloaded."
                com.unity3d.player.C0027u.Log(r6, r7)
                r6 = 0
                return r6
            L_0x0010:
                r0 = 0
                r5.d = r0     // Catch:{ all -> 0x0046 }
                r2 = 0
                r5.e = r2     // Catch:{ all -> 0x0046 }
                long r2 = r5.f     // Catch:{ all -> 0x0046 }
                java.lang.String r4 = r7.getName()     // Catch:{ all -> 0x0046 }
                java.lang.Object r2 = com.unity3d.player.ReflectionHelper.nativeProxyInvoke(r2, r4, r8)     // Catch:{ all -> 0x0046 }
                boolean r3 = r5.e     // Catch:{ all -> 0x0046 }
                if (r3 == 0) goto L_0x003b
                int r0 = r7.getModifiers()     // Catch:{ all -> 0x0046 }
                r0 = r0 & 1024(0x400, float:1.435E-42)
                if (r0 != 0) goto L_0x0035
                java.lang.Object r6 = r5.a(r6, r7, r8)     // Catch:{ all -> 0x0046 }
                com.unity3d.player.ReflectionHelper.endProxyCall()
                return r6
            L_0x0035:
                long r6 = r5.d     // Catch:{ all -> 0x0046 }
            L_0x0037:
                com.unity3d.player.ReflectionHelper.nativeProxyLogJNIInvokeException(r6)     // Catch:{ all -> 0x0046 }
                goto L_0x0042
            L_0x003b:
                long r6 = r5.d     // Catch:{ all -> 0x0046 }
                int r8 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
                if (r8 == 0) goto L_0x0042
                goto L_0x0037
            L_0x0042:
                com.unity3d.player.ReflectionHelper.endProxyCall()
                return r2
            L_0x0046:
                r6 = move-exception
                com.unity3d.player.ReflectionHelper.endProxyCall()
                throw r6
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.ReflectionHelper.a.invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[]):java.lang.Object");
        }
    }

    private static class b {
        private final Class a;
        private final String b;
        private final String c;
        /* access modifiers changed from: private */
        public final int d;
        public volatile Member e;

        b(Class cls, String str, String str2) {
            this.a = cls;
            this.b = str;
            this.c = str2;
            this.d = ((((cls.hashCode() + 527) * 31) + str.hashCode()) * 31) + str2.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof b)) {
                return false;
            }
            b bVar = (b) obj;
            return this.d == bVar.d && this.c.equals(bVar.c) && this.b.equals(bVar.b) && this.a.equals(bVar.a);
        }

        public int hashCode() {
            return this.d;
        }
    }

    private static class c implements Runnable {
        final long a;
        final long b;

        public c(long j, long j2) {
            this.a = j;
            this.b = j2;
        }

        public void run() {
            if (ReflectionHelper.beginProxyCall(this.a)) {
                try {
                    ReflectionHelper.nativeProxyFinalize(this.b);
                } finally {
                    ReflectionHelper.endProxyCall();
                }
            }
        }
    }

    protected interface d extends InvocationHandler {
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:7|8|(1:10)|11|12|(1:14)(1:19)) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        return 0.0f;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x001e */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0024 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static float a(java.lang.Class r1, java.lang.Class r2) {
        /*
            boolean r0 = r1.equals(r2)
            if (r0 == 0) goto L_0x0009
            r1 = 1065353216(0x3f800000, float:1.0)
            return r1
        L_0x0009:
            boolean r0 = r1.isPrimitive()
            if (r0 != 0) goto L_0x0028
            boolean r0 = r2.isPrimitive()
            if (r0 != 0) goto L_0x0028
            java.lang.Class r0 = r1.asSubclass(r2)     // Catch:{ ClassCastException -> 0x001e }
            if (r0 == 0) goto L_0x001e
            r1 = 1056964608(0x3f000000, float:0.5)
            return r1
        L_0x001e:
            java.lang.Class r1 = r2.asSubclass(r1)     // Catch:{ ClassCastException -> 0x0028 }
            if (r1 == 0) goto L_0x0028
            r1 = 1036831949(0x3dcccccd, float:0.1)
            return r1
        L_0x0028:
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.ReflectionHelper.a(java.lang.Class, java.lang.Class):float");
    }

    private static float a(Class cls, Class[] clsArr, Class[] clsArr2) {
        if (clsArr2.length == 0) {
            return 0.1f;
        }
        int i = 0;
        if ((clsArr == null ? 0 : clsArr.length) + 1 != clsArr2.length) {
            return 0.0f;
        }
        float f = 1.0f;
        if (clsArr != null) {
            int length = clsArr.length;
            float f2 = 1.0f;
            int i2 = 0;
            while (i < length) {
                f2 *= a(clsArr[i], clsArr2[i2]);
                i++;
                i2++;
            }
            f = f2;
        }
        return f * a(cls, clsArr2[clsArr2.length - 1]);
    }

    private static Class a(String str, int[] iArr) {
        while (iArr[0] < str.length()) {
            int i = iArr[0];
            iArr[0] = i + 1;
            char charAt = str.charAt(i);
            if (charAt != '(' && charAt != ')') {
                if (charAt == 'L') {
                    int indexOf = str.indexOf(59, iArr[0]);
                    if (indexOf == -1) {
                        return null;
                    }
                    String substring = str.substring(iArr[0], indexOf);
                    iArr[0] = indexOf + 1;
                    try {
                        return Class.forName(substring.replace('/', '.'));
                    } catch (ClassNotFoundException unused) {
                        return null;
                    }
                } else if (charAt == 'Z') {
                    return Boolean.TYPE;
                } else {
                    if (charAt == 'I') {
                        return Integer.TYPE;
                    }
                    if (charAt == 'F') {
                        return Float.TYPE;
                    }
                    if (charAt == 'V') {
                        return Void.TYPE;
                    }
                    if (charAt == 'B') {
                        return Byte.TYPE;
                    }
                    if (charAt == 'C') {
                        return Character.TYPE;
                    }
                    if (charAt == 'S') {
                        return Short.TYPE;
                    }
                    if (charAt == 'J') {
                        return Long.TYPE;
                    }
                    if (charAt == 'D') {
                        return Double.TYPE;
                    }
                    if (charAt == '[') {
                        return Array.newInstance(a(str, iArr), 0).getClass();
                    }
                    C0027u.Log(5, "! parseType; " + charAt + " is not known!");
                    return null;
                }
            }
        }
        return null;
    }

    private static synchronized boolean a(b bVar) {
        synchronized (ReflectionHelper.class) {
            b[] bVarArr = a;
            b bVar2 = bVarArr[bVar.d & (bVarArr.length - 1)];
            if (!bVar.equals(bVar2)) {
                return false;
            }
            bVar.e = bVar2.e;
            return true;
        }
    }

    private static Class[] a(String str) {
        Class a2;
        int i = 0;
        int[] iArr = {0};
        ArrayList arrayList = new ArrayList();
        while (iArr[0] < str.length() && (a2 = a(str, iArr)) != null) {
            arrayList.add(a2);
        }
        Class[] clsArr = new Class[arrayList.size()];
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            clsArr[i] = (Class) it.next();
            i++;
        }
        return clsArr;
    }

    protected static synchronized boolean beginProxyCall(long j) {
        boolean z;
        synchronized (ReflectionHelper.class) {
            if (j == b) {
                c++;
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    protected static synchronized void endProxyCall() {
        Class<ReflectionHelper> cls = ReflectionHelper.class;
        synchronized (cls) {
            long j = c - 1;
            c = j;
            if (0 == j && d) {
                cls.notifyAll();
            }
        }
    }

    protected static synchronized void endUnityLaunch() {
        Class<ReflectionHelper> cls = ReflectionHelper.class;
        synchronized (cls) {
            try {
                b++;
                d = true;
                while (c > 0) {
                    cls.wait();
                }
            } catch (InterruptedException unused) {
                C0027u.Log(6, "Interrupted while waiting for all proxies to exit.");
            }
            d = false;
        }
    }

    protected static Constructor getConstructorID(Class cls, String str) {
        Constructor constructor;
        b bVar = new b(cls, "", str);
        if (a(bVar)) {
            constructor = (Constructor) bVar.e;
        } else {
            Class[] a2 = a(str);
            float f = 0.0f;
            Constructor constructor2 = null;
            for (Constructor constructor3 : cls.getConstructors()) {
                float a3 = a(Void.TYPE, constructor3.getParameterTypes(), a2);
                if (a3 > f) {
                    constructor2 = constructor3;
                    if (a3 == 1.0f) {
                        break;
                    }
                    f = a3;
                }
            }
            a(bVar, (Member) constructor2);
            constructor = constructor2;
        }
        if (constructor != null) {
            return constructor;
        }
        throw new NoSuchMethodError("<init>" + str + " in class " + cls.getName());
    }

    protected static Field getFieldID(Class cls, String str, String str2, boolean z) {
        Field field;
        String str3 = str;
        String str4 = str2;
        boolean z2 = z;
        Class cls2 = cls;
        b bVar = new b(cls2, str3, str4);
        if (a(bVar)) {
            field = (Field) bVar.e;
        } else {
            Class[] a2 = a(str2);
            float f = 0.0f;
            Field field2 = null;
            while (cls2 != null) {
                Field[] declaredFields = cls2.getDeclaredFields();
                int length = declaredFields.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    Field field3 = declaredFields[i];
                    if (z2 == Modifier.isStatic(field3.getModifiers()) && field3.getName().compareTo(str3) == 0) {
                        float a3 = a(field3.getType(), (Class[]) null, a2);
                        if (a3 > f) {
                            field2 = field3;
                            if (a3 == 1.0f) {
                                f = a3;
                                break;
                            }
                            f = a3;
                        } else {
                            continue;
                        }
                    }
                    i++;
                }
                if (f == 1.0f || cls2.isPrimitive() || cls2.isInterface() || cls2.equals(Object.class) || cls2.equals(Void.TYPE)) {
                    break;
                }
                cls2 = cls2.getSuperclass();
            }
            a(bVar, (Member) field2);
            field = field2;
        }
        if (field != null) {
            return field;
        }
        Object[] objArr = new Object[4];
        objArr[0] = z2 ? "static" : "non-static";
        objArr[1] = str3;
        objArr[2] = str4;
        objArr[3] = cls2.getName();
        throw new NoSuchFieldError(String.format("no %s field with name='%s' signature='%s' in class L%s;", objArr));
    }

    protected static String getFieldSignature(Field field) {
        Class<?> type = field.getType();
        if (type.isPrimitive()) {
            String name = type.getName();
            return "boolean".equals(name) ? "Z" : "byte".equals(name) ? "B" : "char".equals(name) ? "C" : "double".equals(name) ? "D" : "float".equals(name) ? "F" : "int".equals(name) ? "I" : "long".equals(name) ? "J" : "short".equals(name) ? "S" : name;
        } else if (type.isArray()) {
            return type.getName().replace('.', '/');
        } else {
            return "L" + type.getName().replace('.', '/') + ";";
        }
    }

    protected static Method getMethodID(Class cls, String str, String str2, boolean z) {
        Method method;
        b bVar = new b(cls, str, str2);
        if (a(bVar)) {
            method = (Method) bVar.e;
        } else {
            Class[] a2 = a(str2);
            float f = 0.0f;
            Method method2 = null;
            while (cls != null) {
                Method[] declaredMethods = cls.getDeclaredMethods();
                int length = declaredMethods.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    Method method3 = declaredMethods[i];
                    if (z == Modifier.isStatic(method3.getModifiers()) && method3.getName().compareTo(str) == 0) {
                        float a3 = a(method3.getReturnType(), method3.getParameterTypes(), a2);
                        if (a3 > f) {
                            method2 = method3;
                            if (a3 == 1.0f) {
                                f = a3;
                                break;
                            }
                            f = a3;
                        } else {
                            continue;
                        }
                    }
                    i++;
                }
                if (f == 1.0f || cls.isPrimitive() || cls.isInterface() || cls.equals(Object.class) || cls.equals(Void.TYPE)) {
                    break;
                }
                cls = cls.getSuperclass();
            }
            a(bVar, (Member) method2);
            method = method2;
        }
        if (method != null) {
            return method;
        }
        Object[] objArr = new Object[4];
        objArr[0] = z ? "static" : "non-static";
        objArr[1] = str;
        objArr[2] = str2;
        objArr[3] = cls.getName();
        throw new NoSuchMethodError(String.format("no %s method with name='%s' signature='%s' in class L%s;", objArr));
    }

    /* access modifiers changed from: private */
    public static native void nativeProxyFinalize(long j);

    /* access modifiers changed from: private */
    public static native Object nativeProxyInvoke(long j, String str, Object[] objArr);

    /* access modifiers changed from: private */
    public static native void nativeProxyLogJNIInvokeException(long j);

    protected static Object newProxyInstance(UnityPlayer unityPlayer, long j, Class cls) {
        return newProxyInstance(unityPlayer, j, new Class[]{cls});
    }

    protected static Object newProxyInstance(UnityPlayer unityPlayer, long j, Class[] clsArr) {
        return Proxy.newProxyInstance(ReflectionHelper.class.getClassLoader(), clsArr, new a(j, unityPlayer, clsArr));
    }

    protected static void setNativeExceptionOnProxy(Object obj, long j, boolean z) {
        a aVar = (a) ((d) Proxy.getInvocationHandler(obj));
        aVar.d = j;
        aVar.e = z;
    }

    private static synchronized void a(b bVar, Member member) {
        synchronized (ReflectionHelper.class) {
            bVar.e = member;
            b[] bVarArr = a;
            bVarArr[bVar.d & (bVarArr.length - 1)] = bVar;
        }
    }
}
