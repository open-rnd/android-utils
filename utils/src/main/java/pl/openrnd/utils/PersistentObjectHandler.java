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

import android.os.Bundle;

import java.io.Serializable;
import java.lang.reflect.Field;

public class PersistentObjectHandler {
    private static final String TAG = PersistentObjectHandler.class.getSimpleName();

    /**
     * Method find all declared field with annotation @PersistentObject in object and add them to bundle
     *
     * @param object Object to save
     * @param bundle Bundle object to be used for storing data.
     */
    public void save(Object object, Bundle bundle) {
        Field[] fields = object.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                if (field.isAnnotationPresent(PersistentObject.class)) {
                    field.setAccessible(true);
                    bundle.putSerializable(getNameForField(field), (Serializable) field.get(object));
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method restore all declared field with annotation @PersistentObject in object
     *
     * @param object Object to restore.
     * @param bundle Bundle object from which data has to be restored.
     */
    public void restore(Object object, Bundle bundle) {
        Field[] fields = object.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                if (field.isAnnotationPresent(PersistentObject.class)) {
                    field.setAccessible(true);
                    field.set(object, bundle.getSerializable(getNameForField(field)));
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a key name for a class filed.
     *
     * Method handles fields with the same name in class hierarchy.
     *
     * @param filed Field object.
     * @return Key name for provided Filed object.
     */
    protected String getNameForField(Field filed) {
        return String.format("%s_%s_%s", TAG, filed.getDeclaringClass().getName(), filed.getName());
    }
}