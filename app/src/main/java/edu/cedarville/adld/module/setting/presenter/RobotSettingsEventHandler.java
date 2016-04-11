package edu.cedarville.adld.module.setting.presenter;

import edu.cedarville.adld.module.setting.view.RobotSettingsView;

public interface RobotSettingsEventHandler {

    void attachView(RobotSettingsView view);
    void onViewStarted();
    void onViewStopped();
    void detachView();

    void saveSettings(String robotName, String threshold, boolean displayHex);


}
