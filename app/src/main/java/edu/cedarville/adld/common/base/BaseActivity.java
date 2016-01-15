package edu.cedarville.adld.common.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.cedarville.adld.common.ADLDDebuggerApplication;
import edu.cedarville.adld.common.dagger.Components;

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Called when the activity is created. In this method, inject the Activity into
     * the appropriate component to satisfy dependencies
     * @param components    -   Provided Components instance for injection
     */
    protected abstract void setupActivityComponent(Components components);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Satisfy dependencies
        this.setupActivityComponent(components());
    }

    /**
     * Fetches the Components instance from the Application
     * @return  -   Components instance for injection
     */
    protected Components components() {
        return ADLDDebuggerApplication.get(this);
    }
}
