package edu.cedarville.adld.common.model;

/**
 * DataPoint models the data that is received by the device from the robot's bluetooth transmitter.
 * Each set of data that is transmitted is in the form
 *
 * A,B,C,D
 *
 * Where A is the left sensor, B is the front sensor, C is the right sensor, and D is the sonar
 * sensor. Each value, A-D, can be a pair of any two Hex values 00-FF. A valid input would be
 *
 * A0,15,82,EF
 *
 * This class uses the converted values of the input and makes them available as integers.
 */
public class DataPoint {

    /** Integer value of the left sensor for this data point */
    public final int leftSensor;

    /** Integer value of the front sensor for this data point */
    public final int frontSensor;

    /** Integer value of the right sensor for this data point */
    public final int rightSensor;

    /** Integer value of the sonar sensor for this data point */
    public final int sonarSensor;

    private DataPoint(Builder builder) {
        leftSensor = builder.leftSensor;
        frontSensor = builder.frontSensor;
        rightSensor = builder.rightSensor;
        sonarSensor = builder.sonarSensor;
    }


    ////
    ////// Class Methods
    ////
    public String getLeftSensorHexValue() {
        return Integer.toHexString(leftSensor);
    }

    public String getFrontSensorHexValue() {
        return Integer.toHexString(frontSensor);
    }

    public String getRightSensorHexValue() {
        return Integer.toHexString(rightSensor);
    }

    public String getSonarSensorHexValue() {
        return Integer.toHexString(sonarSensor);
    }

    public String getLeftSensorStringValue() {
        return Integer.toString(leftSensor);
    }

    public String getFrontSensorStringValue() {
        return Integer.toString(frontSensor);
    }

    public String getRightSensorStringValue() {
        return Integer.toString(rightSensor);
    }

    public String getSonarSensorStringValue() {
        return Integer.toString(sonarSensor);
    }


    ////
    ////// Class Builder
    ////
    public static final class Builder {
        private int leftSensor;
        private int frontSensor;
        private int rightSensor;
        private int sonarSensor;

        public Builder() {
        }

        public Builder withLeftSensor(int val) {
            leftSensor = val;
            return this;
        }

        public Builder withFrontSensor(int val) {
            frontSensor = val;
            return this;
        }

        public Builder withRightSensor(int val) {
            rightSensor = val;
            return this;
        }

        public Builder withSonarSensor(int val) {
            sonarSensor = val;
            return this;
        }

        public DataPoint build() {
            return new DataPoint(this);
        }
    }
}
