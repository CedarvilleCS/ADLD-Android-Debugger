package edu.cedarville.adld.module.main.presenter;

import android.content.Intent;

import javax.inject.Inject;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import edu.cedarville.adld.common.dagger.scope.AppScope;
import edu.cedarville.adld.common.model.DataPoint;
import edu.cedarville.adld.module.main.logic.MainInteractor;
import edu.cedarville.adld.module.main.ui.MainViewInterface;
import rx.Subscriber;
import rx.Subscription;
import rx.observers.SafeSubscriber;
import timber.log.Timber;

public class MainPresenter implements MainEventHandler {

    private final MainInteractor interactor;

    private MainViewInterface view;
    private Subscription inputSubscription;

    @Inject @AppScope
    public MainPresenter(MainInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void attachView(MainViewInterface view) {
        this.view = view;
    }

    @Override
    public void requestUpdateView() {
        // Device doesn't have Bluetooth capabilities. Inform User
        if (!this.interactor.isBluetoothAvailable()) {
            this.view.informBluetoothNotAvailable();
        } else {
            // Bluetooth on the device is turned on, move to connect a device
            if (this.interactor.isBluetoothEnabled()) {
                this.view.displayConnectionView();

            // Bluetooth on the device is turned off. Request to turn it on
            } else {
                this.view.requestToEnableBluetooth();
            }
        }
    }

    @Override
    public void detachView() {
        this.view = null;
        this.interactor.disableBluetooth();
        if (inputSubscription != null && !inputSubscription.isUnsubscribed()) {
            inputSubscription.unsubscribe();
            inputSubscription = null;
        }
    }

    @Override
    public void onEnableBluetooth() {
        this.interactor.enableBluetooth();
        this.view.displayConnectionView();
    }

    @Override
    public void onConnectToDevice(Intent data) {
        this.interactor.connectToDevice(data, new BluetoothSPP.BluetoothConnectionListener() {
            @Override
            public void onDeviceConnected(String name, String address) {
                Timber.e("On Device Connected");
            }

            @Override
            public void onDeviceDisconnected() {
                Timber.e("On Device Disconnected");
            }

            @Override
            public void onDeviceConnectionFailed() {
                Timber.e("On Device Connection failed");
            }
        });

    }

    ////
    ////// Class Helper Methods
    ////
    public void observeIncomingData() {
        this.inputSubscription = this.interactor.getInputObservable().subscribe(new SafeSubscriber<>(new Subscriber<DataPoint>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(DataPoint dataPoint) {

            }
        }));
    }
}
