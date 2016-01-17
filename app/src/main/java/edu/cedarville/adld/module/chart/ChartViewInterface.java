package edu.cedarville.adld.module.chart;

import com.github.mikephil.charting.data.LineData;

import edu.cedarville.adld.common.model.DataPoint;

public interface ChartViewInterface {

    void setIncomingDataPoint(DataPoint dataPoint);
    void setChartLineData(LineData data);
    void refreshChart();

}
