package edu.cedarville.adld.module.robot.shared.presenter;

import edu.cedarville.adld.module.robot.shared.view.RobotDebuggerView;

public interface RobotDebuggerEventHandler {

    //------------------------------------------------------------------------------
    // Methods for Attaching Event Handler to Lifecycle
    //------------------------------------------------------------------------------
    void attachView(RobotDebuggerView view);
    void onViewStarted();
    void onViewStopped();
    void detachView();


    //------------------------------------------------------------------------------
    // Methods for handling events on the View
    //------------------------------------------------------------------------------
    void onPlayPressed();
    void onPausePressed();
    void onDisconnectPressed();
    void onSettingsPressed();
    void onChartPressed();
    void onConsolePressed();

}
