package edu.cedarville.adld.common.model;

/**
 * SensorValue.java
 * Created by Daniel Rees on 3/4/16
 *
 * Each Sensor emits data in the range of 0-255. These values are modeled by the SensorValue class.
 */
public class SensorValue {

    //------------------------------------------------------------------------------
    // Class Variables
    //------------------------------------------------------------------------------
    /**
     * The Radix value to parse Hex to Int.
     */
    private static final int HEX_RADIX = 16;

    /**
     * The actual value emitted by the sensor as a Hex string
     */
    private final String value;

    /**
     * The actual value emitted by the sensor as an integer
     */
    private final int intValue;

    /**
     * True if {@link #toString()} outputs a Hex value. False will output an Integer
     */
    private final boolean displayHex;

    /**
     * The type of Sensor
     */
    private final SensorType type;


    //------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------
    /**
     * Constructs a SensorValue with the ASCII value emitted by the physical Sensor
     * @param value Value of the Sensor. Will be turned into all uppercase
     * @param type  {@link SensorType} of the sensor represented.
     */
    public SensorValue(String value, boolean displayHex, SensorType type) {
        this.value = value.toUpperCase();
        this.displayHex = displayHex;
        this.intValue = Integer.parseInt(this.value, HEX_RADIX);
        this.type = type;
    }

    public SensorValue(int intValue, boolean displayHex, SensorType type) {
        this.value = String.format("%02X", intValue);
        this.displayHex = displayHex;
        this.intValue = intValue;
        this.type = type;
    }

    //------------------------------------------------------------------------------
    // Accessor Methods
    //------------------------------------------------------------------------------
    /**
     * @return  The actual value of the class in 00-FF format.
     */
    public String getHexValue() {
        return this.value;
    }

    /**
     * @return  The actual value of the class in 0-255 format.
     */
    public int getIntegerValue() {
        return this.intValue;
    }

    /**
     * @return  Type of the sensor that emitted the value
     */
    public SensorType getType() {
        return type;
    }


    //------------------------------------------------------------------------------
    // Overridden Methods
    //------------------------------------------------------------------------------
    @Override
    public String toString() {
        return displayHex ? value : Integer.toString(intValue);
    }
}
