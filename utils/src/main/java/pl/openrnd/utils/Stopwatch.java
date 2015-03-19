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

/**
 * Class to watch elapsed time
 */
public class Stopwatch {

    private static final String TAG = Stopwatch.class.getSimpleName();

    private long mStart;
    private long mLast;

    public Stopwatch() {
        mLast = mStart = System.currentTimeMillis();
    }

    public void curr(String text) {
        long currTime = System.currentTimeMillis();
        long elapsedTime = currTime - mLast;
        mLast = currTime;
        Log.d(TAG, text + " " + elapsedTime);
    }

    public void stop(String text) {
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - mStart;
        Log.d(TAG, text + " " + elapsedTime);
    }
}