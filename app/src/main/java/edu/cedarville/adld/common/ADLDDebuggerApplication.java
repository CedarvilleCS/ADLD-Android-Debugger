package edu.cedarville.adld.common;

import android.app.Application;
import android.content.Context;

import edu.cedarville.adld.common.dagger.Components;
import edu.cedarville.adld.common.dagger.components.AppComponent;
import edu.cedarville.adld.common.dagger.components.DaggerAppComponent;
import edu.cedarville.adld.common.dagger.components.RobotComponent;
import edu.cedarville.adld.common.dagger.modules.AppModule;
import edu.cedarville.adld.common.dagger.modules.RobotModule;
import edu.cedarville.adld.common.model.Robot;
import timber.log.Timber;

public class ADLDDebuggerApplication extends Application implements Components {

    /**
     * Easy accessor method to get access to the Application level components for dependency injections
     * @param context   Context the components are being applied to
     * @return  Components interface which provides components fro injection
     */
    public static Components get(Context context) {
        return (Components) context.getApplicationContext();
    }

    //------------------------------------------------------------------------------
    // Application Level
    //------------------------------------------------------------------------------
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        this.appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this, this))
                .build();

    }


    //------------------------------------------------------------------------------
    // Available Components
    //------------------------------------------------------------------------------
    private AppComponent appComponent;
    private RobotComponent robotComponent;


    //------------------------------------------------------------------------------
    // Access Created Components
    //------------------------------------------------------------------------------
    @Override
    public AppComponent getAppComponent() {
        return this.appComponent;
    }

    @Override
    public RobotComponent getRobotComponent() {
        return this.robotComponent;
    }

    //------------------------------------------------------------------------------
    // Create New Scoped Components
    //------------------------------------------------------------------------------
    @Override
    public void createRobotComponent(Robot robot) {
        this.robotComponent = this.appComponent.plus(new RobotModule(robot));
    }

    //------------------------------------------------------------------------------
    // End Existing Scopes
    //------------------------------------------------------------------------------
    @Override
    public void endRobotScope() {
        this.robotComponent = null;
    }
}
