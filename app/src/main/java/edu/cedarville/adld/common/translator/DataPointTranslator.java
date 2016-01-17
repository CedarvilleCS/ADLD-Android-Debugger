package edu.cedarville.adld.common.translator;

import java.util.List;

import edu.cedarville.adld.common.model.DataPoint;

public class DataPointTranslator {

    private static final int LEFT_SENSOR_POS = 0;
    private static final int FRONT_SENSOR_POS = 1;
    private static final int RIGHT_SENSOR_POS = 2;
    private static final int SONAR_SENSOR_POS = 3;
    private static final int BASE_16 = 16;

    private int index;

    public DataPointTranslator() {
        index = 0;
        // Default Constructor for injection
    }

    /**
     * Translates an input in hex format "AA,BB,CC,DD" into a DataPoint model
     * @param input -   Input from the robot
     * @return  -   DataPoint model representing the input
     */
    public DataPoint translateInputToDataPoint(List<String> input) {
        // Parse each hex value into an integer value
        int left = 0;
        int front = 0;
        int right = 0;
        int sonar = 0;


        for (String msg : input) {
            String[] values = msg.split(",");

            // Currently, all inputs must have 4 values and will always have 4 value
            if (values.length != 4) {
                throw new IllegalArgumentException("An input with " + values.length + " values was received. Inputs must have 4 values separated by commas");
            }

            // Parse each hex value into an integer value
            left += Integer.parseInt(values[LEFT_SENSOR_POS], BASE_16);
            front += Integer.parseInt(values[FRONT_SENSOR_POS], BASE_16);
            right += Integer.parseInt(values[RIGHT_SENSOR_POS], BASE_16);
            sonar += Integer.parseInt(values[SONAR_SENSOR_POS], BASE_16);
        }


        // Build and return a DataPoint model with int values
        DataPoint dataPoint = new DataPoint.Builder()
                .withIndex(index)
                .withLeftSensor(left / input.size())
                .withFrontSensor(front / input.size())
                .withRightSensor(right / input.size())
                .withSonarSensor(sonar / input.size())
                .build();

        this.index += 1;
        return dataPoint;
    }

}
