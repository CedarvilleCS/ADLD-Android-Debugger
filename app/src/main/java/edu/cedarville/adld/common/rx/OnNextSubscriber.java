package edu.cedarville.adld.common.rx;

import rx.Subscriber;

public abstract class OnNextSubscriber<T> extends Subscriber<T>{

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }
}
