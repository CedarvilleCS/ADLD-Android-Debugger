package edu.cedarville.adld.common.model;

public class SensorActivation {

    public final SensorType type;
    public final boolean active;

    public SensorActivation(SensorType type, boolean active) {
        this.type = type;
        this.active = active;
    }
}
