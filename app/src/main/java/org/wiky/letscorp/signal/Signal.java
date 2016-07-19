package org.wiky.letscorp.signal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Signal {

    public static final int SIGINT_ITEM_READN = 0;

    private static Map<Integer, List<SignalListener>> mListeners = new HashMap<>();

    public static void register(int signal, SignalListener listener) {
        List<SignalListener> listeners = mListeners.get(signal);
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
        mListeners.put(signal, listeners);
    }

    public static void unregister(int signal, SignalListener listener) {
        List<SignalListener> listeners = mListeners.get(signal);
        for (SignalListener l : listeners) {
            if (l == listener) {
                listeners.remove(listener);
                break;
            }
        }
    }

    public static void trigger(int signal, Object data) {
        List<SignalListener> listeners = mListeners.get(signal);
        for (SignalListener listener : listeners) {
            listener.onSignal(signal, data);
        }
    }

    public interface SignalListener {
        void onSignal(int signal, Object data);
    }
}

