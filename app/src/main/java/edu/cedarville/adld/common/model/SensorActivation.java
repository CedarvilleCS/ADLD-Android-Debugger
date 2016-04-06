package edu.cedarville.adld.common.model;

/**
 * Easy wrapper class which bundles a Sensor Type and if that sensor is active or not. If a user
 * toggles the sensor to active, then that sensors data should be graphed.
 */
public class SensorActivation {


    //------------------------------------------------------------------------------
    // Attributes
    //------------------------------------------------------------------------------
    private final SensorType type;
    private final boolean active;


    //------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------
    public SensorActivation(SensorType type, boolean active) {
        this.type = type;
        this.active = active;
    }


    //------------------------------------------------------------------------------
    // Accessors
    //------------------------------------------------------------------------------
    public SensorType getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }
}
