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

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtils {

    private static final String TAG = CryptoUtils.class.getSimpleName();

    private CryptoUtils() {}

    /**
     * Hash text using MD5 algorithm.
     *
     * @param text Source text
     * @return Texts md5's or empty string in case of error.
     */
    public static String md5(final String text) {
        try {
            if(StringUtils.isNullOrEmpty(text)){
                return "";
            }
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(text.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException ex) {
            Log.w(TAG, "CryptoUtils.md5()", ex);
        }
        return "";
    }

    /**
     * Encodes provided test with given encoding.
     *
     * @param text Text to be encoded.
     * @param encoding Encoding to be used.
     * @return Encoded text or null if encoding is not supported.
     */
    public static String urlEncode(String text, String encoding) {
        String result = null;
        try {
            result = URLEncoder.encode(text, encoding);
        } catch (UnsupportedEncodingException ex) {
            Log.w(TAG, "CryptoUtils.urlEncode()", ex);
        }
        return result;
    }
}
