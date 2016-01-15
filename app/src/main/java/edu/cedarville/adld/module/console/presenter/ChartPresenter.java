package edu.cedarville.adld.module.console.presenter;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import edu.cedarville.adld.common.dagger.scope.AppScope;
import edu.cedarville.adld.common.model.DataPoint;
import edu.cedarville.adld.module.console.logic.ChartInteractor;
import edu.cedarville.adld.module.console.ui.ChartViewInterface;
import rx.Subscriber;
import rx.Subscription;
import rx.observers.SafeSubscriber;

public class ChartPresenter implements ChartEventHandler {

    private final ChartInteractor interactor;


    private ChartViewInterface view;
    private List<Entry> entries;
    private Subscription incomingDataPointSubscription;

    @Inject @AppScope
    public ChartPresenter(ChartInteractor interactor) {
        this.interactor = interactor;
    }


    ////
    ////// Chart Event Handler Interface
    ////
    @Override
    public void attachView(ChartViewInterface view) {
        this.view = view;
    }

    @Override
    public void requestViewUpdate() {
        this.entries = new ArrayList<>();
        this.incomingDataPointSubscription = this.interactor
                .getIncomingDataPoints()
                .subscribe(new SafeSubscriber<>(new Subscriber<DataPoint>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DataPoint dataPoint) {
                        Entry entry = new Entry(dataPoint.leftSensor, entries.size());
                        entries.add(entry);

                        LineDataSet lineDataSet = new LineDataSet(entries, "Left Sensor");
                        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

                        List<String> xVals = new ArrayList<>();
                        for (int i = 0; i < entries.size(); i++) {
                            xVals.add(String.valueOf(i));
                        }

                        LineData data = new LineData(xVals, lineDataSet);
                        view.setIncomingDataPoint(dataPoint);
                        view.setChartLineData(data);
                        view.refreshChart();
                    }
                }));
    }

    @Override
    public void detachView() {
        this.view = null;
        this.incomingDataPointSubscription.unsubscribe();
        this.incomingDataPointSubscription = null;
    }
}
