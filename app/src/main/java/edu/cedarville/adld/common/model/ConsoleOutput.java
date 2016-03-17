package edu.cedarville.adld.common.model;

/**
 * ConsoleOutput.java
 * Created by Daniel Rees on 3/16/16
 *
 * Model which represents data that can be printed out to the console. The Console view will
 * display a List of ConsoleOutput objects, each printing the {@link #toString()} value of the
 * ConsoleOutput
 */
public class ConsoleOutput {


    //------------------------------------------------------------------------------
    // Model Attributes
    //------------------------------------------------------------------------------
    private final String output;


    //------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------
    public ConsoleOutput(String output) {
        this.output = output;
    }


    //------------------------------------------------------------------------------
    // Overridden Methods
    //------------------------------------------------------------------------------
    @Override
    public String toString() {
        return this.output;
    }
}
