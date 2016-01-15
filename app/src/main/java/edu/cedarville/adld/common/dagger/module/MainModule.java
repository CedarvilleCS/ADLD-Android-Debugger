package edu.cedarville.adld.common.dagger.module;

import dagger.Module;
import dagger.Provides;
import edu.cedarville.adld.common.dagger.scope.AppScope;
import edu.cedarville.adld.module.main.presenter.MainEventHandler;
import edu.cedarville.adld.module.main.presenter.MainPresenter;

@Module
public class MainModule {

    @Provides
    @AppScope
    public MainEventHandler providesMainEventHandler(MainPresenter presenter) {
        return presenter;
    }

}
