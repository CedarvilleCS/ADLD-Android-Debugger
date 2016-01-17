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
import edu.cedarville.adld.common.view.DataPointSquaresView;

public class ChartFragment extends Fragment implements ChartViewInterface {

    public interface ChartViewEventListener {
        void onChartViewDestroyed();
    }

    @Bind(R.id.line_chart)
    LineChart lineChart;

    @Bind(R.id.data_point_square_view)
    DataPointSquaresView dataPointView;

    private ChartViewEventListener eventListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        ButterKnife.bind(this, view);


        this.lineChart.setDescription("");
        this.lineChart.setNoDataTextDescription("No data has been received");


        this.lineChart.setDrawGridBackground(false);
        this.lineChart.setMaxVisibleValueCount(100);


//        this.leftSensorDataSet = new LineDataSet(new ArrayList<Entry>(), "Left Sensor");
//        this.leftSensorDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
//        this.leftSensorDataSet.setColor(getResources().getColor(R.color.left_sensor_color));
//        this.leftSensorDataSet.setCircleColor(getResources().getColor(R.color.left_sensor_color));

        // Set empty data
        LineData data = new LineData();
        this.lineChart.setData(data);


        // Modify X-Axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);

        // Modify Left Y-Axis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);

        // Remove Right Y-Axis
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.eventListener.onChartViewDestroyed();
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
        Entry entry = new Entry(dataPoint.leftSensor, dataPoint.index);


        LineData data = lineChart.getData();

        if (data != null) {
            LineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addXValue(Integer.toString(dataPoint.index));
            data.addEntry(entry, 0);

            // inform chart of change
            lineChart.notifyDataSetChanged();

            // limit the number of visible entries
            lineChart.setVisibleXRangeMaximum(120);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            lineChart.moveViewToX(data.getXValCount() - 121);

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
    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Left Sensor");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleSize(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

}
