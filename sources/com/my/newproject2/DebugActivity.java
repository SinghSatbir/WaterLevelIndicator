package com.my.newproject2;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

public class DebugActivity extends Activity {
    String[] errMessage = new String[]{"Invalid string operation\n", "Invalid list operation\n", "Invalid arithmetical operation\n", "Invalid toNumber block operation\n", "Invalid intent operation"};
    String[] exceptionType = new String[]{"StringIndexOutOfBoundsException", "IndexOutOfBoundsException", "ArithmeticException", "NumberFormatException", "ActivityNotFoundException"};

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Missing block: B:7:0x0023, code skipped:
            if (r1.isEmpty() != false) goto L_0x0025;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCreate(Bundle bundle) {
        CharSequence stringExtra;
        Object stringExtra2;
        int i = 0;
        super.onCreate(bundle);
        Intent intent = getIntent();
        String str = "";
        str = "";
        if (intent != null) {
            stringExtra2 = intent.getStringExtra("error");
            String[] split = stringExtra2.split("\n");
            while (i < this.exceptionType.length) {
                try {
                    if (split[0].contains(this.exceptionType[i])) {
                        str = new StringBuilder(String.valueOf(this.errMessage[i])).append(split[0].substring(this.exceptionType[i].length() + split[0].indexOf(this.exceptionType[i]), split[0].length())).toString();
                        break;
                    }
                    i++;
                } catch (Exception e) {
                    stringExtra2 = str;
                }
            }
        }
        stringExtra2 = str;
        Builder builder = new Builder(this);
        builder.setTitle("An error occured");
        builder.setMessage(stringExtra2);
        builder.setNeutralButton("End Application", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                DebugActivity.this.finish();
            }
        });
        builder.create().show();
    }
}
