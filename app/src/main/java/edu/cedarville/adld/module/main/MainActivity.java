package edu.cedarville.adld.module.main;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import butterknife.Bind;
import butterknife.ButterKnife;
import edu.cedarville.adld.R;
import edu.cedarville.adld.common.model.DataPoint;
import edu.cedarville.adld.common.rx.OnNextSubscriber;
import edu.cedarville.adld.common.translator.DataPointTranslator;
import edu.cedarville.adld.module.connection.ConnectionFragment;
import edu.cedarville.adld.module.connection.ConnectionViewInterface;
import edu.cedarville.adld.module.chart.ChartFragment;
import edu.cedarville.adld.module.chart.ChartViewInterface;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity implements
        ConnectionFragment.ConnectionViewEventListener,
        ChartFragment.ChartViewEventListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.frame_main)
    FrameLayout mainFrame;

    private DataPointTranslator translator;
    private Menu menu;
    private BluetoothSPP bt;

    private ConnectionViewInterface connectionView;
    private ChartViewInterface chartView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        this.showConnectionFragment();

        this.translator = new DataPointTranslator();
        this.bt = new BluetoothSPP(this);
        if(!bt.isBluetoothAvailable()) {
            this.connectionView.setStatusNotAvailable();
        }


        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                DataPoint dataPoint = translator.translateInputToDataPoint(message);
                chartView.setIncomingDataPoint(dataPoint);
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceDisconnected() {
                menu.clear();
                showConnectionFragment();
                Toast.makeText(MainActivity.this, "Device Disconnected!", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() {
                connectionView.setStatusConnectionFailed();
                connectionView.setClickableEnabled(true);
            }

            public void onDeviceConnected(String name, String address) {
                connectionView.setStatusConnected(name);
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
    public void onBackPressed() {
        if (chartView != null) {
            this.bt.disconnect();
        } else {
            super.onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_disconnect:
                if(bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Called from Selecting a device from DeviceListActivity
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK) {
                connectionView.setStatusConnecting();
                bt.connect(data);
            } else {
                connectionView.setClickableEnabled(true);
            }

        // Called when returning from enabling Bluetooth
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth was not enabled.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    ////
    ////// Connection View Event Listener
    ////
    @Override
    public void onConnectBluetoothPressed() {
        this.connectionView.setClickableEnabled(false);
        this.bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);

        Intent intent = new Intent(getApplicationContext(), DeviceList.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    @Override
    public void onViewDestroyed() {
        this.connectionView = null;
    }


    ////
    ////// Chart View Event Listener
    ////
    @Override
    public void onChartViewDestroyed() {
        this.chartView = null;
    }


    ////
    ////// Class Methods
    ////
    public void showConnectionFragment() {
        ConnectionFragment connectionFragment = new ConnectionFragment();
        this.connectionView = connectionFragment;
        this.replaceFragment(connectionFragment);
    }

    public void showChartFragment() {
        ChartFragment chartFragment = new ChartFragment();
        this.chartView = chartFragment;
        this.replaceFragment(chartFragment);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(mainFrame.getId(), fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }
}