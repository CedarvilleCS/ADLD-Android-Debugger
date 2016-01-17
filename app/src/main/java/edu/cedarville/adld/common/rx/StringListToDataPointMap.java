package edu.cedarville.adld.common.rx;

import java.util.List;

import edu.cedarville.adld.common.model.DataPoint;
import edu.cedarville.adld.common.translator.DataPointTranslator;
import rx.functions.Func1;

public class StringListToDataPointMap implements Func1<List<String>, DataPoint> {

    private final DataPointTranslator translator;

    public StringListToDataPointMap(DataPointTranslator translator) {
        this.translator = translator;
    }

    @Override
    public DataPoint call(List<String> strings) {
        return this.translator.translateInputToDataPoint(strings);
    }
}
