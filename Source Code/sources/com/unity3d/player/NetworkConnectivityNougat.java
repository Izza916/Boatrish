package com.unity3d.player;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

public class NetworkConnectivityNougat extends NetworkConnectivity {
    /* access modifiers changed from: private */
    public int b = 0;
    private final ConnectivityManager.NetworkCallback c;

    class a extends ConnectivityManager.NetworkCallback {
        a() {
        }

        public void onAvailable(Network network) {
            super.onAvailable(network);
        }

        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            int i;
            NetworkConnectivityNougat networkConnectivityNougat;
            super.onCapabilitiesChanged(network, networkCapabilities);
            if (networkCapabilities.hasTransport(0)) {
                networkConnectivityNougat = NetworkConnectivityNougat.this;
                i = 1;
            } else {
                networkConnectivityNougat = NetworkConnectivityNougat.this;
                i = 2;
            }
            networkConnectivityNougat.b = i;
        }

        public void onLost(Network network) {
            super.onLost(network);
            NetworkConnectivityNougat.this.b = 0;
        }

        public void onUnavailable() {
            super.onUnavailable();
            NetworkConnectivityNougat.this.b = 0;
        }
    }

    public NetworkConnectivityNougat(Context context) {
        super(context);
        a aVar = new a();
        this.c = aVar;
        if (this.a != null) {
            this.b = super.b();
            this.a.registerDefaultNetworkCallback(aVar);
        }
    }

    public void a() {
        ConnectivityManager connectivityManager = this.a;
        if (connectivityManager != null) {
            connectivityManager.unregisterNetworkCallback(this.c);
        }
    }

    public int b() {
        return this.b;
    }
}
