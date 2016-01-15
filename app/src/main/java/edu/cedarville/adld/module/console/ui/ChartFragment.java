package edu.cedarville.adld.module.console.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.cedarville.adld.R;
import edu.cedarville.adld.common.base.BaseFragment;
import edu.cedarville.adld.common.dagger.Components;
import edu.cedarville.adld.common.model.DataPoint;
import edu.cedarville.adld.common.view.DataPointSquaresView;
import edu.cedarville.adld.module.console.presenter.ChartEventHandler;

public class ChartFragment extends BaseFragment implements ChartViewInterface {


    @Inject
    ChartEventHandler eventHandler;

    @Bind(R.id.line_chart)
    LineChart lineChart;

    @Bind(R.id.data_point_square_view)
    DataPointSquaresView dataPointView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        ButterKnife.bind(this, view);

        this.setupActivityComponent(components());
        this.eventHandler.attachView(this);
        this.eventHandler.requestViewUpdate();

        return view;
    }

    @Override
    protected void setupActivityComponent(Components components) {
        components.getAppComponent().inject(this);
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
