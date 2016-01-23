package edu.cedarville.adld.common.dagger.components;

import dagger.Component;
import edu.cedarville.adld.common.dagger.modules.AppModule;
import edu.cedarville.adld.common.dagger.modules.RobotModule;
import edu.cedarville.adld.common.dagger.scopes.AppScope;
import edu.cedarville.adld.module.main.ui.MainActivity;

@AppScope
@Component(
        modules = {
                AppModule.class
        }
)
public interface AppComponent {

    //------------------------------------------------------------------------------
    // Injection Methods
    //------------------------------------------------------------------------------
    void inject(MainActivity activity);


    //------------------------------------------------------------------------------
    // Sub Component Methods
    //------------------------------------------------------------------------------
    RobotComponent plus(RobotModule module);
}
