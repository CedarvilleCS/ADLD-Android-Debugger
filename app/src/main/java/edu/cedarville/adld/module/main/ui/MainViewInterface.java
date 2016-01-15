package edu.cedarville.adld.module.main.ui;

public interface MainViewInterface {

    void displayConnectionView();
    void displayConsoleView();

    void informBluetoothNotAvailable();
    void requestToEnableBluetooth();
}
