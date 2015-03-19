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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class StringUtils {

    private static final String TAG =  StringUtils.class.getSimpleName();

    public static final String EMPTY = "";

    /**
     * Checks if given string is null or empty.
     *
     * @param string String to be verified.
     * @return True if given string is null or empty.
     */
    public static boolean isNullOrEmpty(String string) {
        return (string == null) || (string.length() == 0);
    }

    /**
     * Checks if given string is null. If yes returns empty string.
     *
     * @param text String to be verified.
     * @return Provided string or empty string if the provided one is null.
     */
    public static String getNotNull(String text) {
        return text != null ? text : EMPTY;
    }

    /**
     * Creates concatenation of all strings provided, additionally adding
     * given separator between each concatenation.
     *
     * @param strings List of string to merge.
     * @param separator Separator to use.
     * @return The concatenated string.
     */
    public static String getConcatenatedString(List<String> strings, String separator) {
        String sep = getNotNull(separator);
        StringBuilder stringBuilder = new StringBuilder();
        if (strings != null) {
            int size = strings.size();
            for (int i = 0; i < size; ++i) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(sep);
                }
                stringBuilder.append(strings.get(i));
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Tries to read data under provided input stream as a String object using given encoding.
     *
     * @param inputStream InputStream from which data has to be read.
     * @param encoding Encoding to be used.
     * @return String object from the stream or null in case of error.
     */
    public static String streamToString(InputStream inputStream, String encoding) {
        String result = null;

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, encoding));
            String line;
            String newLine = System.getProperty("line.separator");
            if (newLine == null) {
                newLine = "\n";
            }
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(newLine);
            }
            result = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.w(TAG, "StringUtils.streamToString IOException", e);
                }
            }
        }
        return result;
    }
}
