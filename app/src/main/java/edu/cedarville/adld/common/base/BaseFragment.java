package edu.cedarville.adld.common.base;

import android.support.v4.app.Fragment;

import edu.cedarville.adld.common.ADLDDebuggerApplication;
import edu.cedarville.adld.common.dagger.Components;

public abstract class BaseFragment extends Fragment {

    /**
     * Called when the activity is created. In this method, inject the Activity into
     * the appropriate component to satisfy dependencies
     * @param components    -   Provided Components instance for injection
     */
    protected abstract void setupActivityComponent(Components components);


    /**
     * Fetches the Components instance from the Application
     * @return  -   Components instance for injection
     */
    protected Components components() {
        return ADLDDebuggerApplication.get(getActivity());
    }
}
