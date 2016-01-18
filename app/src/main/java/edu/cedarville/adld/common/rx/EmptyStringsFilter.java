package edu.cedarville.adld.common.rx;

import rx.functions.Func1;

public class EmptyStringsFilter implements Func1<String, Boolean> {
    @Override
    public Boolean call(String s) {
        return !s.isEmpty();
    }
}
