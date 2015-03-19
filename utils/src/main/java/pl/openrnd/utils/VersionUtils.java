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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class VersionUtils {

    private static final String TAG = ValidatorUtils.class.getSimpleName();

    public static final String DEFAULT_VERSION_NAME = "N/A";
    public static final int DEFAULT_VERSION_CODE = 0;

    /**
     * Gets the application version name as configured in AndroidManifest.xml file.
     *
     * @param context Current context.
     * @return Application version name or DEFAULT_VERSION_NAME if not found.
     */
    public static String getAppVersionName(Context context) {
        String result;

        try {
            Context applicationContext = context.getApplicationContext();
            result = applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            Log.w(TAG, "VersionUtils.getAppVersionName() NameNotFoundException", e);
            result = DEFAULT_VERSION_NAME;
        }

        return result;
    }

    /**
     * Gets the application version code as configured in AndroidManifest.xml file.
     *
     * @param context Current context.
     * @return Application version code or DEFAULT_VERSION_CODE if not found.
     */
    public static int getAppVersionCode(Context context) {
        int result;

        try {
            Context applicationContext = context.getApplicationContext();
            PackageInfo packageInfo = applicationContext.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            result = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            Log.w(TAG, "VersionUtils.getAppVersionName() NameNotFoundException", e);
            result = DEFAULT_VERSION_CODE;
        }

        return result;
    }
}
