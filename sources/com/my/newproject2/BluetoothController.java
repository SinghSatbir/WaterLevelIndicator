package com.my.newproject2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import com.my.newproject2.BluetoothConnect.BluetoothConnectionListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.UUID;

public class BluetoothController {
    public static final String STATE_CONNECTED = "connected";
    public static final String STATE_CONNECTING = "connecting";
    public static final String STATE_LISTEN = "listen";
    public static final String STATE_NONE = "none";
    private static BluetoothController instance;
    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;
    private String state = STATE_NONE;

    private class AcceptThread extends Thread {
        private BluetoothConnect bluetoothConnect;
        private BluetoothConnectionListener listener;
        private BluetoothServerSocket serverSocket;
        private String tag;

        public AcceptThread(BluetoothConnect bluetoothConnect, BluetoothConnectionListener bluetoothConnectionListener, String str, UUID uuid, BluetoothAdapter bluetoothAdapter) {
            this.bluetoothConnect = bluetoothConnect;
            this.listener = bluetoothConnectionListener;
            this.tag = str;
            try {
                this.serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(str, uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            BluetoothController.this.state = BluetoothController.STATE_LISTEN;
        }

        /* JADX WARNING: Missing block: B:19:0x0037, code skipped:
            if (r0.equals(com.my.newproject2.BluetoothController.STATE_LISTEN) != false) goto L_0x0039;
     */
        /* JADX WARNING: Missing block: B:25:0x0058, code skipped:
            if (r0.equals(com.my.newproject2.BluetoothController.STATE_CONNECTED) != false) goto L_0x005a;
     */
        /* JADX WARNING: Missing block: B:27:?, code skipped:
            r1.close();
     */
        /* JADX WARNING: Missing block: B:28:0x005e, code skipped:
            r0 = move-exception;
     */
        /* JADX WARNING: Missing block: B:30:?, code skipped:
            r0.printStackTrace();
     */
        /* JADX WARNING: Missing block: B:32:0x0069, code skipped:
            if (r0.equals(com.my.newproject2.BluetoothController.STATE_NONE) == false) goto L_0x0027;
     */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            while (!BluetoothController.this.state.equals(BluetoothController.STATE_CONNECTED)) {
                try {
                    BluetoothSocket accept = this.serverSocket.accept();
                    if (accept != null) {
                        synchronized (BluetoothController.this) {
                            String access$1 = BluetoothController.this.state;
                            switch (access$1.hashCode()) {
                                case -1102508601:
                                    break;
                                case -775651656:
                                    if (!access$1.equals(BluetoothController.STATE_CONNECTING)) {
                                        break;
                                    }
                                case -579210487:
                                    break;
                                case 3387192:
                                    break;
                            }
                            BluetoothController.this.connected(accept, accept.getRemoteDevice(), this.bluetoothConnect, this.listener, this.tag);
                        }
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }

        public void cancel() {
            try {
                this.serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothAdapter bluetoothAdapter;
        private BluetoothConnect bluetoothConnect;
        private BluetoothDevice device;
        private BluetoothConnectionListener listener;
        private BluetoothSocket socket;
        private String tag;

        public ConnectThread(BluetoothDevice bluetoothDevice, BluetoothConnect bluetoothConnect, BluetoothConnectionListener bluetoothConnectionListener, String str, UUID uuid, BluetoothAdapter bluetoothAdapter) {
            this.device = bluetoothDevice;
            this.bluetoothConnect = bluetoothConnect;
            this.listener = bluetoothConnectionListener;
            this.tag = str;
            this.bluetoothAdapter = bluetoothAdapter;
            try {
                this.socket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            BluetoothController.this.state = BluetoothController.STATE_CONNECTING;
        }

        public void run() {
            this.bluetoothAdapter.cancelDiscovery();
            try {
                this.socket.connect();
                synchronized (BluetoothController.this) {
                    BluetoothController.this.connectThread = null;
                }
                BluetoothController.this.connected(this.socket, this.device, this.bluetoothConnect, this.listener, this.tag);
            } catch (Exception e) {
                try {
                    this.socket.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                BluetoothController.this.connectionFailed(this.bluetoothConnect, this.listener, this.tag, e.getMessage());
            }
        }

        public void cancel() {
            try {
                this.socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectedThread extends Thread {
        private BluetoothConnect bluetoothConnect;
        private InputStream inputStream;
        private BluetoothConnectionListener listener;
        private OutputStream outputStream;
        private BluetoothSocket socket;
        private String tag;

        public ConnectedThread(BluetoothSocket bluetoothSocket, BluetoothConnect bluetoothConnect, BluetoothConnectionListener bluetoothConnectionListener, String str) {
            this.bluetoothConnect = bluetoothConnect;
            this.listener = bluetoothConnectionListener;
            this.tag = str;
            this.socket = bluetoothSocket;
            try {
                this.inputStream = bluetoothSocket.getInputStream();
                this.outputStream = bluetoothSocket.getOutputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
            BluetoothController.this.state = BluetoothController.STATE_CONNECTED;
        }

        public void run() {
            while (BluetoothController.this.state.equals(BluetoothController.STATE_CONNECTED)) {
                try {
                    final byte[] bArr = new byte[1024];
                    final int read = this.inputStream.read(bArr);
                    this.bluetoothConnect.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            ConnectedThread.this.listener.onDataReceived(ConnectedThread.this.tag, bArr, read);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    BluetoothController.this.connectionLost(this.bluetoothConnect, this.listener, this.tag);
                    return;
                }
            }
        }

        public void write(final byte[] bArr) {
            try {
                this.outputStream.write(bArr);
                this.bluetoothConnect.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ConnectedThread.this.listener.onDataSent(ConnectedThread.this.tag, bArr);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                this.socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized BluetoothController getInstance() {
        BluetoothController bluetoothController;
        synchronized (BluetoothController.class) {
            if (instance == null) {
                instance = new BluetoothController();
            }
            bluetoothController = instance;
        }
        return bluetoothController;
    }

    public synchronized void start(BluetoothConnect bluetoothConnect, BluetoothConnectionListener bluetoothConnectionListener, String str, UUID uuid, BluetoothAdapter bluetoothAdapter) {
        if (this.connectThread != null) {
            this.connectThread.cancel();
            this.connectThread = null;
        }
        if (this.connectedThread != null) {
            this.connectedThread.cancel();
            this.connectedThread = null;
        }
        if (this.acceptThread != null) {
            this.acceptThread.cancel();
            this.acceptThread = null;
        }
        this.acceptThread = new AcceptThread(bluetoothConnect, bluetoothConnectionListener, str, uuid, bluetoothAdapter);
        this.acceptThread.start();
    }

    public synchronized void connect(BluetoothDevice bluetoothDevice, BluetoothConnect bluetoothConnect, BluetoothConnectionListener bluetoothConnectionListener, String str, UUID uuid, BluetoothAdapter bluetoothAdapter) {
        if (this.state.equals(STATE_CONNECTING) && this.connectThread != null) {
            this.connectThread.cancel();
            this.connectThread = null;
        }
        if (this.connectedThread != null) {
            this.connectedThread.cancel();
            this.connectedThread = null;
        }
        this.connectThread = new ConnectThread(bluetoothDevice, bluetoothConnect, bluetoothConnectionListener, str, uuid, bluetoothAdapter);
        this.connectThread.start();
    }

    public synchronized void connected(BluetoothSocket bluetoothSocket, final BluetoothDevice bluetoothDevice, BluetoothConnect bluetoothConnect, final BluetoothConnectionListener bluetoothConnectionListener, final String str) {
        if (this.connectThread != null) {
            this.connectThread.cancel();
            this.connectThread = null;
        }
        if (this.connectedThread != null) {
            this.connectedThread.cancel();
            this.connectedThread = null;
        }
        if (this.acceptThread != null) {
            this.acceptThread.cancel();
            this.acceptThread = null;
        }
        this.connectedThread = new ConnectedThread(bluetoothSocket, bluetoothConnect, bluetoothConnectionListener, str);
        this.connectedThread.start();
        bluetoothConnect.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                HashMap hashMap = new HashMap();
                hashMap.put("name", bluetoothDevice.getName());
                hashMap.put("address", bluetoothDevice.getAddress());
                bluetoothConnectionListener.onConnected(str, hashMap);
            }
        });
    }

    public synchronized void stop(BluetoothConnect bluetoothConnect, final BluetoothConnectionListener bluetoothConnectionListener, final String str) {
        if (this.connectThread != null) {
            this.connectThread.cancel();
            this.connectThread = null;
        }
        if (this.connectedThread != null) {
            this.connectedThread.cancel();
            this.connectedThread = null;
        }
        if (this.acceptThread != null) {
            this.acceptThread.cancel();
            this.acceptThread = null;
        }
        this.state = STATE_NONE;
        bluetoothConnect.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                bluetoothConnectionListener.onConnectionStopped(str);
            }
        });
    }

    public void write(byte[] bArr) {
        synchronized (this) {
            if (this.state.equals(STATE_CONNECTED)) {
                ConnectedThread connectedThread = this.connectedThread;
                connectedThread.write(bArr);
                return;
            }
        }
    }

    public void connectionFailed(BluetoothConnect bluetoothConnect, final BluetoothConnectionListener bluetoothConnectionListener, final String str, final String str2) {
        this.state = STATE_NONE;
        bluetoothConnect.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                bluetoothConnectionListener.onConnectionError(str, BluetoothController.this.state, str2);
            }
        });
    }

    public void connectionLost(BluetoothConnect bluetoothConnect, final BluetoothConnectionListener bluetoothConnectionListener, final String str) {
        this.state = STATE_NONE;
        bluetoothConnect.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                bluetoothConnectionListener.onConnectionError(str, BluetoothController.this.state, "Bluetooth connection is disconnected");
            }
        });
    }

    public String getState() {
        return this.state;
    }
}
