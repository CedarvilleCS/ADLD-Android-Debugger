package edu.cedarville.adld.module.robot.chart;

import edu.cedarville.adld.common.model.DataPoint;

public interface ChartViewInterface {

    void setIncomingDataPoint(DataPoint dataPoint);
    void setDisplayHex(boolean displayHex);
}