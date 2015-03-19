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

import java.util.Random;

public final class RandUtils {
    private RandUtils() {}

    /**
     * Rands double value withing a range provided.
     *
     * @param random Random object.
     * @param rangeMin Min value.
     * @param rangeMax Max value.
     * @return Random double value between rangeMin and rangeMax values.
     */
    public static double randDouble(Random random, double rangeMin, double rangeMax) {
        return rangeMin + (rangeMax - rangeMin) * random.nextDouble();
    }

    /**
     * Rands integer value withing a range provided.
     *
     * @param random Random object.
     * @param rangeMin Min value.
     * @param rangeMax Max value.
     * @return Random integer value between rangeMin and rangeMax values.
     */
    public static int randInt(Random random, int rangeMin, int rangeMax) {
        return rangeMin + (rangeMax - rangeMin) * random.nextInt();
    }
}
