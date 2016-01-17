package edu.cedarville.adld.common.rx;

import rx.functions.Func1;

public class IntegerToStringMap implements Func1<Integer, String> {

    @Override
    public String call(Integer integer) {
        return integer.toString();
    }
}
