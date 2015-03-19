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

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Helper class for storing data in SharedPreferences.
 */
public class ObjectPreferences {
    private SharedPreferences mAppPreferences = null;

    /**
     * Class constructor.
     *
     * @param name SharedPreference name.
     * @param context Current context.
     */
    public ObjectPreferences(String name, Context context) {
        mAppPreferences = context.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * Gets object from SharedPreferences.
     *
     * @param name Key name under which the object is stored.
     * @return - Stored object or null if no object is stored or its deserialization failed.
     */
    public Object getObject(String name) {
        Object result = null;

        if (name != null) {
            String text = mAppPreferences.getString(name, null);
            result = ObjectSerializerUtils.stringToObject(text);
        }

        return result;
    }

    /**
     * Put object to SharedPreferences.
     *
     * Object must implement Serializable interface.
     *
     * @param name Key name under which the object will be stored.
     * @param object Object to be stored. If object is null item under given name will be removed.
     * @return Result of putting object to sharedPreferences. If succeed return true otherwise return false.
     */
    public boolean putObject(String name, Object object) {
        boolean result = false;

        if (name != null) {
            SharedPreferences.Editor editor = mAppPreferences.edit();
            if (object == null) {
                editor.remove(name);
            } else {
                editor.putString(name, ObjectSerializerUtils.objectToString(object));
            }

            result = editor.commit();
        }

        return result;
    }

    /**
     * Remove object from SharedPreferences.
     *
     * @param name Key name of object to removed.
     * @return True if object was removed, false otherwise.
     */
    public boolean removeObject(String name) {
        boolean result = false;

        if (name != null) {
            SharedPreferences.Editor editor = mAppPreferences.edit();
            editor.remove(name);

            result = editor.commit();
        }

        return result;
    }
}
