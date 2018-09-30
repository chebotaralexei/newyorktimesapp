package ru.chebotar.newyorktimesapp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.Nullable;

public class Utils {

    private static final String DATE_FORMAT_API = "dd.MM.yyyy hh:mm:ss";

    public static int dpToPx(Context context, int dp) {
        if (context==null) return 0;
        return (int) (dp * (((float) context.getResources().getDisplayMetrics().densityDpi) / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String toStringPrice(int price) {
        return "" + price + " ₽";
    }

    public static String toStringPrice(double price) {
        return "" + NumberFormat.getInstance().format(price) + " ₽";
    }

    public static String toStringSale(int sale) {
        return "-" + sale + "%";
    }

    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static void showSoftKeyboard(View view, Context context) {
        if (context!=null&& view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void showSoftKeyboard(Activity activity) {
        if (activity == null) return;
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity == null) return;
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static double safeParseDouble(String toParse) {
        try {
            return Double.parseDouble(toParse);
        } catch (NumberFormatException | NullPointerException e) {
            return 0.0;
        }
    }

    public static int getStatusBarHeight(Context context) {
        if (context==null) return 0;
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        Log.i("StatusBar", "StatusBar Height = " + result);
        return result;
    }


    public static Pair<Integer,Integer> getViewMinusStatusBarPosition(Activity activity, View view) {
        int[] array = new int[2];
        view.getLocationOnScreen(array);
        return new Pair<>(array[0], array[1] - getStatusBarHeight(activity));
    }

    public int getStatusBarTitleBarHeight(Activity context) {
        Rect rectangle = new Rect();
        Window window = context.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;
        Log.i("StatusBar", "StatusBar Height = " + statusBarHeight + " , TitleBar Height = " + titleBarHeight);
        return statusBarHeight;
    }

    public static boolean StringToBoolean(String string) {
        return "1".equals(string) || "true".equals(string);
    }

    public static String BooleanToString(boolean bool) {
        return bool ? "1" : "0";
    }

    public static String loadAssetTextAsString(Context context, String name) {
        BufferedReader in = null;
        try {
            StringBuilder buf = new StringBuilder();
            InputStream is = context.getAssets().open(name);
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            boolean isFirst = true;
            while ((str = in.readLine()) != null) {
                if (isFirst)
                    isFirst = false;
                else
                    buf.append('\n');
                buf.append(str);
            }
            return buf.toString();
        } catch (IOException e) {
            Log.e(context.getPackageName(), "Error opening asset " + name);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(context.getPackageName(), "Error closing asset " + name);
                }
            }
        }
        return null;
    }

    @Nullable
    public static Date getDate(String date, String format) {
        if (!TextUtils.isEmpty(date)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                return dateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Nullable
    public static Date getDate(String date) {
        if (!TextUtils.isEmpty(date)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_API, Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                return dateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param date    Дата строкой
     * @param formats массив форматов, так как в отзывах начали присылать дату в разных форматах
     * @return оъект Date
     */
    @Nullable
    public static Date getDate(String date, String... formats) {
        if (!TextUtils.isEmpty(date)) {
            for (String format : formats) {
                Date d = getDate(date, format);
                if (d != null) return d;
            }
        }
        return null;
    }

    public static String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_API, Locale.getDefault());
            return dateFormat.format(date);
        }
        return "";
    }

    public static String formatDate(Date date, String format) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            return dateFormat.format(date);
        }
        return null;
    }
}
