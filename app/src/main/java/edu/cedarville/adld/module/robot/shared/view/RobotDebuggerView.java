package edu.cedarville.adld.module.robot.shared.view;

import edu.cedarville.adld.common.model.ConsoleOutput;

public interface RobotDebuggerView {

    void switchToConsoleView();
    void switchToChartView();

    void setStatePlaying(boolean isPlaying);

    void printOutput(ConsoleOutput output);
}
