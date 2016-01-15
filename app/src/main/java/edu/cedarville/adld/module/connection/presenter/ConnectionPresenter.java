package edu.cedarville.adld.module.connection.presenter;

import edu.cedarville.adld.module.connection.ui.ConnectionViewInterface;

public class ConnectionPresenter implements ConnectionEventHandler {

    private ConnectionViewInterface view;
//    private

    @Override
    public void attachView(ConnectionViewInterface view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void onConnectBluetoothPressed() {

    }
}
