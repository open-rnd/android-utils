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

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public final class BitmapUtils {

    private static final String TAG = BitmapUtils.class.getSimpleName();

    private BitmapUtils() {}

    /**
     * Encodes bitmap object to Base64.
     *
     * Bitmap is scaled down if its size is larger that the max size provided.
     *
     * @param bitmap Source bitmap object
     * @param maxBitmapSize Max bitmap size calculated as height * width * 3
     * @return Base64 encoded bitmap
     */
    public static String encodeToBase64(Bitmap bitmap, int maxBitmapSize) {
        Log.d(TAG, "encodeToBase64()");

        bitmap = createScaled(bitmap, maxBitmapSize);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    /**
     * Scaled down bitmap object if its size is larger that the max size provided.
     *
     * @param source Source bitmap
     * @param maxSize Max bitmap size (height * width * 3)
     * @return Scaled bitmap object when is larger then source, otherwise return the same object
     */
    public static Bitmap createScaled(Bitmap source, final int maxSize) {
        Log.d(TAG, "createScaledBitmap()");

        final long BYTES_PER_PIXEL = 3;

        final long source_w = source.getWidth();
        final long source_h = source.getHeight();
        final long source_size = source_h * source_w * BYTES_PER_PIXEL;

        if (source_size > maxSize) {
            long w = (long) (Math.sqrt(maxSize * source_size) / (BYTES_PER_PIXEL * source_h));
            long h = (long) ((double) w * source_h / source_w);

            long new_size = w * h * BYTES_PER_PIXEL;
            Log.d(TAG, String.format("createScaledBitmap(): old[%dx%d], new[%dx%d]", source_w, source_h, w, h));
            Log.d(TAG, String.format("createScaledBitmap(): oldSize[%d], newSize[%d], max[%d]", source_size, new_size, maxSize));

            return Bitmap.createScaledBitmap(source, (int) w, (int) h, true);
        } else {
            return source;
        }
    }

    /**
     * Method rotates bitmap
     *
     * @param source Source bitmap object
     * @param angle Rotation angle in degrees
     * @return Rotated bitmap
     */
    public static Bitmap rotate(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
