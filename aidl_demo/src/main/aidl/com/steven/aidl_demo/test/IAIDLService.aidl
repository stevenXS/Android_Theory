// IAIDLTestCallback.aidl
package com.steven.aidl_demo.test;
import com.steven.aidl_demo.test.IAIDLTestCallback;
// Declare any non-default types here with import statements

interface IAIDLService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void psotMessage(IAIDLTestCallback callback);
}