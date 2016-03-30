package edu.cedarville.adld.module.connection.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.cedarville.adld.R;
import edu.cedarville.adld.common.base.BaseActivity;
import edu.cedarville.adld.common.dagger.Components;
import edu.cedarville.adld.module.connection.presenter.ConnectionEventHandler;

public class ConnectionActivity extends BaseActivity implements ConnectionView {


    //------------------------------------------------------------------------------
    // Dependencies
    //------------------------------------------------------------------------------
    @Inject
    ConnectionEventHandler eventHandler;


    //------------------------------------------------------------------------------
    // Views
    //------------------------------------------------------------------------------
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.layout_bluetooth_status)
    View statusLayout;

    @Bind(R.id.img_bluetooth_status)
    ImageView statusImage;

    @Bind(R.id.label_bluetooth_status)
    TextView statusLabel;


    //------------------------------------------------------------------------------
    // Android Lifecycle
    //------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        this.eventHandler.attachView(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.eventHandler.onViewStarted();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.eventHandler.onViewDestroyed();
    }


    //------------------------------------------------------------------------------
    // Abstract Parent Class Implementation
    //------------------------------------------------------------------------------
    @Override
    protected void setupDependencies(Components components) {
        components.getAppComponent().inject(this);
    }


    //------------------------------------------------------------------------------
    // ButterKnife Event Injections
    //------------------------------------------------------------------------------
    @OnClick (R.id.layout_bluetooth_status)
    void onBluetoothStatusClicked() {
        this.onConnectBluetoothPressed();
    }



    //------------------------------------------------------------------------------
    // Activity Intent Results
    //------------------------------------------------------------------------------
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Called from Selecting a device from DeviceListActivity
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK) {
                this.eventHandler.onDeviceSelected(data);
            } else {
                this.statusLayout.setClickable(true);
            }

        // Called when returning from enabling Bluetooth
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                this.eventHandler.onBluetoothEnabled();
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth was not enabled.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    //------------------------------------------------------------------------------
    // Main View Interface
    //------------------------------------------------------------------------------
    @Override
    public void setStatusBluetoothNotAvailable() {
        this.statusImage.setImageResource(R.drawable.ic_bluetooth_disabled_black_48dp);
        this.statusLabel.setText(R.string.status_bluetooth_not_available);
    }

    @Override
    public void setStatusBluetoothConnecting() {
        this.statusImage.setImageResource(R.drawable.ic_bluetooth_searching_black_48dp);
        this.statusLabel.setText(R.string.status_connecting_to_device);
    }

    @Override
    public void setStatusBluetoothRetry() {
        this.statusImage.setImageResource(R.drawable.ic_bluetooth_searching_black_48dp);
        this.statusLabel.setText(R.string.status_rety_connecting_to_device);
    }

    @Override
    public void setStatusBluetoothFailed() {
        this.statusImage.setImageResource(R.drawable.ic_bluetooth_disabled_black_48dp);
        this.statusLabel.setText(R.string.status_connection_to_device_failed);
    }

    @Override
    public void setStatusBluetoothConnected(String deviceName) {
        this.statusImage.setImageResource(R.drawable.ic_bluetooth_connected_black_48dp);
        this.statusLabel.setText(String.format("Connected to %s!", deviceName));
    }

    @Override
    public void startIntentForResult(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }

    @Override
    public Context getViewContext() {
        return this;
    }



    //------------------------------------------------------------------------------
    // Utility
    //------------------------------------------------------------------------------
    private void onConnectBluetoothPressed() {
        Intent intent = new Intent(getApplicationContext(), DeviceList.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }



}
