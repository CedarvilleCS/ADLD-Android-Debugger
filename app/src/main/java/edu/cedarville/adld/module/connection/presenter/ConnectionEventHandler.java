package edu.cedarville.adld.module.connection.presenter;

import edu.cedarville.adld.module.connection.ui.ConnectionViewInterface;

public interface ConnectionEventHandler {

    void attachView(ConnectionViewInterface view);
    void detachView();
    void onConnectBluetoothPressed();
}
