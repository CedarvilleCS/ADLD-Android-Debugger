package edu.cedarville.adld.common.dagger.components;

import dagger.Subcomponent;
import edu.cedarville.adld.common.dagger.modules.RobotModule;
import edu.cedarville.adld.common.dagger.scopes.RobotScope;
import edu.cedarville.adld.module.robot.shared.view.RobotDebuggerActivity;

@RobotScope
@Subcomponent(
        modules = {
                RobotModule.class
        }
)
public interface RobotComponent {

    void inject(RobotDebuggerActivity activity);
}
