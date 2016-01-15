package edu.cedarville.adld.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.cedarville.adld.R;
import edu.cedarville.adld.common.model.DataPoint;

public class DataPointSquaresView extends LinearLayout {

    @Bind (R.id.label_left_sensor)
    TextView leftSensor;
    @Bind(R.id.label_front_sensor)
    TextView frontSensor;
    @Bind(R.id.label_right_sensor)
    TextView rightSensor;
    @Bind(R.id.label_sonar_sensor)
    TextView sonarSensor;

    public DataPointSquaresView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.data_point_square_view, this, true);
        ButterKnife.bind(this);
    }

    /**
     * Updates the four sensor outputs to match the values of the DataPoint
     * @param dataPoint -   DataPoint which holds sensor values to display
     */
    public void setDataPoint(DataPoint dataPoint) {
        this.leftSensor.setText(dataPoint.getLeftSensorStringValue());
        this.rightSensor.setText(dataPoint.getFrontSensorStringValue());
        this.frontSensor.setText(dataPoint.getRightSensorStringValue());
        this.sonarSensor.setText(dataPoint.getSonarSensorStringValue());
    }


}
