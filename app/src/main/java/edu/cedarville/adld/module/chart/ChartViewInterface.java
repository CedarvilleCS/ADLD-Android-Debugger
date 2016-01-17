package edu.cedarville.adld.module.chart;

import edu.cedarville.adld.common.model.DataPoint;

public interface ChartViewInterface {

    void setIncomingDataPoint(DataPoint dataPoint);
    void setDisplayHex(boolean displayHex);
}
