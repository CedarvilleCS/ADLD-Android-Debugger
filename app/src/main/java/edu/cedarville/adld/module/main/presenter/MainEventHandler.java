package edu.cedarville.adld.module.main.presenter;

import android.content.Intent;

import edu.cedarville.adld.module.main.ui.MainView;

public interface MainEventHandler {

    void attachView(MainView view);
    void onViewStarted();
    void onViewDestroyed();
    void onDeviceSelected(Intent data);
    void onBluetoothEnabled();
}
