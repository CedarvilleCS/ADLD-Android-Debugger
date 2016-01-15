package edu.cedarville.adld.module.console.presenter;

import edu.cedarville.adld.module.console.ui.ChartViewInterface;

public interface ChartEventHandler {

    void attachView(ChartViewInterface view);
    void requestViewUpdate();
    void detachView();
}
