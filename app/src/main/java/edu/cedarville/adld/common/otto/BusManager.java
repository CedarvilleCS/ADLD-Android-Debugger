package edu.cedarville.adld.common.otto;

import com.squareup.otto.Bus;

public class BusManager {

    private static BusManager instance;
    public static BusManager getInstance() {
        if (instance == null) {
            instance = new BusManager();
        }
        return instance;
    }

    private Bus bus;

    private BusManager() {
        this.bus = new Bus();
    }

    public void register(Object obj) {
        bus.register(obj);
    }

    public void unregister(Object obj) {
        bus.unregister(obj);
    }

    public void postRunningAverageChangeEvent() {
        bus.post(new RunningAverageChangeEvent());
    }

    public void postDisplayHexChangeEvent() {
        bus.post(new DisplayHexChangeEvent());
    }
}
