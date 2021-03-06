package edu.cedarville.adld.module.robot.shared.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.cedarville.adld.R;
import edu.cedarville.adld.common.base.BaseActivity;
import edu.cedarville.adld.common.dagger.Components;
import edu.cedarville.adld.common.model.SensorData;
import edu.cedarville.adld.module.robot.chart.ChartFragment;
import edu.cedarville.adld.module.robot.chart.ChartView;
import edu.cedarville.adld.module.robot.console.ConsoleFragment;
import edu.cedarville.adld.module.robot.console.ConsoleView;
import edu.cedarville.adld.module.robot.shared.presenter.RobotDebuggerEventHandler;

public class RobotDebuggerActivity extends BaseActivity implements
        RobotDebuggerView,
        ConsoleFragment.ConsoleFragmentInteractionListener,
        ChartFragment.ChartFragmentInteractionListener {

    //------------------------------------------------------------------------------
    // Activity Intent Factory
    //------------------------------------------------------------------------------
    public static Intent instance(Context context) {
        return new Intent(context, RobotDebuggerActivity.class);
    }


    //------------------------------------------------------------------------------
    // Dependencies
    //------------------------------------------------------------------------------
    @Inject
    RobotDebuggerEventHandler eventHandler;


    //------------------------------------------------------------------------------
    // Views
    //------------------------------------------------------------------------------
    @Bind (R.id.toolbar)
    Toolbar toolbar;

    @Bind (R.id.frame_main)
    FrameLayout mainFrame;


    //------------------------------------------------------------------------------
    // Variables
    //------------------------------------------------------------------------------
    /**
     * Interface allowing access to modify the Console Fragment
     */
    private ConsoleView consoleView;

    /**
     * Interface allowing access to modify the Chart Fragment
     */
    private ChartView chartView;

    /**
     * Boolean flag tracking if output is being displayed or not
     */
    private boolean isPlaying;


    //------------------------------------------------------------------------------
    // Android Lifecycle
    //------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_debugger);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.eventHandler.attachView(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        this.eventHandler.onViewStarted();
    }


    @Override
    protected void onStop() {
        super.onStop();
        this.eventHandler.onViewStopped();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.eventHandler.detachView();
    }


    //------------------------------------------------------------------------------
    // Toolbar Action Menu
    //------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuId;
        if (consoleView == null) {
            menuId = isPlaying ? R.menu.menu_pause_chart: R.menu.menu_play_chart;
        } else {
            menuId = isPlaying ? R.menu.menu_pause_console : R.menu.menu_play_console;
        }
        getMenuInflater().inflate(menuId, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_play:
                this.eventHandler.onPlayPressed();
                return true;

            case R.id.action_pause:
                this.eventHandler.onPausePressed();
                return true;

            case R.id.action_chart:
                this.eventHandler.onChartPressed();
                return true;

            case R.id.action_console:
                this.eventHandler.onConsolePressed();
                return true;

            case R.id.action_disconnect:
                this.eventHandler.onDisconnectPressed();
                return true;

            case R.id.action_settings:
                this.eventHandler.onSettingsPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //------------------------------------------------------------------------------
    // Base Activity Abstract Methods
    //------------------------------------------------------------------------------
    @Override
    protected void setupDependencies(Components components) {
        components.getRobotComponent().inject(this);
    }


    //------------------------------------------------------------------------------
    // Fragment Handling
    //------------------------------------------------------------------------------
    private void showConsoleFragment() {
        this.replaceFragment(ConsoleFragment.instance());
    }


    private void showChartFragment() {
        this.replaceFragment(ChartFragment.instance());
    }


    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(mainFrame.getId(), fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }


    //------------------------------------------------------------------------------
    // Console Fragment Interaction Listener
    //------------------------------------------------------------------------------
    @Override
    public void onConsoleViewCreated(ConsoleView consoleView) {
        this.consoleView = consoleView;
        this.invalidateOptionsMenu();
    }

    @Override
    public void onConsoleViewDestroyed() {
        this.consoleView = null;
    }


    //------------------------------------------------------------------------------
    // Chart Fragment Interaction Listener
    //------------------------------------------------------------------------------
    @Override
    public void onChartViewCreated(ChartView view) {
        this.chartView = view;
        this.invalidateOptionsMenu();
    }

    @Override
    public void onChartViewDestroyed() {
        this.chartView = null;
    }


    //------------------------------------------------------------------------------
    // Robot Debugger View Interface
    //------------------------------------------------------------------------------
    @Override
    public void switchToConsoleView() {
        this.showConsoleFragment();
    }

    @Override
    public void switchToChartView() {
        this.showChartFragment();
    }

    @Override
    public void setStatePlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
        this.invalidateOptionsMenu();
    }

    @Override
    public void printOutput(String output) {
        if (consoleView != null) {
            this.consoleView.addConsoleRow(output);
        }
    }

    @Override
    public void showSensorData(SensorData data) {
        if (chartView != null) {
            this.chartView.showSensorData(data);
        }
    }

    @Override
    public void dismissView() {
        this.finish();
    }

    @Override
    public Context getViewContext() {
        return this;
    }
}
