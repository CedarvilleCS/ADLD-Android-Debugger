package edu.cedarville.adld.common.dagger.module;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import dagger.Module;
import dagger.Provides;
import edu.cedarville.adld.common.dagger.Components;
import edu.cedarville.adld.common.dagger.scope.AppScope;

@Module
public class AppModule {

    private final Context context;
    private final Components components;


    public AppModule(Context context, Components components) {
        this.context = context;
        this.components = components;
    }


    @Provides
    @AppScope
    public Context providesApplicationContext() {
        return this.context;
    }


    @Provides
    @AppScope
    public Components providesApplicationComponents() {
        return this.components;
    }


    @Provides
    @AppScope
    public BluetoothSPP providesBluetoothSpp(Context context) {
        return new BluetoothSPP(context);
    }

    @Provides
    @AppScope
    public BluetoothAdapter providesBluetoothAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

}
