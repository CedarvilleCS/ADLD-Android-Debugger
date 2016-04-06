package edu.cedarville.adld.common.model;

/**
 * SensorData.java
 * Created by Daniel Rees on 3/4/16
 *
 * Sensor Data represents an entire packet received through the bluetooth connection. Data is
 * expected to be in the format of LL,FF,RR,SS where LL is Left Sensor, FF is Front Sensor, RR is
 * Right Sensor and SS is Sonar Sensor
 */
public class SensorData {


    //------------------------------------------------------------------------------
    // Class Variables
    //------------------------------------------------------------------------------
    private final SensorValue leftSensorValue;
    private final SensorValue frontSensorValue;
    private final SensorValue rightSensorValue;
    private final SensorValue sonarSensorValue;



    //------------------------------------------------------------------------------
    // Class Constructor
    //------------------------------------------------------------------------------
    public SensorData(SensorValue leftSensorValue, SensorValue frontSensorValue, SensorValue rightSensorValue, SensorValue sonarSensorValue) {
        this.leftSensorValue = leftSensorValue;
        this.frontSensorValue = frontSensorValue;
        this.rightSensorValue = rightSensorValue;
        this.sonarSensorValue = sonarSensorValue;
    }



    //------------------------------------------------------------------------------
    // Accessor Methods
    //------------------------------------------------------------------------------
    public SensorValue getLeftSensorValue() {
        return leftSensorValue;
    }

    public SensorValue getFrontSensorValue() {
        return frontSensorValue;
    }

    public SensorValue getRightSensorValue() {
        return rightSensorValue;
    }

    public SensorValue getSonarSensorValue() {
        return sonarSensorValue;
    }


    //------------------------------------------------------------------------------
    // Overridden Methods
    //------------------------------------------------------------------------------
    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s",
                leftSensorValue,
                frontSensorValue,
                rightSensorValue,
                sonarSensorValue);
    }
}