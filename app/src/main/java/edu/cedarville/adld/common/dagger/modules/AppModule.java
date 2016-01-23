package edu.cedarville.adld.common.dagger.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import edu.cedarville.adld.common.dagger.Components;
import edu.cedarville.adld.common.dagger.scopes.AppScope;
import edu.cedarville.adld.module.main.presenter.MainEventHandler;
import edu.cedarville.adld.module.main.presenter.MainPresenter;

@Module
public class AppModule {

    private final Context appContext;
    private final Components appComponents;

    public AppModule(Context appContext, Components appComponents) {
        this.appContext = appContext;
        this.appComponents = appComponents;
    }

    @Provides
    @AppScope
    public Context providesAppContext() {
        return this.appContext;
    }

    @Provides
    @AppScope
    public Components providesAppComponents() {
        return this.appComponents;
    }

    @Provides
    @AppScope
    public MainEventHandler providesMainEventHandler(MainPresenter presenter) {
        return presenter;
    }
}
