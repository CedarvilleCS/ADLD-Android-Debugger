package edu.cedarville.adld.module.main.logic;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import javax.inject.Inject;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import edu.cedarville.adld.common.dagger.scope.AppScope;
import edu.cedarville.adld.common.model.DataPoint;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

public class MainInteractor {

    private final BluetoothSPP bluetoothSPP;
    private final BluetoothAdapter bluetoothAdapter;

    @Inject @AppScope
    public MainInteractor(BluetoothSPP bluetoothSPP, BluetoothAdapter bluetoothAdapter) {
        this.bluetoothSPP = bluetoothSPP;
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public boolean isBluetoothAvailable() {
        return this.bluetoothSPP.isBluetoothAvailable();
    }

    public boolean isBluetoothEnabled() {
        return this.bluetoothSPP.isBluetoothEnabled();
    }

    public void enableBluetooth() {
        this.bluetoothSPP.enable();
    }

    public void disableBluetooth() {
        this.bluetoothSPP.disconnect();
        this.bluetoothAdapter.disable();
    }

    public void connectToDevice(Intent data, BluetoothSPP.BluetoothConnectionListener listener) {
        bluetoothSPP.setBluetoothConnectionListener(listener);
        bluetoothSPP.setupService();
        bluetoothSPP.startService(BluetoothState.DEVICE_OTHER);
        bluetoothSPP.connect(data);
    }

    public Observable<DataPoint> getInputObservable() {
        return Observable.create(new Observable.OnSubscribe<DataPoint>() {
            @Override
            public void call(Subscriber<? super DataPoint> subscriber) {
                bluetoothSPP.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
                    @Override
                    public void onDataReceived(byte[] data, String message) {
                        Timber.e("Data: %s Message: %s", data, message);
                    }
                });
            }
        });
    }
}
