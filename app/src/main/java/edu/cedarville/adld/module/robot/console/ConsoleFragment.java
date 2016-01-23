package edu.cedarville.adld.module.robot.console;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.cedarville.adld.R;
import edu.cedarville.adld.common.model.ConsoleOutput;

/**
 * ConsoleFragment.java
 * Created by Daniel Rees on 3/16/16
 *
 * Fragment for displaying the Console view. Any activity that hosts this fragment must
 * implement {@link edu.cedarville.adld.module.robot.console.ConsoleFragment.ConsoleFragmentInteractionListener} in
 * order to function properly
 */
public class ConsoleFragment extends Fragment implements ConsoleView {

    //------------------------------------------------------------------------------
    // Fragment Factory
    //------------------------------------------------------------------------------
    public static ConsoleFragment instance() {
        return new ConsoleFragment();
    }


    //------------------------------------------------------------------------------
    // Fragment Interaction Callback
    //------------------------------------------------------------------------------
    /**
     * Listener class which will be informed of events in the fragment
     */
    public interface ConsoleFragmentInteractionListener {
        void onConsoleViewCreated(ConsoleView view);
        void onConsoleViewDestroyed();
    }


    //------------------------------------------------------------------------------
    // View Objects
    //------------------------------------------------------------------------------
    @Bind(R.id.list_console)
    ListView console;


    //------------------------------------------------------------------------------
    // Class Variables
    //------------------------------------------------------------------------------
    /** Listener notified of fragment events */
    private ConsoleFragmentInteractionListener mListener;
    /** List containing console output models */
    private List<ConsoleOutput> outputsList;
    /** Adapter which will provide the backing data for the ListView */
    private ArrayAdapter<ConsoleOutput> consoleAdapter;



    //------------------------------------------------------------------------------
    // Android Fragment Lifecycle
    //------------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_console, container, false);
        ButterKnife.bind(this, view);

        this.outputsList = new ArrayList<>();
        this.consoleAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, outputsList);
        this.console.setAdapter(consoleAdapter);

        this.mListener.onConsoleViewCreated(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.mListener.onConsoleViewDestroyed();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ConsoleFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    //------------------------------------------------------------------------------
    // Console View Interface
    //------------------------------------------------------------------------------
    /**
     * Adds a new row to the Console
     * @param output    Output to be displayed
     */
    @Override
    public void addConsoleRow(ConsoleOutput output) {
        this.outputsList.add(output);
        this.consoleAdapter.notifyDataSetChanged();
    }

}
