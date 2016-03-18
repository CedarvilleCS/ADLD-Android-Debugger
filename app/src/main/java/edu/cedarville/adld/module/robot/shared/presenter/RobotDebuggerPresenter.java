package edu.cedarville.adld.module.robot.shared.presenter;

import javax.inject.Inject;

import edu.cedarville.adld.common.dagger.Components;
import edu.cedarville.adld.common.model.ConsoleOutput;
import edu.cedarville.adld.common.model.Robot;
import edu.cedarville.adld.common.model.SensorData;
import edu.cedarville.adld.common.rx.OnNextSubscriber;
import edu.cedarville.adld.module.robot.shared.view.RobotDebuggerView;
import rx.Subscription;
import timber.log.Timber;

public class RobotDebuggerPresenter implements RobotDebuggerEventHandler {


    //------------------------------------------------------------------------------
    // Presenter Class Dependencies
    //------------------------------------------------------------------------------
    /**
     * Robot model which will be emitting sensor data
     */
    private final Robot robot;
    /**
     * Application components for dependency injection
     */
    private final Components components;

    private RobotDebuggerView view;
    private Subscription sensorDataSubscription;

    //------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------
    @Inject
    public RobotDebuggerPresenter(Robot robot, Components components) {
        this.robot = robot;
        this.components = components;
    }


    //------------------------------------------------------------------------------
    // Event Handler - View Lifecycle
    //------------------------------------------------------------------------------
    @Override
    public void attachView(RobotDebuggerView view) {
        this.view = view;
        this.view.switchToConsoleView();
    }

    @Override
    public void onViewStarted() {
        this.startObservingRobotSensorData();
    }

    @Override
    public void onViewStopped() {
        this.stopObservingRobotSensorData();
    }

    @Override
    public void detachView() {
        this.view = null;
    }


    //------------------------------------------------------------------------------
    // Event Handler - View Actions
    //------------------------------------------------------------------------------
    @Override
    public void onPlayPressed() {
        this.startObservingRobotSensorData();
    }

    @Override
    public void onPausePressed() {
        this.stopObservingRobotSensorData();
    }


    //------------------------------------------------------------------------------
    // Event Handler - Robot Observation
    //------------------------------------------------------------------------------
    private void startObservingRobotSensorData() {
        if (!isObservingRobotSensorData()) {
            this.sensorDataSubscription = this.robot.getSensorsDataObservable().subscribe(new OnNextSubscriber<SensorData>() {
                @Override
                public void onNext(SensorData sensorData) {
                    Timber.e("Sensor Data: %s", sensorData.getOutputAsHex());
                    view.printOutput(new ConsoleOutput(sensorData.getOutputAsHex()));
                }
            });
        }
        this.adjustViewPlayPauseState();
    }

    private void stopObservingRobotSensorData() {
        if (isObservingRobotSensorData()) {
            this.sensorDataSubscription.unsubscribe();
            this.sensorDataSubscription = null;
        }
        this.adjustViewPlayPauseState();
    }


    //------------------------------------------------------------------------------
    // Event Handler - Utility Methods
    //------------------------------------------------------------------------------
    private boolean isObservingRobotSensorData() {
        return this.sensorDataSubscription != null && !this.sensorDataSubscription.isUnsubscribed();
    }

    private void adjustViewPlayPauseState() {
        this.view.setStatePlaying(isObservingRobotSensorData());
    }
}
