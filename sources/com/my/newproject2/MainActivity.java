package com.my.newproject2;

import android.app.Activity;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.my.newproject2.BluetoothConnect.BluetoothConnectionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    private BluetoothConnectionListener _bt_bluetooth_connection_listener;
    private Timer _timer = new Timer();
    private BluetoothConnect bt;
    private boolean connsuc = false;
    private String fucku = "";
    private double ilength = 0.0d;
    private LinearLayout linear1;
    private LinearLayout linear2;
    private LinearLayout linear3;
    private LinearLayout linear4;
    private String lukka = "";
    private MediaPlayer mp;
    private double sc1 = 0.0d;
    private double sc2 = 0.0d;
    private double scale = 0.0d;
    private String slength = "";
    private Switch switch1;
    private Switch switch2;
    private TextView textview1;
    private TimerTask ti;
    private Vibrator vib;
    private ScrollView vscroll1;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);
        initialize(bundle);
        initializeLogic();
    }

    private void initialize(Bundle bundle) {
        this.vscroll1 = (ScrollView) findViewById(R.id.vscroll1);
        this.linear1 = (LinearLayout) findViewById(R.id.linear1);
        this.switch1 = (Switch) findViewById(R.id.switch1);
        this.linear2 = (LinearLayout) findViewById(R.id.linear2);
        this.switch2 = (Switch) findViewById(R.id.switch2);
        this.linear3 = (LinearLayout) findViewById(R.id.linear3);
        this.linear4 = (LinearLayout) findViewById(R.id.linear4);
        this.textview1 = (TextView) findViewById(R.id.textview1);
        this.bt = new BluetoothConnect(this);
        this.vib = (Vibrator) getSystemService("vibrator");
        this.switch1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
        this.switch1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    SketchwareUtil.showMessage(MainActivity.this.getApplicationContext(), "Connecting to Arduino");
                    MainActivity.this.bt.startConnection(MainActivity.this._bt_bluetooth_connection_listener, MainActivity.this.fucku, "bt2");
                    return;
                }
                SketchwareUtil.showMessage(MainActivity.this.getApplicationContext(), "Connection Stopped");
                MainActivity.this.bt.stopConnection(MainActivity.this._bt_bluetooth_connection_listener, "bt2");
            }
        });
        this.linear2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
        this.switch2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    SketchwareUtil.showMessage(MainActivity.this.getApplicationContext(), "Timer Started");
                    MainActivity.this.sc1 = (double) MainActivity.this.linear4.getScaleY();
                    MainActivity.this.ti = new TimerTask() {
                        public void run() {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    MainActivity.this.sc2 = (double) MainActivity.this.linear4.getScaleY();
                                    if (Math.abs(MainActivity.this.sc2 - MainActivity.this.sc1) < 20.0d) {
                                        MainActivity.this._notify("Hello", "You need to drink water");
                                        MainActivity.this.vib.vibrate(1000);
                                        MainActivity.this.mp = MediaPlayer.create(MainActivity.this.getApplicationContext(), R.raw.notiwater);
                                        MainActivity.this.mp.start();
                                        MainActivity.this.mp.setLooping(false);
                                        MainActivity.this.bt.sendData(MainActivity.this._bt_bluetooth_connection_listener, "led", "");
                                    }
                                }
                            });
                        }
                    };
                    MainActivity.this._timer.scheduleAtFixedRate(MainActivity.this.ti, 10000, 10000);
                    return;
                }
                SketchwareUtil.showMessage(MainActivity.this.getApplicationContext(), "Timer Stopped");
                MainActivity.this.ti.cancel();
            }
        });
        this.linear3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
        this._bt_bluetooth_connection_listener = new BluetoothConnectionListener() {
            public void onConnected(String str, HashMap<String, Object> hashMap) {
            }

            public void onDataReceived(String str, byte[] bArr, int i) {
                String str2 = new String(bArr, 0, i);
                MainActivity.this.slength = str2;
                MainActivity.this.ilength = Double.parseDouble(MainActivity.this.slength);
                if (MainActivity.this.ilength < 100.0d) {
                    MainActivity.this.linear4.setScaleY((float) MainActivity.this.ilength);
                    MainActivity.this.textview1.setText(str2);
                }
            }

            public void onDataSent(String str, byte[] bArr) {
                String str2 = new String(bArr);
            }

            public void onConnectionError(String str, String str2, String str3) {
            }

            public void onConnectionStopped(String str) {
            }
        };
    }

    private void initializeLogic() {
        this.bt.activateBluetooth();
        this.lukka = "30:4B:07:67:53:58";
        this.fucku = "FC:A8:9A:00:5B:BC";
    }

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    private void _notify(String str, String str2) {
        Builder builder = new Builder(this);
        builder.setSmallIcon(R.drawable.default_image);
        builder.setContentTitle(str);
        builder.setContentText(str2);
        ((NotificationManager) getSystemService("notification")).notify(1, builder.build());
    }

    @Deprecated
    public void showMessage(String str) {
        Toast.makeText(getApplicationContext(), str, 0).show();
    }

    @Deprecated
    public int getLocationX(View view) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        return iArr[0];
    }

    @Deprecated
    public int getLocationY(View view) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        return iArr[1];
    }

    @Deprecated
    public int getRandom(int i, int i2) {
        return new Random().nextInt((i2 - i) + 1) + i;
    }

    @Deprecated
    public ArrayList<Double> getCheckedItemPositionsToArray(ListView listView) {
        ArrayList arrayList = new ArrayList();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        for (int i = 0; i < checkedItemPositions.size(); i++) {
            if (checkedItemPositions.valueAt(i)) {
                arrayList.add(Double.valueOf((double) checkedItemPositions.keyAt(i)));
            }
        }
        return arrayList;
    }

    @Deprecated
    public float getDip(int i) {
        return TypedValue.applyDimension(1, (float) i, getResources().getDisplayMetrics());
    }

    @Deprecated
    public int getDisplayWidthPixels() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    @Deprecated
    public int getDisplayHeightPixels() {
        return getResources().getDisplayMetrics().heightPixels;
    }
}
