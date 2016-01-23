package edu.cedarville.adld.common.dagger.modules;

import dagger.Module;
import dagger.Provides;
import edu.cedarville.adld.common.dagger.scopes.RobotScope;
import edu.cedarville.adld.common.model.Robot;
import edu.cedarville.adld.module.robot.shared.presenter.RobotDebuggerEventHandler;
import edu.cedarville.adld.module.robot.shared.presenter.RobotDebuggerPresenter;

@Module
public class RobotModule {

    private final Robot robot;

    public RobotModule(Robot robot) {
        this.robot = robot;
    }

    @Provides @RobotScope
    public Robot providesRobot() {
        return this.robot;
    }

    @Provides @RobotScope
    public RobotDebuggerEventHandler providesRobotDebuggerEventHandler(RobotDebuggerPresenter presenter) {
        return presenter;
    }
}
