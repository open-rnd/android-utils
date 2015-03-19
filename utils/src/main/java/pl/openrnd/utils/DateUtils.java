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

import java.util.Calendar;
import java.util.Date;

public final class DateUtils {
    private DateUtils() {}

    /**
     * Checks if provided Date objects points to the same day.
     *
     * @param date1 First java.util.Date object
     * @param date2 Second java.util.Date object
     * @return True if both objects points to the same day, otherwise returns false
     */
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        return isSameDay(calendar1, calendar2);
    }

    /**
     * Checks if second Date object points to the day after the first Date object.
     *
     * @param now First java.util.Date object
     * @param date Second java.util.Date object
     * @return True if second Date object points to the day after the first Date object, false otherwise.
     */
    public static boolean isTomorrow(Date now, Date date) {
        Calendar calendarNew = Calendar.getInstance();
        calendarNew.setTime(now);
        calendarNew.add(Calendar.DAY_OF_MONTH, 1);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return isSameDay(calendarNew, calendar);
    }

    /**
     * Checks if provided Calendar objects points to the same day.
     *
     * @param calendar1 First Calendar object
     * @param calendar2 Second Calendar object
     * @return True if both objects points to the same day, otherwise returns false
     */
    public static boolean isSameDay(Calendar calendar1, Calendar calendar2) {
        return (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) &&
                (calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) &&
                (calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH));
    }
}
