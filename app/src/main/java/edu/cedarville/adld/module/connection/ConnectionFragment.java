package edu.cedarville.adld.module.connection;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.cedarville.adld.R;

public class ConnectionFragment extends Fragment implements ConnectionViewInterface {

    public interface ConnectionViewEventListener {
        void onConnectBluetoothPressed();
        void onViewDestroyed();
    }

    @Bind(R.id.layout_bluetooth_status)
    View statusLayout;

    @Bind(R.id.img_bluetooth_status)
    ImageView statusImage;

    @Bind(R.id.label_bluetooth_status)
    TextView statusLabel;

    private ConnectionViewEventListener eventListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.eventListener.onViewDestroyed();
    }

    @Override
    public void onAttach(Context context) {
        try {
            super.onAttach(context);
            this.eventListener = (ConnectionViewEventListener) getActivity();
        } catch (ClassCastException e) {
            throw new IllegalStateException("ConnectionFragment's host activity must implement ConnectionViewEventListener");
        }
    }



    ////
    ////// ButterKnife Event Injections
    ////
    @OnClick(R.id.layout_bluetooth_status)
    void onBluetoothStatusClicked() {
        this.eventListener.onConnectBluetoothPressed();
    }

    ////
    ////// Connection View Interface
    ////
    @Override
    public void setStatusRetryConnection() {
        this.statusImage.setImageResource(R.drawable.ic_bluetooth_searching_black_48dp);
        this.statusLabel.setText(R.string.status_rety_connecting_to_device);
    }

    @Override
    public void setStatusConnecting() {
        this.statusImage.setImageResource(R.drawable.ic_bluetooth_searching_black_48dp);
        this.statusLabel.setText(R.string.status_connecting_to_device);
    }

    @Override
    public void setStatusConnectionFailed() {
        this.statusImage.setImageResource(R.drawable.ic_bluetooth_disabled_black_48dp);
        this.statusLabel.setText(R.string.status_connection_to_device_failed);
    }

    @Override
    public void setStatusConnected(String name) {
        this.statusImage.setImageResource(R.drawable.ic_bluetooth_connected_black_48dp);
        this.statusLabel.setText(String.format("Connected to %s!", name));
    }

    @Override
    public void setStatusNotAvailable() {
        this.statusImage.setImageResource(R.drawable.ic_bluetooth_disabled_black_48dp);
        this.statusLabel.setText(R.string.status_bluetooth_not_available);
    }

    @Override
    public void setClickableEnabled(boolean enabled) {
        this.statusLayout.setClickable(enabled);
    }
}
