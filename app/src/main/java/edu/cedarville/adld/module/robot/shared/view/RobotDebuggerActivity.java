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
import edu.cedarville.adld.common.model.ConsoleOutput;
import edu.cedarville.adld.module.robot.console.ConsoleFragment;
import edu.cedarville.adld.module.robot.console.ConsoleView;
import edu.cedarville.adld.module.robot.shared.presenter.RobotDebuggerEventHandler;

public class RobotDebuggerActivity extends BaseActivity implements
        RobotDebuggerView,
        ConsoleFragment.ConsoleFragmentInteractionListener {

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
    /** Interface allowing access to modify the Console Fragment */
    private ConsoleView consoleView;

    /** Boolean flag tracking if output is being displayed or not */
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
        getMenuInflater().inflate(isPlaying ? R.menu.menu_pause : R.menu.menu_play, menu);
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
        ConsoleFragment fragment = ConsoleFragment.instance();
        this.replaceFragment(fragment);
    }


    private void showChartFragment() {

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
    }

    @Override
    public void onConsoleViewDestroyed() {
        this.consoleView = null;
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
    public void printOutput(ConsoleOutput output) {
        if (consoleView != null) {
            this.consoleView.addConsoleRow(output);
        }
    }
}
