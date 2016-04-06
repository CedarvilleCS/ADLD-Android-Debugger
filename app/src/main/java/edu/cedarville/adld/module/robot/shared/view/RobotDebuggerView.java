package edu.cedarville.adld.module.robot.shared.view;

import android.content.Context;

import edu.cedarville.adld.common.model.ConsoleOutput;
import edu.cedarville.adld.common.model.SensorData;

public interface RobotDebuggerView {

    void switchToConsoleView();
    void switchToChartView();

    void setStatePlaying(boolean isPlaying);

    void printOutput(ConsoleOutput output);
    void showSensorData(SensorData data);
    void dismissView();

    Context getViewContext();
}
