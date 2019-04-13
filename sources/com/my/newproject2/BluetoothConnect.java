package com.my.newproject2;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnect {
    private static final String DEFAULT_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private Activity activity;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public interface BluetoothConnectionListener {
        void onConnected(String str, HashMap<String, Object> hashMap);

        void onConnectionError(String str, String str2, String str3);

        void onConnectionStopped(String str);

        void onDataReceived(String str, byte[] bArr, int i);

        void onDataSent(String str, byte[] bArr);
    }

    public BluetoothConnect(Activity activity) {
        this.activity = activity;
    }

    public boolean isBluetoothEnabled() {
        if (this.bluetoothAdapter != null) {
            return true;
        }
        return false;
    }

    public boolean isBluetoothActivated() {
        if (this.bluetoothAdapter == null) {
            return false;
        }
        return this.bluetoothAdapter.isEnabled();
    }

    public void activateBluetooth() {
        this.activity.startActivity(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"));
    }

    public String getRandomUUID() {
        return String.valueOf(UUID.randomUUID());
    }

    public void getPairedDevices(ArrayList<HashMap<String, Object>> arrayList) {
        Set<BluetoothDevice> bondedDevices = this.bluetoothAdapter.getBondedDevices();
        if (bondedDevices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : bondedDevices) {
                HashMap hashMap = new HashMap();
                hashMap.put("name", bluetoothDevice.getName());
                hashMap.put("address", bluetoothDevice.getAddress());
                arrayList.add(hashMap);
            }
        }
    }

    public void readyConnection(BluetoothConnectionListener bluetoothConnectionListener, String str) {
        if (BluetoothController.getInstance().getState().equals(BluetoothController.STATE_NONE)) {
            BluetoothController.getInstance().start(this, bluetoothConnectionListener, str, UUID.fromString(DEFAULT_UUID), this.bluetoothAdapter);
        }
    }

    public void readyConnection(BluetoothConnectionListener bluetoothConnectionListener, String str, String str2) {
        if (BluetoothController.getInstance().getState().equals(BluetoothController.STATE_NONE)) {
            BluetoothController.getInstance().start(this, bluetoothConnectionListener, str2, UUID.fromString(str), this.bluetoothAdapter);
        }
    }

    public void startConnection(BluetoothConnectionListener bluetoothConnectionListener, String str, String str2) {
        BluetoothController.getInstance().connect(this.bluetoothAdapter.getRemoteDevice(str), this, bluetoothConnectionListener, str2, UUID.fromString(DEFAULT_UUID), this.bluetoothAdapter);
    }

    public void startConnection(BluetoothConnectionListener bluetoothConnectionListener, String str, String str2, String str3) {
        BluetoothController.getInstance().connect(this.bluetoothAdapter.getRemoteDevice(str2), this, bluetoothConnectionListener, str3, UUID.fromString(str), this.bluetoothAdapter);
    }

    public void stopConnection(BluetoothConnectionListener bluetoothConnectionListener, String str) {
        BluetoothController.getInstance().stop(this, bluetoothConnectionListener, str);
    }

    public void sendData(BluetoothConnectionListener bluetoothConnectionListener, String str, String str2) {
        String state = BluetoothController.getInstance().getState();
        if (state.equals(BluetoothController.STATE_CONNECTED)) {
            BluetoothController.getInstance().write(str.getBytes());
        } else {
            bluetoothConnectionListener.onConnectionError(str2, state, "Bluetooth is not connected yet");
        }
    }

    public Activity getActivity() {
        return this.activity;
    }
}
