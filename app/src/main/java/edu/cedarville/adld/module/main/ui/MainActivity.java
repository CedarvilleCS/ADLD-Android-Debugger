package edu.cedarville.adld.module.main.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.Toast;

import javax.inject.Inject;

import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import butterknife.Bind;
import butterknife.ButterKnife;
import edu.cedarville.adld.R;
import edu.cedarville.adld.common.base.BaseActivity;
import edu.cedarville.adld.common.dagger.Components;
import edu.cedarville.adld.module.connection.ConnectionFragment;
import edu.cedarville.adld.module.connection.ConnectionViewInterface;
import edu.cedarville.adld.module.main.presenter.MainEventHandler;

public class MainActivity extends BaseActivity implements MainView,
        ConnectionFragment.ConnectionViewEventListener {


    //------------------------------------------------------------------------------
    // Dependencies
    //------------------------------------------------------------------------------
    @Inject
    MainEventHandler eventHandler;


    //------------------------------------------------------------------------------
    // Views
    //------------------------------------------------------------------------------
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.frame_main)
    FrameLayout mainFrame;


    //------------------------------------------------------------------------------
    // Variables
    //------------------------------------------------------------------------------
    private ConnectionViewInterface connectionView;


    //------------------------------------------------------------------------------
    // Android Lifecycle
    //------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        this.eventHandler.attachView(this);

        this.replaceFragment(new ConnectionFragment());
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
    // Activity Intent Results
    //------------------------------------------------------------------------------
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Called from Selecting a device from DeviceListActivity
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK) {
                this.eventHandler.onDeviceSelected(data);
            } else {
                connectionView.setClickableEnabled(true);
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
        if (connectionView != null) {
            this.connectionView.setStatusNotAvailable();
        }
    }

    @Override
    public void setStatusBluetoothConnecting() {
        this.connectionView.setStatusConnecting();
    }

    @Override
    public void setStatusBluetoothRetry() {
        this.connectionView.setStatusRetryConnection();
    }

    @Override
    public void setStatusBluetoothFailed() {
        this.connectionView.setStatusConnectionFailed();
    }

    @Override
    public void setStatusBluetoothConnected(String deviceName) {
        this.connectionView.setStatusConnected(deviceName);
    }

    @Override
    public void showConnectionView() {
        this.replaceFragment(new ConnectionFragment());
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
    // Connection View Event Listener
    //------------------------------------------------------------------------------
    @Override
    public void onConnectBluetoothPressed() {
        Intent intent = new Intent(getApplicationContext(), DeviceList.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    @Override
    public void onConnectionViewCreated(ConnectionViewInterface view) {
        this.connectionView = view;

    }

    @Override
    public void onConnectionViewDestroyed() {
        this.connectionView = null;
    }


    //------------------------------------------------------------------------------
    // Private Methods
    //------------------------------------------------------------------------------
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(mainFrame.getId(), fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }


}
