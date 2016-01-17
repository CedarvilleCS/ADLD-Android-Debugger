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
import edu.cedarville.adld.common.model.DataPoint;
import edu.cedarville.adld.common.model.SensorActivation;
import edu.cedarville.adld.common.model.SensorType;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public class DataPointSquaresView extends LinearLayout {

    public interface OnInputClickedListener {

        /** Called when the user turns on/off an input for display on the graph. */
        void onInputClicked(boolean enabled);
    }

    @Bind (R.id.label_left_sensor)
    TextView leftSensor;
    @Bind(R.id.label_front_sensor)
    TextView frontSensor;
    @Bind(R.id.label_right_sensor)
    TextView rightSensor;
    @Bind(R.id.label_sonar_sensor)
    TextView sonarSensor;


    @Bind(R.id.square_left_sensor)
    View squareLeft;
    @Bind(R.id.square_front_sensor)
    View squareFront;
    @Bind(R.id.square_right_sensor)
    View squareRight;
    @Bind(R.id.square_sonar_sensor)
    View squareSonar;

    private BehaviorSubject<SensorActivation> sensorActiveSubject;

    public DataPointSquaresView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.data_point_square_view, this, true);
        ButterKnife.bind(this);

        this.sensorActiveSubject = BehaviorSubject.create();
    }


    ////
    ////// ButterKnife Event Injection
    ////
    @OnClick(R.id.square_left_sensor)
    void onLeftSensorClicked() {
        this.notifyListenerOfClick(squareLeft, SensorType.LEFT);
    }

    @OnClick(R.id.square_front_sensor)
    void onFrontSensorClicked() {
        this.notifyListenerOfClick(squareFront, SensorType.FRONT);
    }

    @OnClick(R.id.square_right_sensor)
    void onRightSensorClicked() {
        this.notifyListenerOfClick(squareRight, SensorType.RIGHT);
    }

    @OnClick(R.id.square_sonar_sensor)
    void onSonarSensorClicked() {
        this.notifyListenerOfClick(squareSonar, SensorType.SONAR);
    }


    ////
    ////// Public Methods
    ////
    /**
     * Updates the four sensor outputs to match the values of the DataPoint
     * @param dataPoint -   DataPoint which holds sensor values to display
     */
    public void setDataPoint(DataPoint dataPoint) {
        this.leftSensor.setText(dataPoint.getLeftSensorStringValue());
        this.frontSensor.setText(dataPoint.getFrontSensorStringValue());
        this.rightSensor.setText(dataPoint.getRightSensorStringValue());
        this.sonarSensor.setText(dataPoint.getSonarSensorStringValue());
    }

    public Observable<SensorActivation> getActiviationObservable() {
        return sensorActiveSubject.asObservable();
    }


    ////
    ////// Private Methods
    ////
    private void notifyListenerOfClick(View view, SensorType type) {
        boolean selected = view.isSelected();
        view.setSelected(!selected);

        SensorActivation activation = new SensorActivation(type, !selected);
        this.sensorActiveSubject.onNext(activation);

    }
}
