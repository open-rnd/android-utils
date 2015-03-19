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
import android.content.res.Configuration;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

public class UiUtils {

    private static final String TAG = UiUtils.class.getSimpleName();

    /**
     * Method check actually screen orientation
     * @param context
     * @return if orientation is landscape return true otherwise return false
     */
    public static boolean isLandscape(Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        }
        return false;
    }

    /**
     * Method check actually screen orientation
     * @param context
     * @return if orientation is portrait return true otherwise return false
     */
    public static boolean isPortrait(Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param context
     * @return screen width in pixels
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        return width;
    }

    /**
     *
     * @param context
     * @return screen hight in pixels
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int height = metrics.heightPixels;
        return height;
    }

    /**
     * Method find resource id by name
     * @param name - drawable name
     * @param context - application context
     * @return drawable resource id
     */
    public static int getDrawableId(String name, Context context) {
        return context.getResources().getIdentifier(name, "drawable", context.getApplicationContext().getPackageName());
    }

    /**
     * Method set view visibility to visible if state is different
     * @param view
     */
    public static void setViewVisible(View view) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Method set view visibility to gone if state is different
     * @param view
     */
    public static void setViewGone(View view) {
        if (view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * Method set view visibility to invisible if state is different
     * @param view
     */
    public static void setViewInvisible(View view) {
        if (view.getVisibility() != View.INVISIBLE) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Method set view visibility if state is different
     * @param view
     * @param visibility - value of visibility to set
     */
    public static void setViewVisibility(View view, int visibility) {
        if (view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    /**
     * Method add shader to TextView
     * @param textView
     * @param color1
     * @param color2
     */
    public static void attachGradientShaderToTextView(TextView textView, int color1, int color2) {
        Paint paint = new Paint();
        paint.setTextSize(textView.getTextSize());
        paint.setTextScaleX(textView.getTextScaleX());
        String text = textView.getText().toString();
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);

        int colors[] = new int[]{color1, color2};
        float[] positions = new float[]{0, 1};

        Shader textShader = new LinearGradient(0, 0, 0, rect.height(), colors, positions, TileMode.CLAMP);
        textView.setTextColor(color1);
        textView.getPaint().setShader(textShader);
    }

}
