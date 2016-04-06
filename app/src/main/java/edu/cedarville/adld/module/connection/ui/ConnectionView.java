package edu.cedarville.adld.module.connection.ui;

import android.content.Context;
import android.content.Intent;

public interface ConnectionView {

    void setStatusBluetoothNotAvailable();
    void setStatusBluetoothConnecting();
    void setStatusBluetoothRetry();
    void setStatusBluetoothFailed();
    void setStatusBluetoothConnected(String deviceName);

    void startIntentForResult(Intent intent, int requestCode);

    Context getViewContext();

}
