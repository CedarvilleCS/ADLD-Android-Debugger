package edu.cedarville.adld.module.connection.presenter;

import android.content.Intent;

import edu.cedarville.adld.module.connection.ui.ConnectionView;

public interface ConnectionEventHandler {

    void attachView(ConnectionView view);
    void onViewStarted();
    void onViewDestroyed();
    void onDeviceSelected(Intent data);
    void onBluetoothEnabled();
}
