package edu.cedarville.adld.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.cedarville.adld.R;
import edu.cedarville.adld.common.model.SensorActivation;
import edu.cedarville.adld.common.model.SensorData;
import edu.cedarville.adld.common.model.SensorType;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public class DataPointSquaresView extends LinearLayout {


    //------------------------------------------------------------------------------
    // Child Views
    //------------------------------------------------------------------------------
    @Bind (R.id.label_left_sensor)
    TextView leftSensor;
    @Bind (R.id.label_front_sensor)
    TextView frontSensor;
    @Bind (R.id.label_right_sensor)
    TextView rightSensor;
    @Bind (R.id.label_sonar_sensor)
    TextView sonarSensor;


    @Bind (R.id.square_left_sensor)
    View squareLeft;
    @Bind (R.id.square_front_sensor)
    View squareFront;
    @Bind (R.id.square_right_sensor)
    View squareRight;
    @Bind (R.id.square_sonar_sensor)
    View squareSonar;


    //------------------------------------------------------------------------------
    // View Variables
    //------------------------------------------------------------------------------
    private BehaviorSubject<SensorActivation> sensorActiveSubject;


    //------------------------------------------------------------------------------
    // View Allocation
    //------------------------------------------------------------------------------
    public DataPointSquaresView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.data_point_square_view, this, true);
        ButterKnife.bind(this);

        this.sensorActiveSubject = BehaviorSubject.create();
    }


    //------------------------------------------------------------------------------
    // ButterKnife Event Injection
    //------------------------------------------------------------------------------
    @OnClick (R.id.square_left_sensor)
    void onLeftSensorClicked() {
        this.notifyListenerOfClick(squareLeft, SensorType.LEFT);
    }

    @OnClick (R.id.square_front_sensor)
    void onFrontSensorClicked() {
        this.notifyListenerOfClick(squareFront, SensorType.FRONT);
    }

    @OnClick (R.id.square_right_sensor)
    void onRightSensorClicked() {
        this.notifyListenerOfClick(squareRight, SensorType.RIGHT);
    }

    @OnClick (R.id.square_sonar_sensor)
    void onSonarSensorClicked() {
        this.notifyListenerOfClick(squareSonar, SensorType.SONAR);
    }


    //------------------------------------------------------------------------------
    // Public View Methods
    //------------------------------------------------------------------------------

    /**
     * Sets the data that is displayed in the four lower squares that represents each sensor
     *
     * @param data Data to display
     */
    public void setSensorData(SensorData data) {
        this.leftSensor.setText(data.getLeftSensorValue().toString());
        this.frontSensor.setText(data.getFrontSensorValue().toString());
        this.rightSensor.setText(data.getRightSensorValue().toString());
        this.sonarSensor.setText(data.getSonarSensorValue().toString());
    }

    /**
     * @return An Observable that emits when a Sensor has been activated or deactivated
     */
    public Observable<SensorActivation> getActivationObservable() {
        return sensorActiveSubject.asObservable();
    }

    public boolean isLeftSensorActive() {
        return leftSensor.isSelected();
    }

    public boolean isFrontSensorActive() {
        return frontSensor.isSelected();
    }

    public boolean isRightSensorActive() {
        return rightSensor.isSelected();
    }

    public boolean isSonarSensorActive() {
        return sonarSensor.isSelected();
    }

    public boolean isAtLeastOneSensorActive() {
        return isLeftSensorActive()
                || isFrontSensorActive()
                || isRightSensorActive()
                || isSonarSensorActive();
    }

    //------------------------------------------------------------------------------
    // Utility Methods
    //------------------------------------------------------------------------------

    /**
     * Generates an object to be emitted to notify of a Sensor becoming Active or Inactive
     *
     * @param view View which was toggled
     * @param type Type of sensor that was toggled
     */
    private void notifyListenerOfClick(View view, SensorType type) {
        boolean selected = view.isSelected();
        view.setSelected(!selected);

        SensorActivation activation = new SensorActivation(type, !selected);
        this.sensorActiveSubject.onNext(activation);

    }
}
