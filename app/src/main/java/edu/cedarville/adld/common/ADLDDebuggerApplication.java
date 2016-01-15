package edu.cedarville.adld.common;

import android.app.Application;
import android.content.Context;

import edu.cedarville.adld.common.dagger.Components;
import edu.cedarville.adld.common.dagger.component.AppComponent;
import edu.cedarville.adld.common.dagger.component.DaggerAppComponent;
import edu.cedarville.adld.common.dagger.module.AppModule;

public class ADLDDebuggerApplication extends Application implements Components {

    /**
     * Fetches the Components instance for Dagger Injections used throughout Activities and Fragments
     * @param context   -   Context of the activity/fragment to inject into
     * @return  -   Components instance used for injection
     */
    public static Components get(Context context) {
        return (Components) context.getApplicationContext();
    }


    private AppComponent appComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        this.appComponent = DaggerAppComponent.builder().
                appModule(new AppModule())
                .build();

    }


    ////
    ////// Components Interface
    ////
    @Override
    public AppComponent getAppComponent() {
        return appComponent;
    }
}
