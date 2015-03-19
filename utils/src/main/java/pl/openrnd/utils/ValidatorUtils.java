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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtils {

    private static final String TAG = ValidatorUtils.class.getSimpleName();

    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String FULL_NAME_PATTERN = "[A-Za-z]+ [A-Za-z]+";

    /**
     * Method checks if given String object match FULL_NAME_PATTERN pattern.
     *
     * @param fullName String to be verified.
     * @return True if given String match FULL_NAME_PATTERN pattern, false otherwise.
     */
    public static boolean isFullNameValid(String fullName) {
        return isTextValid(fullName, FULL_NAME_PATTERN);
    }

    /**
     * Method checks if given String object match EMAIL_PATTERN pattern.
     *
     * @param email String to be verified.
     * @return True if given String match EMAIL_PATTERN pattern, false otherwise.
     */
    public static boolean isEmailValid(String email) {
        return isTextValid(email, EMAIL_PATTERN);
    }

    /**
     * Method checks if given text match provided pattern.
     *
     * @param text String to be verified.
     * @param pattern Pattern to be used.
     * @return True if given text match provided pattern, false otherwise.
     */
    public static boolean isTextValid(String text, String pattern) {
        if(StringUtils.isNullOrEmpty(pattern)){
            return false;
        }
        if(text == null){
            return false;
        }
        Pattern patt = Pattern.compile(pattern);
        Matcher matcher = patt.matcher(text);
        return matcher.matches();
    }

}
