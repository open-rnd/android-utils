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

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * PersistentApplication is used to automatic save and restore all serialized objects
 * which have annotation @PersistentObject and are declared in Application class.
 */
public abstract class PersistentApplication extends Application {
    private static final String TAG = PersistentApplication.class.getSimpleName();

    private ObjectPreferences mObjectPreferences;

    @PersistentObject
    private String mAppVersion;

    /**
     * @see android.app.Application
     *
     * Method tries to restore application state and notifies about version changes.
     * Restore state will fail if stored object class definition changed.
     *
     * Please consider using database if stored objects definition is suppose to chenge with
     * version changes.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        mObjectPreferences = new ObjectPreferences(TAG, this);

        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);

        try {
            restore();
        } catch (Exception exc) {
            Log.e(TAG, "onCreate()", exc);

            clear();
            restore();
        }

        String appVersion = getVersion();
        if ((appVersion == null) || !appVersion.equals(mAppVersion)) {
            String oldVersion = mAppVersion;
            mAppVersion = appVersion;
            onVersionChanged(oldVersion, appVersion);
        }
    }

    /**
     * Method called when version of the application changed.
     *
     * If the application is started for the first time oldVersion parameter will be null.
     * New version is a result of getVersion() method.
     *
     * @param oldVersion String object with last version name.
     * @param newVersion String object with current version name.
     */
    protected void onVersionChanged(String oldVersion, String newVersion) {
        Log.d(TAG, String.format("onVersionChanged(): old[%s], new[%s]", oldVersion, newVersion));
    }

    /**
     * Gets current version name.
     *
     * @return Current version name.
     */
    public abstract String getVersion();

    /**
     * Clears all data stored.
     */
    protected void clear() {
        Log.v(TAG, "clear()");

        Class<?> clazz = getClass();

        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(PersistentObject.class)) {
                    field.setAccessible(true);
                    mObjectPreferences.removeObject(getNameForField(field));
                    field.setAccessible(false);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * Method save all object with annotation @PersistentObject to SharedPreferences
     */
    protected void save() {
        Log.v(TAG, "save()");

        Class<?> clazz = getClass();

        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            try {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(PersistentObject.class)) {
                        field.setAccessible(true);
                        mObjectPreferences.putObject(getNameForField(field), (Serializable) field.get(this));
                        field.setAccessible(false);
                    }
                }
            } catch (IllegalAccessException e) {
                Log.e(TAG, "save object error", e);
            }
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * Method restores all saved objects.
     */
    protected void restore() {
        Log.v(TAG, "restore()");

        Class<?> clazz = getClass();

        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            try {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(PersistentObject.class)) {
                        field.setAccessible(true);
                        field.set(this, mObjectPreferences.getObject(getNameForField(field)));
                        field.setAccessible(false);
                    }
                }
            } catch (IllegalAccessException e) {
                Log.e(TAG, "restore object error", e);
            }
            clazz = clazz.getSuperclass();
        }
    }

    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {
            //Current state is saved with every Activity paused.
            save();
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

    /**
     * Creates a key name for a class filed.
     *
     * Method handles fields with the same name in class hierarchy.
     *
     * @param filed Field object.
     * @return Key name for provided Filed object.
     */
    protected String getNameForField(Field filed) {
        return String.format("%s_%s", filed.getDeclaringClass().getName(), filed.getName());
    }
}
