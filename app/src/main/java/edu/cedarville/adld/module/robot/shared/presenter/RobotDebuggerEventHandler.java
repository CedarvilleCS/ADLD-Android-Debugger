package edu.cedarville.adld.module.robot.shared.presenter;

import edu.cedarville.adld.module.robot.shared.view.RobotDebuggerView;

public interface RobotDebuggerEventHandler {

    void attachView(RobotDebuggerView view);
    void onViewStarted();
    void onViewStopped();
    void detachView();

    void onPlayPressed();
    void onPausePressed();

}
