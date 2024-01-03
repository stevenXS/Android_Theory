package com.steven.aidl_demo.optUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainThreadOptUtil {
    public static final String TAG = "BookClientActivity# ";
    private Class<?> class_Handler;
    private Class<?> class_MessageQueue;
    private Class<?> class_Message;
    private Class<?> class_ActivityThread;
    private Class<?> class_ActivityThread_BindServiceData;
    private Field filed_mMessages;
    private Field field_next;
    private Handler mh;
    private MessageQueue mhHandlerMessageQueue;
    private Field field_nextMessage;
    private static Method metaGetDeclaredField;
    private static Intent bindServiceData;

    private static Method metaGetDeclaredMethod;
    private static Method metaClassForNameMethod;

    static {
        try {
            metaGetDeclaredField = Class.class.getDeclaredMethod("getDeclaredField", String.class);
            metaGetDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
            metaClassForNameMethod = Class.class.getDeclaredMethod("forName",String.class);
            // Android10.0以上执行以下代码才可以访问类的隐藏属性
            // 对应异常信息 Accessing hidden method Landroid/view/ViewGroup;->makeOptionalFitsSystemWindows()V (greylist, reflection, allowed)
            Method forName = Class.class.getDeclaredMethod("forName", String.class);
            Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
            Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
            Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
            Method setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
            Object sVmRuntime = getRuntime.invoke(null);
            setHiddenApiExemptions.invoke(sVmRuntime, new Object[]{new String[]{"L"}});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("PrivateApi")
    public MainThreadOptUtil() {
        try {
            class_Handler = Class.forName("android.os.Handler");
            class_MessageQueue = Class.forName("android.os.MessageQueue");
            filed_mMessages = class_MessageQueue.getDeclaredField("mMessages");
            filed_mMessages.setAccessible(true);
            field_nextMessage = Message.class.getDeclaredField("next");
            field_nextMessage.setAccessible(true);

            class_Message = Class.forName("android.os.Message");
            field_next = class_Message.getDeclaredField("next");
            field_next.setAccessible(true);
            class_ActivityThread = Class.forName("android.app.ActivityThread");
            class_ActivityThread_BindServiceData = Class.forName("android.app.ActivityThread$BindServiceData");

            mh = reflectGetmH();
            mhHandlerMessageQueue = getMessageQueue(mh);
            bindServiceData = getBindServiceData();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "error ");
        }
        Log.d(TAG, "init " + mh + "," + class_ActivityThread +", bindServiceData " + bindServiceData);
    }

    private Handler reflectGetmH() throws Exception {
        Object currentActivityThread = class_ActivityThread.getDeclaredMethod("currentActivityThread").invoke(null);
        Field mhField = class_ActivityThread.getDeclaredField("mH");
        mhField.setAccessible(true);
        return (Handler) mhField.get(currentActivityThread);
    }

    private Intent getBindServiceData() {
        try {
            Field intent = getHiddenField(this.class_ActivityThread_BindServiceData, "intent");
            intent.setAccessible(true);
            return (Intent) intent.get(this.class_ActivityThread_BindServiceData.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("SoonBlockedPrivateApi")
    private MessageQueue getMessageQueue(Handler handler) throws Exception {
        Field mQueueField = getHiddenField(this.class_Handler,"mQueue");
        mQueueField.setAccessible(true);
        return (MessageQueue) mQueueField.get(handler);
    }

    private Field getHiddenField(Class clazz, String name) throws Exception {
        return (Field) metaGetDeclaredField.invoke(clazz, name);
    }

    public boolean upgradeMessagePriority(Handler handler, MessageQueue messageQueue) {
        if (messageQueue == null) {
            Log.e(TAG, "message queue is null");
            return false;
        }
        synchronized (messageQueue) {
            try {
                Message message = (Message) filed_mMessages.get(messageQueue);
                Message preMessage = null;
                while (message != null) {
                    Log.d(TAG, "[get Message] " + message);
                    if (isTargetMessage(message)) {
                        // 拷贝消息
                        Message copy = copyMessage(message);
                        if (preMessage != null) { //如果已经在队列首部了，则不需要优化
                            //当前消息的下一个消息
                            Message next = nextMessage(message);
                            setMessageNext(preMessage, next);
                            boolean res = handler.sendMessageAtFrontOfQueue(copy);
                            Log.d(TAG, "sendMessage to head " + res + ", " + copy);
                            return true;
                        }
                        return false;
                    }
                    preMessage = message;
                    message = nextMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean isTargetMessage(Message message) {
        if (message != null && (message.what == 121)) {
            return true;
        }
//        if (message.getCallback() != null) {
//            if (message.getCallback().getClass().getName().split("\\$")[0].equals("com.steven.aidl_demo.BookClientActivity")) {
//                Log.d(TAG, "targetMessage is get " + message);
//                return true;
//            }
//        }
        return false;
    }

    private static Message copyMessage(Message message) {
        Message copy = Message.obtain(message);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (message.isAsynchronous()) {
                copy.setAsynchronous(true);
            }
        }
        return copy;
    }

    private Message nextMessage(Message message) {
        try {
            Message next = (Message) field_next.get(message);
            return next;
        } catch (IllegalAccessException e) {
            //report it
            return null;
        }
    }

    public boolean setMessageNext(Message target, Message nextMessage) {
        try {
            field_nextMessage.set(target, nextMessage);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }


    public Handler getMh() {
        return mh;
    }

    public MessageQueue getMhHandlerMessageQueue() {
        return mhHandlerMessageQueue;
    }
}
