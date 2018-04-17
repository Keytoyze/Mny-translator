package com.mnysqtp.com.mnyproject.Utils;

import android.content.Context;

import com.mnysqtp.com.mnyproject.Activity.MainActivity;
import com.mnysqtp.com.mnyproject.Activity.WebViewActivity;

public class Util {
    public static void sendInv(Context context) {
        SharedPrefsUtils.putBoolean("ans", false, context);
        WebViewActivity.sendIntent("https://www.wjx.cn/jq/22681893.aspx", context);
    }
}
