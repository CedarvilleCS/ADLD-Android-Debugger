package edu.cedarville.adld.common.dagger;

import edu.cedarville.adld.common.dagger.components.AppComponent;
import edu.cedarville.adld.common.dagger.components.RobotComponent;
import edu.cedarville.adld.common.model.Robot;

public interface Components {

    //------------------------------------------------------------------------------
    // Access Created Components
    //------------------------------------------------------------------------------
    AppComponent getAppComponent();
    RobotComponent getRobotComponent();


    //------------------------------------------------------------------------------
    // Create New Scoped Components
    //------------------------------------------------------------------------------
    void createRobotComponent(Robot robot);


    //------------------------------------------------------------------------------
    // End Existing Scopes
    //------------------------------------------------------------------------------
    void endRobotScope();
}
