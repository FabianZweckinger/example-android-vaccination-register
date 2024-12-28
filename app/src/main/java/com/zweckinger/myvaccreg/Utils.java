package com.zweckinger.myvaccreg;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Utils {

    public static Context context;

    /** Has to be called once before using this utility. */
    public static void init(Context context) {
        Utils.context = context;
    }

    public static void showError(String errorMessage) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
        Log.e("Error", errorMessage);
    }
}
