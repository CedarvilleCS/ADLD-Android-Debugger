package edu.cedarville.adld.module.main.presenter;

import android.content.Intent;

import edu.cedarville.adld.module.main.ui.MainViewInterface;

public interface MainEventHandler {

    void attachView(MainViewInterface view);
    void requestUpdateView();
    void detachView();

    void onEnableBluetooth();
    void onConnectToDevice(Intent data);
}
