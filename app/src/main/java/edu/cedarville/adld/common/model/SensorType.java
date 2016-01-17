package edu.cedarville.adld.common.model;

import edu.cedarville.adld.R;

public enum SensorType {

    LEFT("Left Sensor", R.color.left_sensor_color),
    FRONT("Front Sensor", R.color.front_sensor_color),
    RIGHT("Right Sensor", R.color.right_sensor_color),
    SONAR("Sonar Sensor", R.color.sonar_sensor_color);

    public final String name;
    public final int colorId;

    SensorType(String name, int colorId) {
        this.name = name;
        this.colorId = colorId;
    }
}
