/******************************************************************************
 *
 *  2015 (C) Copyright Open-RnD Sp. z o.o.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package pl.openrnd.utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Helper class with routines handing registering for/and notifying object's changes.
 *
 * @param <T> Interface with definition of object notifications.
 */
public class ObjectListenerHandler<T> {

    private List<WeakReference<T>> mWeakListeners;
    private List<T> mStrongListeners;

    /**
     * Registers object's listener as a weak reference.
     *
     * Caller must keep strong reference of the listener as long as it is suppose to
     * receive notifications.
     *
     * @param listener Listener object to be registered.
     * @return Size of all registered weak listeners.
     */
    public synchronized int registerObjectWeakListener(T listener) {
        if (mWeakListeners == null) {
            mWeakListeners = new ArrayList<WeakReference<T>>();

            mWeakListeners.add(new WeakReference<T>(listener));
        } else {
            Iterator<WeakReference<T>> iter = mWeakListeners.iterator();
            boolean skipAdding = false;
            while (iter.hasNext()) {
                T weakListener = iter.next().get();
                if (weakListener == null) {
                    iter.remove();
                } else if (weakListener == listener) {
                    skipAdding = true;
                }
            }
            if (!skipAdding) {
                mWeakListeners.add(new WeakReference<T>(listener));
            }
        }

        return mWeakListeners.size();
    }

    /**
     * Un-registers object's listener from object's weak reference listeners.
     *
     * @param listener Listener object to be un-registered.
     * @return Size of all registered weak listeners.
     */
    public synchronized int unregisterObjectWeakListener(T listener) {
        if (mWeakListeners != null) {
            Iterator<WeakReference<T>> iter = mWeakListeners.iterator();
            while (iter.hasNext()) {
                T weakListener = iter.next().get();
                if ((weakListener == null) || (weakListener == listener)) {
                    iter.remove();
                }
            }
            return mWeakListeners.size();
        } else {
            return 0;
        }
    }

    /**
     * Registers object's listener as a strong reference.
     *
     * @param listener Listener object to be registered.
     * @return Size of all registered strong listeners.
     */
    public synchronized int registerObjectStrongListener(T listener) {
        if (mStrongListeners == null) {
            mStrongListeners = new ArrayList<T>();

            mStrongListeners.add(listener);
        } else {
            if (!mStrongListeners.contains(listener)) {
                mStrongListeners.add(listener);
            }
        }

        return mStrongListeners.size();
    }

    /**
     * Un-registers object's listener from object's strong reference listeners.
     *
     * @param listener Listener object to be un-registered.
     * @return Size of all registered strong listeners.
     */
    public synchronized int unregisterObjectStrongListener(T listener) {
        if (mStrongListeners != null) {
            mStrongListeners.remove(listener);

            return mStrongListeners.size();
        } else {
            return 0;
        }
    }

    /**
     * Notify all registered listeners with object change.
     *
     * Provided NotificationHandler object is supposed to execute required notification.
     *
     * @param notificationHandler NotificationHandler object for executing required notification on
     *                            listener object.
     */
    public synchronized void notifyObjectChange(NotificationHandler notificationHandler) {
        if (mWeakListeners != null) {
            Iterator<WeakReference<T>> iter = mWeakListeners.iterator();
            while (iter.hasNext()) {
                T weakListener = iter.next().get();
                if (weakListener == null) {
                    iter.remove();
                } else {
                    notificationHandler.runOnListener(weakListener);
                }
            }
        }

        if (mStrongListeners != null) {
            for (T listener : mStrongListeners) {
                notificationHandler.runOnListener(listener);
            }
        }
    }

    /**
     * Interface defining object's change notification handler.
     *
     * @param <T> The object change listener on which the handler is suppose to
     *           execute required notification.
     */
    public static abstract class NotificationHandler<T> {
        public abstract void runOnListener(T listener);
    }
}
