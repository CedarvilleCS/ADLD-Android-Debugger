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
    // Formatted Output Methods
    //------------------------------------------------------------------------------
    /**
     * @return  A string of all sensor values in Hex value "00, AA, FF, 55"
     */
    public String getOutputAsHex() {
        return String.format("%s, %s, %s, %s",
                leftSensorValue.getHexValue(),
                frontSensorValue.getHexValue(),
                rightSensorValue.getHexValue(),
                sonarSensorValue.getHexValue());
    }

    /**
     * @return  A string of all sensor values in Integer value "0, 170, 255, 45"
     */
    public String getOutputAsIntegers() {
        return String.format("%d, %d, %d, %d",
                leftSensorValue.getIntegerValue(),
                frontSensorValue.getIntegerValue(),
                rightSensorValue.getIntegerValue(),
                sonarSensorValue.getIntegerValue());
    }
}
