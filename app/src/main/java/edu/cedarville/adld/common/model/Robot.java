package edu.cedarville.adld.common.model;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import rx.Observable;
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

    /** Subject emits left sensor data */
    private PublishSubject<SensorValue> leftSensor;
    /** Subject emits front sensor data */
    private PublishSubject<SensorValue> frontSensor;
    /** Subject emits right sensor data */
    private PublishSubject<SensorValue> rightSensor;
    /** Subject emits sonar sensor data */
    private PublishSubject<SensorValue> sonarSensor;
    /** Subject emits all four data types */
    private PublishSubject<SensorData> sensors;


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
        this.sensors = PublishSubject.create();
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


        // Create each Sensor Type
        SensorValue leftValue = new SensorValue(values[0], SensorType.LEFT);
        SensorValue frontValue = new SensorValue(values[1], SensorType.FRONT);
        SensorValue rightValue = new SensorValue(values[2], SensorType.RIGHT);
        SensorValue sonarValue = new SensorValue(values[3], SensorType.SONAR);
        SensorData data = new SensorData(leftValue, frontValue, rightValue, sonarValue);

        // Emit each value on their respective sensor subject
        this.leftSensor.onNext(leftValue);
        this.frontSensor.onNext(frontValue);
        this.rightSensor.onNext(rightValue);
        this.sonarSensor.onNext(sonarValue);
        this.sensors.onNext(data);
    }


    //------------------------------------------------------------------------------
    // Sensor Emit Methods
    //------------------------------------------------------------------------------
    /**
     * @return  Observable that will emit the SensorValues of the Left Sensor
     */
    public Observable<SensorValue> getLeftSensorObservable() {
        return this.leftSensor.asObservable();
    }

    /**
     * @return  Observable that will emit the SensorValues of the Front Sensor
     */
    public Observable<SensorValue> getFrontSensorObservable() {
        return this.frontSensor.asObservable();
    }

    /**
     * @return  Observable that will emit the SensorValues of the Right Sensor
     */
    public Observable<SensorValue> getRightSensorObservable() {
        return this.rightSensor.asObservable();
    }

    /**
     * @return  Observable that will emit the SensorValues of the Sonar Sensor
     */
    public Observable<SensorValue> getSonarSensorObservable() {
        return this.sonarSensor.asObservable();
    }

    /**
     * @return  Observable that will emit the SensorData of all four sensors
     */
    public Observable<SensorData> getSensorsDataObservable() {
        return this.sensors.asObservable();
    }

}
