package edu.cedarville.adld.common.rx;

import rx.Subscriber;
import timber.log.Timber;

public abstract class OnNextSubscriber<T> extends Subscriber<T>{

    @Override
    public void onCompleted() {
        Timber.e("On Complete");
    }

    @Override
    public void onError(Throwable e) {
        Timber.e(e, "Error on next");
    }
}
