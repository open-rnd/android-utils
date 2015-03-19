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

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectSerializerUtils  {
    private static final String TAG = ObjectSerializerUtils.class.getSimpleName();

    /**
     * Convert Object to String.
     * @param object Object must implements Serializable
     * @return
     */
    public static String objectToString(Object object) {
        String encoded = null;

        if (object != null) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                        byteArrayOutputStream);
                objectOutputStream.writeObject(object);
                objectOutputStream.close();
                encoded = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            } catch (Exception e) {
                Log.e(TAG, String.format("objectToString(): EXC[%s]", e.getMessage()));
                e.printStackTrace();

                throw new IllegalStateException(e);
            }
        }

        return encoded;
    }

    /**
     * Convert String to Object
     * @param string
     * @return object converted from String
     */
    public static Object stringToObject(String string) {
        Object object = null;

        if (string == null) {
            return object;
        }

        byte[] bytes = Base64.decode(string.getBytes(), Base64.DEFAULT);
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            object = objectInputStream.readObject();
        } catch (Exception e) {
            Log.e(TAG, String.format("stringToObject(): EXC[%s]", e.getMessage()));
            e.printStackTrace();

            throw new IllegalStateException(e);
        }

        return object;
    }
}
