package edu.cedarville.adld.module.robot.shared.presenter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.inject.Inject;

import edu.cedarville.adld.common.dagger.Components;
import edu.cedarville.adld.common.model.ConsoleOutput;
import edu.cedarville.adld.common.model.Robot;
import edu.cedarville.adld.common.model.SensorData;
import edu.cedarville.adld.common.rx.OnNextSubscriber;
import edu.cedarville.adld.common.utility.Navigator;
import edu.cedarville.adld.module.robot.shared.view.RobotDebuggerView;
import rx.Subscription;

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
    /**
     * Utility class which helps start activities
     */
    private final Navigator navigator;


    //------------------------------------------------------------------------------
    // Presenter Class Variables
    //------------------------------------------------------------------------------
    /** Interface referencing the View (RobotDebuggerActivity) */
    private RobotDebuggerView view;

    /** Informs the event handler how to output data. */
    private boolean outputToChart = false;

    /** Rx Subscriptions used for keeping track of observations of sensor data. */
    private Subscription sensorDataSubscription;
    private Subscription leftSensorSubscription;
    private Subscription frontSensorSubscription;
    private Subscription rightSensorSubscription;
    private Subscription sonarSensorSubscription;



    //------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------
    @Inject
    public RobotDebuggerPresenter(Robot robot, Components components, Navigator navigator) {
        this.robot = robot;
        this.components = components;
        this.navigator = navigator;
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
        this.robot.disconnect();
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

    @Override
    public void onDisconnectPressed() {
        this.view.dismissView();
    }

    @Override
    public void onSettingsPressed() {
        this.navigator.navigateToRobotSettings(view.getViewContext());
    }

    @Override
    public void onChartPressed() {
        this.outputToChart = true;
        this.view.switchToChartView();
    }

    @Override
    public void onConsolePressed() {
        this.outputToChart = false;
        this.view.switchToConsoleView();
    }


    //------------------------------------------------------------------------------
    // Event Handler - Robot Observation
    //------------------------------------------------------------------------------
    private void startObservingRobotSensorData() {
        if (!isObservingRobotSensorData()) {
            this.sensorDataSubscription = this.robot.getSensorsDataObservable().
                    subscribe(new OnNextSubscriber<SensorData>() {
                        @Override
                        public void onNext(SensorData sensorData) {
                            if (outputToChart) {
                                view.showSensorData(sensorData);
                            } else {
                                view.printOutput(new ConsoleOutput(sensorData.getOutputAsHex()));
                            }

                        }
                    });
        }
        this.adjustViewPlayPauseState();
    }

    private void stopObservingRobotSensorData() {
        this.sensorDataSubscription = unsubscribe(sensorDataSubscription);
        this.leftSensorSubscription = unsubscribe(leftSensorSubscription);
        this.frontSensorSubscription = unsubscribe(frontSensorSubscription);
        this.rightSensorSubscription = unsubscribe(rightSensorSubscription);
        this.sonarSensorSubscription = unsubscribe(sonarSensorSubscription);

        this.adjustViewPlayPauseState();
    }



    //------------------------------------------------------------------------------
    // Event Handler - Utility Methods
    //------------------------------------------------------------------------------
    /**
     * @return  True if the SensorData observable which emits all 4 sensors is being observed
     */
    private boolean isObservingRobotSensorData() {
        return isSubscriptionActive(sensorDataSubscription);
    }

    /**
     * @param subscription  Subscription to check
     * @return  True if the subscription is not null and is not unsubscribed to
     */
    private boolean isSubscriptionActive(Subscription subscription) {
        return subscription != null && !subscription.isUnsubscribed();
    }


    /**
     * Changes the view depending on if robot sensors are paused or not
     */
    private void adjustViewPlayPauseState() {
        this.view.setStatePlaying(isObservingRobotSensorData());
        DateTime now = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.fullTime();
        String res = String.format((isObservingRobotSensorData() ? "PLAY " : "PAUSE") + "\n%s" , now.toString(fmt));
        this.view.printOutput(new ConsoleOutput(res));
    }

    /**
     * Checks if it is safe to unsubscribe from a subscription. Always returns null.
     * @param subscription  Subscription to unsubscribe from
     * @return  Always null as a convenience subscription = unsubscribe(subscription);
     */
    private Subscription unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        return null;
    }


}
