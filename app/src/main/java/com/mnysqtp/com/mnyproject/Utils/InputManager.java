package com.mnysqtp.com.mnyproject.Utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by 22428 on 2017/12/10.
 */

public class InputManager {
    public static void HideInput(Context context, EditText edittext){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
    }

    public static void ShowInput(Context context, EditText editText){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }
}
