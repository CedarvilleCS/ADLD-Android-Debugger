package edu.cedarville.adld.module.robot.chart;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.cedarville.adld.R;
import edu.cedarville.adld.common.model.SensorData;
import edu.cedarville.adld.common.model.SensorValue;
import edu.cedarville.adld.common.view.DataPointSquaresView;

public class ChartFragment extends Fragment implements ChartView {


    //------------------------------------------------------------------------------
    // Static Variables
    //------------------------------------------------------------------------------
    private static final String LABEL_LEFT_SENSOR = "Left";
    private static final String LABEL_FRONT_SENSOR = "Front";
    private static final String LABEL_RIGHT_SENSOR = "Right";
    private static final String LABEL_SONAR_SENSOR = "Sensor";


    //------------------------------------------------------------------------------
    // Fragment Factory Method
    //------------------------------------------------------------------------------
    public static ChartFragment instance() {
        return new ChartFragment();
    }


    //------------------------------------------------------------------------------
    // Fragment Interaction Listener
    //------------------------------------------------------------------------------
    public interface ChartFragmentInteractionListener {
        void onChartViewCreated(ChartView view);

        void onChartViewDestroyed();
    }


    //------------------------------------------------------------------------------
    // Fragment Views
    //------------------------------------------------------------------------------
    @Bind (R.id.line_chart)
    LineChart lineChart;

    @Bind (R.id.data_point_square_view)
    DataPointSquaresView dataPointSquaresView;


    //------------------------------------------------------------------------------
    // Fragment Variables
    //------------------------------------------------------------------------------
    /**
     * Reference to the parent activity. Informs of view creation/destruction
     */
    private ChartFragmentInteractionListener mListener;

    /**
     * Tracks the index on the chart to add a data point at
     */
    private int chartIndex;


    //------------------------------------------------------------------------------
    // Fragment Lifecycle Methods
    //------------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        ButterKnife.bind(this, view);

        this.setLineChartDefaults();
        this.mListener.onChartViewCreated(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.mListener.onChartViewDestroyed();

    }

    @Override
    public void onAttach(Context context) {
        try {
            super.onAttach(context);
            this.mListener = (ChartFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new IllegalStateException("ChartFragment's host activity must implement ChartViewEventListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }


    //------------------------------------------------------------------------------
    // Chart View Interface
    //------------------------------------------------------------------------------
    @Override
    public void showSensorData(SensorData data) {
        this.dataPointSquaresView.setSensorData(data);
        this.addSensorEntry(data);
    }



    //------------------------------------------------------------------------------
    // Chart Utility
    //------------------------------------------------------------------------------
    /**
     * Sets the default settings for the LineChart such as colors, titles, etc.
     */
    private void setLineChartDefaults() {
        lineChart.setDescription("");
        lineChart.setData(new LineData());

        lineChart.setPinchZoom(false);

        // Set the left Y-Axis values
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinValue(0f);
        yAxis.setAxisMaxValue(260f);

        // Disable the right Y-Axis
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }


    private void addSensorEntry(SensorData value) {
        LineData data = lineChart.getData();

        // Ignore entries if the data is null or no Sensors have been set to active
        if (data != null && dataPointSquaresView.isAtLeastOneSensorActive()) {

            // Get the DataSet that is related to each Sensor
            ILineDataSet leftDataSet = getLineDataSet(data, LABEL_LEFT_SENSOR, R.color.left_sensor_color);
            ILineDataSet frontDataSet = getLineDataSet(data, LABEL_FRONT_SENSOR, R.color.front_sensor_color);
            ILineDataSet rightDataSet = getLineDataSet(data, LABEL_RIGHT_SENSOR, R.color.right_sensor_color);
            ILineDataSet sonarDataSet = getLineDataSet(data, LABEL_SONAR_SENSOR, R.color.sonar_sensor_color);


            // If the sensor is active, add it to the DataSet
            addSensorValue(dataPointSquaresView.isLeftSensorActive(), data, value.getLeftSensorValue(), leftDataSet);
            addSensorValue(dataPointSquaresView.isFrontSensorActive(), data, value.getFrontSensorValue(), frontDataSet);
            addSensorValue(dataPointSquaresView.isRightSensorActive(), data, value.getRightSensorValue(), rightDataSet);
            addSensorValue(dataPointSquaresView.isSonarSensorActive(), data, value.getSonarSensorValue(), sonarDataSet);

            // Add a new X position
            data.addXValue(String.format("%d", chartIndex));
            chartIndex++;

            // Update the Chart view for drawing
            lineChart.notifyDataSetChanged();
            lineChart.setVisibleXRangeMaximum(100);
            lineChart.moveViewToX(data.getXValCount() + 10);
        }
    }


    private ILineDataSet getLineDataSet(LineData data, String label, int colorId) {
        ILineDataSet dataSet = data.getDataSetByLabel(label, true);

        if (dataSet == null) {
            dataSet = createSet(label, colorId);
            data.addDataSet(dataSet);
        }

        return dataSet;
    }


    private void addSensorValue(boolean isSensorActive, LineData data, SensorValue value, ILineDataSet dataSet ) {
        if (isSensorActive) {
            Entry leftEntry = new Entry(value.getIntegerValue(), chartIndex);
            data.addEntry(leftEntry, data.getIndexOfDataSet(dataSet));
        }
    }


    private LineDataSet createSet(String label, int colorId) {
        LineDataSet set = new LineDataSet(null, label);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(getResources().getColor(colorId));
        set.setLineWidth(2f);
        set.setFillAlpha(65);
        set.setDrawCircles(false);
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

}
