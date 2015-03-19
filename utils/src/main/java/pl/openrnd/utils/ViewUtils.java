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

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

public final class ViewUtils {

    private static final String TAG = ValidatorUtils.class.getSimpleName();

    private ViewUtils() {
    }

    /**
     * Method scroll list view to top
     * @param listView - refernece to listView
     */
    public static void scrollListViewToTop(final ListView listView) {
        if (listView == null) {
            return;
        }
        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(0);
                listView.scrollTo(0, 0);
            }
        });
    }

    /**
     * Method hide keyboard
     * @param context
     */
    public static void hideSoftKeyboard(Context context) {
        try {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = ((Activity) context).getCurrentFocus();
            if (view == null) {
                return;
            }
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception ex) {
            Log.w(TAG, "ViewUtils.hideSoftKeyboard() Exception" , ex);
        }
    }

    /**
     * Helper method to run particular routines on every view within a view hierarchy
     * starting from provided root view.
     *
     * @param view Root view.
     * @param viewOperationRunner View operation handler.
     */
    public static void runOperationOnView(View view, ViewOperationRunner viewOperationRunner) {
        viewOperationRunner.runOnView(view);

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;
            int childs = viewGroup.getChildCount();
            for (int i = 0; i < childs; ++i) {
                runOperationOnView(viewGroup.getChildAt(i), viewOperationRunner);
            }
        }
    }

    /**
     * Interface defining view operation handler.
     */
    public static interface ViewOperationRunner {
        void runOnView(View view);
    }
}
