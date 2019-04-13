package com.my.newproject2;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class SketchwareUtil {
    public static void showMessage(Context context, String str) {
        Toast.makeText(context, str, 0).show();
    }

    public static int getLocationX(View view) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        return iArr[0];
    }

    public static int getLocationY(View view) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        return iArr[1];
    }

    public static int getRandom(int i, int i2) {
        return new Random().nextInt((i2 - i) + 1) + i;
    }

    public static ArrayList<Double> getCheckedItemPositionsToArray(ListView listView) {
        ArrayList arrayList = new ArrayList();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        for (int i = 0; i < checkedItemPositions.size(); i++) {
            if (checkedItemPositions.valueAt(i)) {
                arrayList.add(Double.valueOf((double) checkedItemPositions.keyAt(i)));
            }
        }
        return arrayList;
    }

    public static float getDip(Context context, int i) {
        return TypedValue.applyDimension(1, (float) i, context.getResources().getDisplayMetrics());
    }

    public static int getDisplayWidthPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDisplayHeightPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static void getAllKeysFromMap(Map<String, Object> map, ArrayList<String> arrayList) {
        if (arrayList != null) {
            arrayList.clear();
            if (map != null && map.size() > 0) {
                for (Entry key : map.entrySet()) {
                    arrayList.add((String) key.getKey());
                }
            }
        }
    }
}
