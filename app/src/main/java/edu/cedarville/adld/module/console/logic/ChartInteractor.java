package edu.cedarville.adld.module.console.logic;

import javax.inject.Inject;

import edu.cedarville.adld.common.dagger.scope.AppScope;
import edu.cedarville.adld.common.model.DataPoint;
import edu.cedarville.adld.common.translator.DataPointTranslator;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import timber.log.Timber;

public class ChartInteractor {

    private final DataPointTranslator translator;

    @Inject @AppScope
    public ChartInteractor(DataPointTranslator translator) {
        this.translator = translator;
    }

    public Observable<DataPoint> getIncomingDataPoints() {
        Timber.e("Getting inputs");
        String[] inputs = {"00,11,22,33", "11,22,33,44", "22,33,44,55", "A0,A1,A2,A3", "0A,1A,2A,3A" };
        return Observable
                .from(inputs)
                .map(new Func1<String, DataPoint>() {
                    @Override
                    public DataPoint call(String input) {
                        Timber.e("Mapping inputs");
                        return translator.translateInputToDataPoint(input);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }


}
