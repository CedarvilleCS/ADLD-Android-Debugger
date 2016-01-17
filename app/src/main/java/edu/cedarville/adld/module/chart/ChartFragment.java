package edu.cedarville.adld.module.chart;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.cedarville.adld.R;
import edu.cedarville.adld.common.model.DataPoint;
import edu.cedarville.adld.common.model.SensorActivation;
import edu.cedarville.adld.common.model.SensorType;
import edu.cedarville.adld.common.view.DataPointSquaresView;
import rx.Subscription;
import rx.functions.Action1;

public class ChartFragment extends Fragment implements ChartViewInterface {

    public interface ChartViewEventListener {
        void onChartViewDestroyed();
    }

    @Bind(R.id.line_chart)
    LineChart lineChart;

    @Bind(R.id.data_point_square_view)
    DataPointSquaresView dataPointView;

    private ChartViewEventListener eventListener;
    private Subscription subscription;

    private boolean leftActive;
    private boolean frontActive;
    private boolean rightActive;
    private boolean sonarActive;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        ButterKnife.bind(this, view);

        this.lineChart.setDescription("");
        this.lineChart.setNoDataText("No data to plot");
        this.lineChart.setNoDataTextDescription("Select sensors to graph their data");
        this.lineChart.setDrawGridBackground(false);

        // Set empty data
        final LineData data = new LineData();
        this.lineChart.setData(data);


        // Modify X-Axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);

        // Modify Left Y-Axis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMaxValue(275.0f);
        leftAxis.setDrawGridLines(true);

        // Remove Right Y-Axis
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        // Create all 4 data sets
        data.addDataSet(createSet(SensorType.LEFT.name, SensorType.LEFT.colorId));
        data.addDataSet(createSet(SensorType.FRONT.name, SensorType.FRONT.colorId));
        data.addDataSet(createSet(SensorType.RIGHT.name, SensorType.RIGHT.colorId));
        data.addDataSet(createSet(SensorType.SONAR.name, SensorType.SONAR.colorId));

        this.subscription = dataPointView.getActiviationObservable().subscribe(new Action1<SensorActivation>() {
            @Override
            public void call(SensorActivation sensorActivation) {
                switch (sensorActivation.type) {
                    case LEFT:
                        leftActive = sensorActivation.active;
                        break;
                    case FRONT:
                        frontActive = sensorActivation.active;
                        break;
                    case RIGHT:
                        rightActive = sensorActivation.active;
                        break;
                    case SONAR:
                        sonarActive = sensorActivation.active;
                        break;
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.eventListener.onChartViewDestroyed();
        this.subscription.unsubscribe();
        this.subscription = null;
    }

    @Override
    public void onAttach(Context context) {
        try {
            super.onAttach(context);
            this.eventListener = (ChartViewEventListener) getActivity();
        } catch (ClassCastException e) {
            throw new IllegalStateException("ChartFragment's host activity must implement ChartViewEventListener");
        }
    }

    ////
    ////// Chart View Interface
    ////
    @Override
    public void setIncomingDataPoint(DataPoint dataPoint) {
        this.dataPointView.setDataPoint(dataPoint);

        // Create graph entries for each data point
        Entry leftEntry = new Entry(dataPoint.leftSensor, dataPoint.index);
        Entry frontEntry = new Entry(dataPoint.frontSensor, dataPoint.index);
        Entry rightEntry = new Entry(dataPoint.rightSensor, dataPoint.index);
        Entry sonarEntry = new Entry(dataPoint.sonarSensor, dataPoint.index);

        // Get the data object to update
        LineData data = lineChart.getData();

        if (data != null) {

            LineDataSet leftSet = data.getDataSetByLabel(SensorType.LEFT.name, true);
            LineDataSet frontSet = data.getDataSetByLabel(SensorType.FRONT.name, true);
            LineDataSet rightSet = data.getDataSetByLabel(SensorType.RIGHT.name, true);
            LineDataSet sonarSet = data.getDataSetByLabel(SensorType.SONAR.name, true);

            // Add the next index on the x axis
            data.addXValue(Integer.toString(dataPoint.index));

            // Add DataPoints that are considered active
            this.addEntryToData(leftActive, leftEntry, leftSet, data);
            this.addEntryToData(frontActive, frontEntry, frontSet, data);
            this.addEntryToData(rightActive, rightEntry, rightSet, data);
            this.addEntryToData(sonarActive, sonarEntry, sonarSet, data);

            // inform chart of change
            lineChart.notifyDataSetChanged();

            // limit the number of visible entries
            lineChart.setVisibleXRangeMaximum(20);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            lineChart.moveViewToX(data.getXValCount() - 21);
        }
    }

    @Override
    public void setChartLineData(LineData data) {
        this.lineChart.setData(data);
    }

    @Override
    public void refreshChart() {
        this.lineChart.notifyDataSetChanged();
        this.lineChart.invalidate();
    }


    ////
    ////// Class Helper Methods
    ////
    private LineDataSet createSet(String name, int colorId) {
        LineDataSet set = new LineDataSet(null, name);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(getResources().getColor(colorId));
        set.setCircleColor(getResources().getColor(colorId));
        set.setLineWidth(2f);
        set.setCircleSize(2f);
        set.setFillAlpha(85);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private void addEntryToData(boolean active, Entry entry, LineDataSet set, LineData data) {
        if (set != null && active) {
            data.addEntry(entry, data.getIndexOfDataSet(set));
        }
    }

}
