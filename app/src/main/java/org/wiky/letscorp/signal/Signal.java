package org.wiky.letscorp.signal;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wiky on 7/14/16.
 */
public class Signal {
    public final static String SIGNAL_POST_RESET_START = "post-reset-start";
    public final static String SIGNAL_POST_RESET_END = "post-reset-end";
    public final static String SIGNAL_POST_READN = "post-readn";
    public final static String SIGNAL_CATEGORY_CHANGE = "category-change";
    public static Map<String, SignalHandler> mHandlers = new HashMap<>();

    public static void register(String signal, SignalHandler handler) {
        mHandlers.put(signal, handler);
    }

    public static void trigger(String signal, Object data) {
        SignalHandler handler = mHandlers.get(signal);
        if (handler != null) {
            handler.handleSignal(signal, data);
        }
    }

    public static void trigger(String signal) {
        trigger(signal, null);
    }
}
