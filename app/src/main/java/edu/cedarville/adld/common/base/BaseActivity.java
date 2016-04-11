package edu.cedarville.adld.common.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.cedarville.adld.common.ADLDDebuggerApplication;
import edu.cedarville.adld.common.dagger.Components;

public abstract class BaseActivity extends AppCompatActivity {

    
    //------------------------------------------------------------------------------
    // Abstract Methods
    //------------------------------------------------------------------------------
    protected abstract void setupDependencies(Components components);


    //------------------------------------------------------------------------------
    // Android Lifecycle
    //------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setupDependencies(getComponents());
    }

    //------------------------------------------------------------------------------
    // Inherited Methods
    //------------------------------------------------------------------------------
    protected Components getComponents() {
        return ADLDDebuggerApplication.get(this);
    }

}
