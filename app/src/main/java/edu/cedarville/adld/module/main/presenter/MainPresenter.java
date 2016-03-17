package edu.cedarville.adld.module.main.presenter;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import edu.cedarville.adld.common.model.Robot;
import edu.cedarville.adld.common.rx.OnNextSubscriber;
import edu.cedarville.adld.common.utility.Navigator;
import edu.cedarville.adld.module.main.ui.MainView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class MainPresenter implements MainEventHandler {


    //------------------------------------------------------------------------------
    // Dependencies
    //------------------------------------------------------------------------------
    private final BluetoothSPP bt;
    private final Navigator navigator;


    //------------------------------------------------------------------------------
    // Variables
    //------------------------------------------------------------------------------
    private Intent connectionData;
    private MainView view;


    @Inject
    public MainPresenter(Context context, Navigator navigator) {
        this.navigator = navigator;
        this.bt = new BluetoothSPP(context);
        this.bt.setBluetoothConnectionListener(new ConnectionListener());
    }



    //------------------------------------------------------------------------------
    // Main Event Handler Interface
    //------------------------------------------------------------------------------
    @Override
    public void attachView(MainView view) {
        this.view = view;
    }

    @Override
    public void onViewStarted() {
        if (!bt.isBluetoothAvailable()) {
            this.view.setStatusBluetoothNotAvailable();
        }

        if (bt.isBluetoothEnabled()) {
            if (!bt.isServiceAvailable()) {
                this.onBluetoothEnabled();
            }
        } else {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            this.view.startIntentForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onViewDestroyed() {
        this.bt.disconnect();
        this.bt.stopService();
    }

//    @Override
//    public void onDisconnectPressed() {
//        if(bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
//            bt.disconnect();
//        }
//    }

    @Override
    public void onDeviceSelected(Intent data) {
        this.view.setStatusBluetoothConnecting();
        this.connectionData = data;
        this.bt.connect(connectionData);
    }

    @Override
    public void onBluetoothEnabled() {
        bt.setupService();
        bt.startService(BluetoothState.DEVICE_OTHER);
    }
    

    //------------------------------------------------------------------------------
    // Bluetooth Connection Listener
    //------------------------------------------------------------------------------
    private class ConnectionListener implements BluetoothSPP.BluetoothConnectionListener {

        private int maxRetryCount = 2;
        private int retryCount = 0;

        @Override
        public void onDeviceConnected(String name, String address) {
            retryCount = 0;
            connectionData = null;
            view.setStatusBluetoothConnected(name);
            delayThenShowChartView();
        }

        @Override
        public void onDeviceDisconnected() {
            view.showConnectionView();
            Toast.makeText(view.getViewContext(), "Device Disconnected!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDeviceConnectionFailed() {
            if (retryCount < maxRetryCount) {
                view.setStatusBluetoothRetry();
                bt.connect(connectionData);
                retryCount++;
            } else {
                view.setStatusBluetoothFailed();
                retryCount = 0;
            }
        }
    }


    //------------------------------------------------------------------------------
    // Private Class Methods
    //------------------------------------------------------------------------------
    private void delayThenShowChartView() {
        Observable.timer(1500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new OnNextSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        showRobotDebugger();
                    }
                });
    }

    private void showRobotDebugger() {
        navigator.navigateToRobotDebugger(view.getViewContext(), new Robot(bt));
    }

}
