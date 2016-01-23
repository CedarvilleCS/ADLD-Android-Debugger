package edu.cedarville.adld.common.utility;

import android.content.Context;

import javax.inject.Inject;

import edu.cedarville.adld.common.dagger.Components;
import edu.cedarville.adld.common.dagger.scopes.AppScope;
import edu.cedarville.adld.common.model.Robot;
import edu.cedarville.adld.module.robot.shared.view.RobotDebuggerActivity;

public class Navigator {


    //------------------------------------------------------------------------------
    // Dependencies
    //------------------------------------------------------------------------------
    private final Components components;


    //------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------
    @Inject @AppScope
    public Navigator(Components components) {
        this.components = components;
    }


    //------------------------------------------------------------------------------
    // Navigation Methods
    //------------------------------------------------------------------------------
    public void navigateToRobotDebugger(Context context, Robot robot) {
        components.createRobotComponent(robot);
        context.startActivity(RobotDebuggerActivity.instance(context));
    }
}
