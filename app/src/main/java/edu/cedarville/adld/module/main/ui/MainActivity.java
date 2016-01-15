package edu.cedarville.adld.module.main.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import butterknife.Bind;
import butterknife.ButterKnife;
import edu.cedarville.adld.R;
import edu.cedarville.adld.common.base.BaseActivity;
import edu.cedarville.adld.common.dagger.Components;
import edu.cedarville.adld.common.rx.OnNextSubscriber;
import edu.cedarville.adld.module.connection.ui.ConnectionFragment;
import edu.cedarville.adld.module.console.ui.ChartFragment;
import edu.cedarville.adld.module.main.presenter.MainEventHandler;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements
        ConnectionFragment.ConnectionFragmentEventListener {

    @Inject
    MainEventHandler eventHandler;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.frame_main)
    FrameLayout mainFrame;

    private Menu menu;
    private BluetoothSPP bt;

    private ConnectionFragment connectionFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        this.showConnectionFragment();

        this.bt = new BluetoothSPP(this);
        if(!bt.isBluetoothAvailable()) {
            this.connectionFragment.setStatusNotAvailable();
        }


        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
//                textRead.append(message + "\n");
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceDisconnected() {
                connectionFragment.setStatusDisconnected();
                Timber.e("Status : Not connect");
                menu.clear();
                getMenuInflater().inflate(R.menu.menu_connection, menu);
            }

            public void onDeviceConnectionFailed() {
                connectionFragment.setStatusConnectionFailed();
                connectionFragment.setClickableEnabled(true);
                Timber.e("Status : Connection failed");
            }

            public void onDeviceConnected(String name, String address) {
                connectionFragment.setStatusConnected(name);
                Timber.e("Status : Connected to " + name);

                Observable.timer(1500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new OnNextSubscriber<Long>() {
                            @Override
                            public void onNext(Long aLong) {
                                showChartFragment();
                            }
                        });

                menu.clear();
                getMenuInflater().inflate(R.menu.menu_disconnection, menu);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bt.disconnect();
        bt.stopService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }

    @Override
    protected void setupActivityComponent(Components components) {
        components.getAppComponent().inject(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_connection, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_device_connect) {
            bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);
			/*
			if(bt.getServiceState() == BluetoothState.STATE_CONNECTED)
    			bt.disconnect();*/
            Intent intent = new Intent(getApplicationContext(), DeviceList.class);
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
        } else if(id == R.id.menu_disconnect) {
            if(bt.getServiceState() == BluetoothState.STATE_CONNECTED)
                bt.disconnect();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                connectionFragment.setStatusConnecting();
                bt.connect(data);
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    ////
    ////// Connection Fragment Event Listener
    ////
    @Override
    public void onConnectBluetoothPressed() {
        this.connectionFragment.setClickableEnabled(false);
        this.bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);

        Intent intent = new Intent(getApplicationContext(), DeviceList.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    ////
    ////// Class Methods
    ////
    public void showConnectionFragment() {
        this.connectionFragment = new ConnectionFragment();
        this.replaceFragment(connectionFragment);
    }

    public void showChartFragment() {
        this.replaceFragment(new ChartFragment());
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(mainFrame.getId(), fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }
}
