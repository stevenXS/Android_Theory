package com.steven.test_demo.ipv6_adress;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

public class Test extends AppCompatActivity {
    public static void test() throws SocketException {
        InetAddress byAddress = null;
        NetworkInterface name = NetworkInterface.getByName("dummy0");
        List<InterfaceAddress> addresses = name.getInterfaceAddresses();
        for (InterfaceAddress address : addresses) {
            InetAddress address1 = address.getAddress();
            byte[] bytes = byAddress.getAddress();
            String res = new String(bytes);
            Log.d("address", res);
        }
    }
}
