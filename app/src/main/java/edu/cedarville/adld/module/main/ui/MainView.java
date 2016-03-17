package edu.cedarville.adld.module.main.ui;

import android.content.Context;
import android.content.Intent;

public interface MainView {

    void setStatusBluetoothNotAvailable();
    void setStatusBluetoothConnecting();
    void setStatusBluetoothRetry();
    void setStatusBluetoothFailed();
    void setStatusBluetoothConnected(String deviceName);

    void startIntentForResult(Intent intent, int requestCode);
    void showConnectionView();

    Context getViewContext();

}
