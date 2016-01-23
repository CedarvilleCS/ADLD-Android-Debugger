package edu.cedarville.adld.module.robot.shared.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.cedarville.adld.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class RobotDebuggerFragment extends Fragment {

    public RobotDebuggerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_robot_debugger, container, false);
    }
}
