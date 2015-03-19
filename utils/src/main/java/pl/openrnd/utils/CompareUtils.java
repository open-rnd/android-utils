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

public final class CompareUtils {
    private CompareUtils() {}

    /**
     * Compare two objects using equals method. Additionally object are marked as
     * equals if both objects are nulls.
     *
     * @param arg1 First object
     * @param arg2 Second object
     * @return True is objects are the same, otherwise returns false
     */
    public static boolean areObjectsEquals(Object arg1, Object arg2) {
        if (arg1 != null) {
            return arg1.equals(arg2);
        } else {
            return arg2 == null;
        }
    }
}
