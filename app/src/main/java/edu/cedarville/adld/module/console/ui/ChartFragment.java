package edu.cedarville.adld.module.console.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

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
    }

    @Override
    public void setChartLineData(LineData data) {
        this.lineChart.setData(data);
    }

    @Override
    public void refreshChart() {
        this.lineChart.invalidate();
    }
}
