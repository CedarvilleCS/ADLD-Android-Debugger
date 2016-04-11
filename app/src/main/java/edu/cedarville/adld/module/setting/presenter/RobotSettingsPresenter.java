package edu.cedarville.adld.module.setting.presenter;

import javax.inject.Inject;

import edu.cedarville.adld.common.dagger.scopes.RobotScope;
import edu.cedarville.adld.common.model.Robot;
import edu.cedarville.adld.module.setting.view.RobotSettingsView;
import edu.cedarville.adld.module.setting.view.SettingsViewModel;

public class RobotSettingsPresenter implements RobotSettingsEventHandler {

    //------------------------------------------------------------------------------
    // Dependencies
    //------------------------------------------------------------------------------
    private final Robot robot;


    //------------------------------------------------------------------------------
    // Variables
    //------------------------------------------------------------------------------
    private RobotSettingsView view;


    //------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------
    @RobotScope
    @Inject
    public RobotSettingsPresenter(Robot robot) {
        this.robot = robot;
    }



    //------------------------------------------------------------------------------
    // Robot Settings Event Handler
    //------------------------------------------------------------------------------
    @Override
    public void attachView(RobotSettingsView view) {
        this.view = view;
    }

    @Override
    public void onViewStarted() {
        SettingsViewModel viewModel = new SettingsViewModel.Builder()
                .withRobotName(robot.getRobotName())
                .withRobotAddress(robot.getRobotAddress())
                .withDisplayHex(robot.isDisplayHex())
                .withThreshold(robot.getAverageThreshold())
                .build();

        this.view.setSettingsViewModel(viewModel);
    }

    @Override
    public void onViewStopped() {

    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void saveSettings(String robotName, String threshold, boolean displayHex) {
        this.robot.setRobotName(robotName);
        this.robot.setAverageThreshold(Integer.parseInt(threshold));
        this.robot.setDisplayHex(displayHex);

        this.view.dismissView();
    }
}
