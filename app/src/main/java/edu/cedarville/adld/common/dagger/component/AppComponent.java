package edu.cedarville.adld.common.dagger.component;

import dagger.Component;
import edu.cedarville.adld.common.dagger.module.AppModule;
import edu.cedarville.adld.common.dagger.scope.AppScope;

@AppScope
@Component(
        modules = {
                AppModule.class
        }
)
public interface AppComponent {



}
