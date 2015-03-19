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
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class FileUtils {

    public static final String TAG = FileUtils.class.getSimpleName();

    /**
     * Method creates File object to be used for image capture.
     *
     * File object has a path pointing to the gallery directory on external storage.
     * The file name is a timestamp with a format "yyyyMMdd_HHmmss".
     *
     * @param context Current context
     * @return File object
     */
    public static File getFileForImageCapture(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName);
        Log.d(TAG, "getFileForImageCapture(): " + image.getAbsolutePath());
        return image;
    }

}
