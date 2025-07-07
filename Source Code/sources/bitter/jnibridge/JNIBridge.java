package bitter.jnibridge;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JNIBridge {

    private static class a implements InvocationHandler {
        /* access modifiers changed from: private */
        public Object a = new Object[0];
        /* access modifiers changed from: private */
        public long b;
        private Constructor c;

        public a(long j) {
            this.b = j;
            Class<MethodHandles.Lookup> cls = MethodHandles.Lookup.class;
            try {
                Constructor<MethodHandles.Lookup> declaredConstructor = cls.getDeclaredConstructor(new Class[]{Class.class, Integer.TYPE});
                this.c = declaredConstructor;
                declaredConstructor.setAccessible(true);
            } catch (NoClassDefFoundError | NoSuchMethodException unused) {
                this.c = null;
            }
        }

        public Object invoke(Object obj, Method method, Object[] objArr) {
            synchronized (this.a) {
                long j = this.b;
                if (j == 0) {
                    return null;
                }
                try {
                    Object invoke = JNIBridge.invoke(j, method.getDeclaringClass(), method, objArr);
                    return invoke;
                } catch (NoSuchMethodError e) {
                    if (this.c == null) {
                        System.err.println("JNIBridge error: Java interface default methods are only supported since Android Oreo");
                        throw e;
                    } else if ((method.getModifiers() & 1024) == 0) {
                        if (objArr == null) {
                            objArr = new Object[0];
                        }
                        Class<?> declaringClass = method.getDeclaringClass();
                        return ((MethodHandles.Lookup) this.c.newInstance(new Object[]{declaringClass, 2})).in(declaringClass).unreflectSpecial(method, declaringClass).bindTo(obj).invokeWithArguments(objArr);
                    } else {
                        throw e;
                    }
                }
            }
        }
    }

    static void disableInterfaceProxy(Object obj) {
        if (obj != null) {
            a aVar = (a) Proxy.getInvocationHandler(obj);
            synchronized (aVar.a) {
                aVar.b = 0;
            }
        }
    }

    static native Object invoke(long j, Class cls, Method method, Object[] objArr);

    static Object newInterfaceProxy(long j, Class[] clsArr) {
        return Proxy.newProxyInstance(JNIBridge.class.getClassLoader(), clsArr, new a(j));
    }
}
