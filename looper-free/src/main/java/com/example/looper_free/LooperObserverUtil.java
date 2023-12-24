package com.example.looper_free;

import android.os.Looper;
import android.os.Message;

import java.lang.reflect.Method;

public class LooperObserverUtil {
    public static boolean setObserver(final LooperMessageObserver looperMessageObserver) {
        try {
            final Observer oldObserver = ReflectUtil.getDeclaredFieldObj(Looper.class,null,"sObserver");
            Method forNameMethod = Class.class.getDeclaredMethod("forName", String.class);
            Class classObserver = (Class) forNameMethod.invoke(null, "android.os.Looper$Observer");
            Method setObserverMethod = ReflectUtil.getDeclaredMethod(Looper.class,"setObserver",classObserver);

            //检查函数签名是否符合预期
            Method messageDispatchStarting =ReflectUtil.getDeclaredMethod(classObserver,
                    "messageDispatchStarting");
            Method messageDispatched =ReflectUtil.getDeclaredMethod(classObserver,
                    "messageDispatched",Object.class, Message.class);
            Method dispatchingThrewException =ReflectUtil.getDeclaredMethod(classObserver,
                    "messageDispatchStarting");

            if (messageDispatchStarting == null || messageDispatched == null ||
                    dispatchingThrewException == null) {
                return false;
            }
            //检查  Observer的函数是否存在
            if (oldObserver!=null){
                setObserverMethod.invoke(null,new LooperObserver(){
                    @Override
                    public Object messageDispatchStarting() {
                        Object token = null;
                        token = oldObserver.messageDispatchStarting();
                        looperMessageObserver.messageDispatchStarting(token);
                        return token;
                    }

                    @Override
                    public void messageDispatched(Object token, Message msg) {
                        oldObserver.messageDispatched(token, msg);
                        looperMessageObserver.messageDispatched(token, msg);
                    }

                    @Override
                    public void dispatchingThrewException(Object token, Message msg, Exception exception) {
                        System.out.println("捕获到异常 dispatchingThrewException");
                        oldObserver.dispatchingThrewException(token, msg, exception);
                        looperMessageObserver.dispatchingThrewException(token, msg, exception);
                    }
                });

            }else {
                setObserverMethod.invoke(classObserver, new LooperObserver(){
                    @Override
                    public Object messageDispatchStarting() {
                        return looperMessageObserver.messageDispatchStarting(new Object());
                    }

                    @Override
                    public void messageDispatched(Object token, Message msg) {
                        looperMessageObserver.messageDispatched(token,msg);

                    }

                    @Override
                    public void dispatchingThrewException(Object token, Message msg, Exception exception) {
                        looperMessageObserver.dispatchingThrewException(token,msg,exception);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public interface Observer {
        /**
         * Called right before a message is dispatched.
         *
         * <p> The token type is not specified to allow the implementation to specify its own type.
         *
         * @return a token used for collecting telemetry when dispatching a single message.
         *         The token token must be passed back exactly once to either
         *         {@link Observer#messageDispatched} or {@link Observer#dispatchingThrewException}
         *         and must not be reused again.
         *
         */
        Object messageDispatchStarting();

        /**
         * Called when a message was processed by a Handler.
         *
         * @param token Token obtained by previously calling
         *              {@link Observer#messageDispatchStarting} on the same Observer instanceRef.
         * @param msg The message that was dispatched.
         */
        void messageDispatched(Object token, Message msg);

        /**
         * Called when an exception was thrown while processing a message.
         *
         * @param token Token obtained by previously calling
         *              {@link Observer#messageDispatchStarting} on the same Observer instanceRef.
         * @param msg The message that was dispatched and caused an exception.
         * @param exception The exception that was thrown.
         */
        void dispatchingThrewException(Object token, Message msg, Exception exception);
    }

}
