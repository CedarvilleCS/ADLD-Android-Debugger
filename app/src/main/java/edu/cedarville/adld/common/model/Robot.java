package edu.cedarville.adld.common.model;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func4;
import rx.observables.MathObservable;
import rx.subjects.PublishSubject;

/**
 * Robot.java
 * Created by Daniel Rees on 3/4/16
 *
 * Primary class for modeling the Robot that we are connecting to. The actual Robots that are being
 * built have four sensors. Those are LEFT, MIDDLE, RIGHT, and SONAR. See {@link SensorType} for the
 * Enum values. Each one of these sensors will be emitting the values that they receive through their
 * bluetooth UART device.
 *
 * The purpose of this class is to model that behavior. The Robot class provides four sensors, each
 * of which will emit the values of the actual sensors of the Robot.
 */
public class Robot {

    //------------------------------------------------------------------------------
    // Class Variables
    //------------------------------------------------------------------------------
    /**
     * Gives us access to the Bluetooth library that maintains connectivity with the bluetooth chip
     */
    private final BluetoothSPP bluetoothSPP;

    /**
     * Provides a listening interface for receiving data from the {@link #bluetoothSPP}
     */
    private final BluetoothSPP.OnDataReceivedListener dataReceivedListener = new BluetoothSPP.OnDataReceivedListener() {
        /**
         * Called every time data has been emitted from the Bluetooth device and received by the phone
         * @param data  Byte data emitted. We are ignoring this value
         * @param message   Message that was emitted. This is the value that we care about.
         */
        @Override
        public void onDataReceived(byte[] data, String message) {
            Robot.this.handleBluetoothDataReceived(message);
        }
    };


    //------------------------------------------------------------------------------
    // Sensors
    //------------------------------------------------------------------------------
    /** Subject emits left sensor data */
    private PublishSubject<SensorValue> leftSensor;
    /** Subject emits front sensor data */
    private PublishSubject<SensorValue> frontSensor;
    /** Subject emits right sensor data */
    private PublishSubject<SensorValue> rightSensor;
    /** Subject emits sonar sensor data */
    private PublishSubject<SensorValue> sonarSensor;


    //------------------------------------------------------------------------------
    // Robot Settings
    //------------------------------------------------------------------------------
    /** Display the output in hex. False to display in decimal. */
    private boolean displayHex;
    /** Sets the threshold of data points to average together. */
    private int averageThreshold;


    //------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------
    /**
     * Constructs a Robot model which will emit the sensor values.
     * @param bluetoothSPP  BluetoothSPP maintaining the connection to the physical Robot.
     */
    public Robot(BluetoothSPP bluetoothSPP) {
        this.bluetoothSPP = bluetoothSPP;
        this.bluetoothSPP.setOnDataReceivedListener(dataReceivedListener);

        this.leftSensor = PublishSubject.create();
        this.frontSensor = PublishSubject.create();
        this.rightSensor = PublishSubject.create();
        this.sonarSensor = PublishSubject.create();

        // Default is to display hex
        this.displayHex = true;
        // Default threshold is 1
        this.averageThreshold = 1;
    }


    /**
     * Disconnects from the Robot
     */
    public void disconnect() {
        this.bluetoothSPP.disconnect();
    }


    //------------------------------------------------------------------------------
    // Robot Accessors
    //------------------------------------------------------------------------------
    /**
     * Gets the name of the Robot. This is the name of the Bluetooth device
     * @return  Name of the Robot/Bluetooth device
     */
    public String getRobotName() {
        return this.bluetoothSPP.getConnectedDeviceName();
    }

    /**
     * @return  Bluetooth address of the robot
     */
    public String getRobotAddress() {
        return this.bluetoothSPP.getConnectedDeviceAddress();
    }

    /**
     * @return  True if the robot is displaying hex. False if it is displaying decimal
     */
    public boolean isDisplayHex() {
        return displayHex;
    }

    /**
     * @return  Threshold count of values to be averaged together.
     */
    public int getAverageThreshold() {
        return averageThreshold;
    }

    //------------------------------------------------------------------------------
    // Robot Setters
    //------------------------------------------------------------------------------

    /**
     * Sets the name of the Robot. This will also set the name of the Bluetooth device
     * @param name  New name to set to the Robot
     */
    public void setRobotName(String name) {
        this.bluetoothSPP.getBluetoothAdapter().setName(name);
    }

    /**
     * Sets if the output data should be displayed in hex or in decimal.
     * @param displayHex    True to output hex. False to output decimal
     */
    public void setDisplayHex(boolean displayHex) {
        this.displayHex = displayHex;
    }

    /**
     * Sets the threshold of data points to average together. Example, if the threshold is set
     * to 10, then every 10 values will be averaged together and emitted as a single value
     * @param averageThreshold  Threshold value
     */
    public void setAverageThreshold(int averageThreshold) {
        this.averageThreshold = averageThreshold;
    }

    //------------------------------------------------------------------------------
    // Private Methods
    //------------------------------------------------------------------------------
    /**
     * Processes the message data that was received through the bluetooth connection
     * @param message   Message emitted by the hardware in the format of "LL,FF,RR,SS"
     */
    private void handleBluetoothDataReceived(String message) {
        // Split the message int an array of 4 individual values
        String[] values = message.split(",");

        // If the data is less than expected length, ignore it.
        if (values.length < 4) return;

        // Create each Sensor Type
        SensorValue leftValue = new SensorValue(values[0], SensorType.LEFT);
        SensorValue frontValue = new SensorValue(values[1], SensorType.FRONT);
        SensorValue rightValue = new SensorValue(values[2], SensorType.RIGHT);
        SensorValue sonarValue = new SensorValue(values[3], SensorType.SONAR);

        // Emit each value on their respective sensor subject
        this.leftSensor.onNext(leftValue);
        this.frontSensor.onNext(frontValue);
        this.rightSensor.onNext(rightValue);
        this.sonarSensor.onNext(sonarValue);
    }


    //------------------------------------------------------------------------------
    // Sensor Emit Methods
    //------------------------------------------------------------------------------
    /**
     * @return  Observable that will emit the SensorValues of the Left Sensor
     */
    public Observable<SensorValue> getLeftSensorObservable() {
        return this.getManipulatedObservable(leftSensor);
    }

    /**
     * @return  Observable that will emit the SensorValues of the Front Sensor
     */
    public Observable<SensorValue> getFrontSensorObservable() {
        return this.getManipulatedObservable(frontSensor);
    }

    /**
     * @return  Observable that will emit the SensorValues of the Right Sensor
     */
    public Observable<SensorValue> getRightSensorObservable() {
        return this.getManipulatedObservable(rightSensor);
    }

    /**
     * @return  Observable that will emit the SensorValues of the Sonar Sensor
     */
    public Observable<SensorValue> getSonarSensorObservable() {
        return this.getManipulatedObservable(sonarSensor);
    }

    /**
     * @return  Observable that will emit the SensorData of all four sensors
     */
    public Observable<SensorData> getSensorsDataObservable() {
        // Convert all 4 sensor streams into one stream that emits all 4 values
        return Observable.zip(
                getLeftSensorObservable(),
                getFrontSensorObservable(),
                getRightSensorObservable(),
                getSonarSensorObservable(),
                new Func4<SensorValue, SensorValue, SensorValue, SensorValue, SensorData>() {
                    @Override
                    public SensorData call(SensorValue leftValue,
                                           SensorValue frontValue,
                                           SensorValue rightValue,
                                           SensorValue sonarValue) {
                        return new SensorData(leftValue, frontValue, rightValue, sonarValue);
                    }
                }
         );
    }



    //------------------------------------------------------------------------------
    // RxJava Stream Manipulation
    //------------------------------------------------------------------------------
    private Observable<SensorValue>  getManipulatedObservable(PublishSubject<SensorValue> subject) {
        return subject
                .asObservable()
                // Turn a SensorValue into an integer value
                .map(new Func1<SensorValue, Integer>() {
                    @Override
                    public Integer call(SensorValue sensorValue) {
                        return sensorValue.getIntegerValue();
                    }
                })
                // Bundle together a group of values
                .window(averageThreshold)

                // Average together that window
                .flatMap(new Func1<Observable<Integer>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Observable<Integer> integerObservable) {
                        return MathObservable.averageInteger(integerObservable);
                    }

                // Turn the average into a SensorValue and emit that
                }).map(new Func1<Integer, SensorValue>() {
                    @Override
                    public SensorValue call(Integer integer) {
                        return new SensorValue(SensorType.LEFT, integer);
                    }
                });
    }
}
