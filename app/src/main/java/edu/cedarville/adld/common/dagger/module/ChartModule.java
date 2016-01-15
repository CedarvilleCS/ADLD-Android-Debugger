package edu.cedarville.adld.common.dagger.module;

import dagger.Module;
import dagger.Provides;
import edu.cedarville.adld.common.dagger.scope.AppScope;
import edu.cedarville.adld.module.console.presenter.ChartEventHandler;
import edu.cedarville.adld.module.console.presenter.ChartPresenter;

@Module
public class ChartModule {

    @Provides
    @AppScope
    public ChartEventHandler providesChartEventHandler(ChartPresenter presenter) {
        return presenter;
    }
}
