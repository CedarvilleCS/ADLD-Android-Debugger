package edu.cedarville.adld.module.robot.console;

import edu.cedarville.adld.common.model.ConsoleOutput;

public interface ConsoleView {
    /**
     * Adds a new row to the Console
     * @param output    Output to be displayed
     */
    void addConsoleRow(ConsoleOutput output);

}
