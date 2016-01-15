package edu.cedarville.adld.common.dagger.component;

import dagger.Component;
import edu.cedarville.adld.common.dagger.module.AppModule;
import edu.cedarville.adld.common.dagger.module.ChartModule;
import edu.cedarville.adld.common.dagger.module.MainModule;
import edu.cedarville.adld.common.dagger.scope.AppScope;
import edu.cedarville.adld.module.console.ui.ChartFragment;
import edu.cedarville.adld.module.main.ui.MainActivity;

@AppScope
@Component(
        modules = {
                AppModule.class,
                MainModule.class,
                ChartModule.class
        }
)
public interface AppComponent {

    void inject(MainActivity activity);
    void inject(ChartFragment fragment);

}
